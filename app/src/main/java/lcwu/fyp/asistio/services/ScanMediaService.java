package lcwu.fyp.asistio.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jiajunhui.xapp.medialoader.MediaLoader;
import com.jiajunhui.xapp.medialoader.bean.FileItem;
import com.jiajunhui.xapp.medialoader.bean.FileResult;
import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lcwu.fyp.asistio.activities.Dashboard;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.User;
import lcwu.fyp.asistio.model.UserFile;

public class ScanMediaService extends Service {
    private DatabaseReference reference;
    private ValueEventListener listener;
    public static Dashboard dashboard;
    private List<FileItem> images, videos, audios, docs;
    private User user;
    private HashMap<String, UserFile> map;
    private List<UserFile> userFiles;

    public ScanMediaService() {
        videos = new ArrayList<>();
        audios = new ArrayList<>();
        docs = new ArrayList<>();
        images = new ArrayList<>();
        map = new HashMap<>();
        userFiles = new ArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Session session = new Session(this);
        user = session.getUser();
        reference = FirebaseDatabase.getInstance().getReference().child("UserFiles").child(user.getId());

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listener != null)
                    reference.removeEventListener(listener);
                Log.e("ScanMediaService", "Data reading success");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    UserFile file = data.getValue(UserFile.class);
                    if (file != null) {
                        map.put(data.getKey(), file);
                    }
                }
                Log.e("ScanMediaService", "Map size: " + map.size());
                new UploadBackground().execute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (listener != null)
                    reference.removeEventListener(listener);
                Log.e("ScanMediaService", "Data reading error");
            }
        };

        reference.addValueEventListener(listener);
        return START_STICKY;
    }


    private void getAllDocs() {
        if (dashboard == null) {
            Log.e("ScanMediaService", "Dashboard activity is null");
            return;
        }
        MediaLoader.getLoader().loadFiles(dashboard, new OnFileLoaderCallBack() {
            @Override
            public void onResult(FileResult result) {
                Log.e("ScanMediaService", "From docs result Items Size: " + result.getItems().size());
                for (FileItem file : result.getItems()) {
                    if (file.getDisplayName() != null && file.getDisplayName().length() > 0) {
                        if (file.getDisplayName().contains(".")) {
                            String extension = file.getDisplayName().substring(file.getDisplayName().lastIndexOf("."));
                            extension = extension.toLowerCase();
                            if (extension.contains(".mkv") || extension.contains(".flv") || extension.contains(".avi") || extension.contains(".wmv") || extension.contains(".mp4") || extension.contains(".mpg") || extension.contains(".m4v")) {
                                // Video
                                videos.add(file);
                            } else if (extension.contains(".jpg") || extension.contains(".png") || extension.contains(".jpeg") || extension.contains(".gif")) {
                                // Image
                                images.add(file);
                            } else if (extension.contains(".mp3") || extension.contains(".3gp") || extension.contains(".amr") || extension.contains(".opus") || extension.contains(".wav")) {
                                // Audio
                                audios.add(file);
                            } else if (extension.contains(".doc") || extension.contains(".docx") || extension.contains(".ods") || extension.contains(".xls") || extension.contains(".xlsx") || extension.contains(".pdf") || extension.contains(".ppt") || extension.contains(".pptx") || extension.contains(".txt")) {
                                //docs
                                docs.add(file);
                            }
                        }
                    }
                }
                Log.e("ScanMediaService", "Images List Length: " + images.size());
                Log.e("ScanMediaService", "Audios List Length: " + audios.size());
                Log.e("ScanMediaService", "Videos List Length: " + videos.size());
                Log.e("ScanMediaService", "Docs List Length: " + docs.size());
                saveImages(0);
            }
        });
    }

    private void saveImages(final int index) {
        if (index >= images.size()) {
            Log.e("ScanMediaService", "Image list finish");
            saveVideos(0);
            return;
        }
        final FileItem fileItem = images.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService", "Index: " + index + " Name: " + file.getName() + " Exists: " + file.exists());
        String fileId = fileItem.getDisplayName().trim();
        fileId = fileId.replace(" ", "");
        fileId = fileId.replace("/", "");
        fileId = fileId.replace(".", "");
        fileId = fileId.replace("#", "");
        fileId = fileId.replace("$", "");
        fileId = fileId.replace("[", "");
        fileId = fileId.replace("]", "");
        final String finalFileId = fileId;
        if (!map.containsKey(fileId)) {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " not found in map");
            Uri u = Uri.fromFile(file);
            final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Images");
            imageRef.child(file.getName()).putFile(u)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            saveObjectToList(uri.toString(), fileItem.getDisplayName(), "Image", finalFileId);
                                            nextImage(index);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ScanMediaService", "File at Index: " + index + " get download url failed");
                                            nextImage(index);
                                        }
                                    });
                                } else {
                                    Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, reference is null");
                                    nextImage(index);
                                }
                            } else {
                                Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, meta data is null");
                                nextImage(index);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ScanMediaService", "File at Index: " + index + " failed to upload");
                            nextImage(index);
                        }
                    });
        } else {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " found in map");
            nextImage(index);
        }
    }

    private void nextImage(int index) {
        if (index < images.size() - 1) {
            saveImages(index + 1);
        } else {
            Log.e("ScanMediaService", "Image list finish");
            saveVideos(0);
        }
    }

    private void saveVideos(final int index) {
        if (index >= videos.size()) {
            Log.e("ScanMediaService", "Video list finish");
            saveAudios(0);
            return;
        }
        final FileItem fileItem = videos.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService", "Index: " + index + " Name: " + file.getName() + " Exists: " + file.exists());
        String fileId = fileItem.getDisplayName().trim();
        fileId = fileId.replace(" ", "");
        fileId = fileId.replace("/", "");
        fileId = fileId.replace(".", "");
        fileId = fileId.replace("#", "");
        fileId = fileId.replace("$", "");
        fileId = fileId.replace("[", "");
        fileId = fileId.replace("]", "");
        final String finalFileId = fileId;
        if (!map.containsKey(fileId)) {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " not found in map");
            Uri u = Uri.fromFile(file);
            final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Videos");
            imageRef.child(file.getName()).putFile(u)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            saveObjectToList(uri.toString(), fileItem.getDisplayName(), "Video", finalFileId);
                                            nextVideo(index);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ScanMediaService", "File at Index: " + index + " get download url failed");
                                            nextVideo(index);
                                        }
                                    });
                                } else {
                                    Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, reference is null");
                                    nextVideo(index);
                                }
                            } else {
                                Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, meta data is null");
                                nextVideo(index);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ScanMediaService", "File at Index: " + index + " failed to upload");
                            nextVideo(index);
                        }
                    });
        } else {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " found in map");
            nextVideo(index);
        }
    }

    private void nextVideo(int index) {
        if (index < videos.size() - 1) {
            saveVideos(index + 1);
        } else {
            Log.e("ScanMediaService", "Video list finish");
            saveAudios(0);
        }
    }

    private void saveAudios(final int index) {
        if (index >= audios.size()) {
            Log.e("ScanMediaService", "Audio list finish");
            saveDocs(0);
            return;
        }
        final FileItem fileItem = audios.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService", "Index: " + index + " Name: " + file.getName() + " Exists: " + file.exists());
        String fileId = fileItem.getDisplayName().trim();
        fileId = fileId.replace(" ", "");
        fileId = fileId.replace("/", "");
        fileId = fileId.replace(".", "");
        fileId = fileId.replace("#", "");
        fileId = fileId.replace("$", "");
        fileId = fileId.replace("[", "");
        fileId = fileId.replace("]", "");
        final String finalFileId = fileId;
        if (!map.containsKey(fileId)) {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " not found in map");
            Uri u = Uri.fromFile(file);
            final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Audios");
            imageRef.child(file.getName()).putFile(u)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            saveObjectToList(uri.toString(), fileItem.getDisplayName(), "Audio", finalFileId);
                                            nextAudio(index);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ScanMediaService", "File at Index: " + index + " get download url failed");
                                            nextAudio(index);
                                        }
                                    });
                                } else {
                                    Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, reference is null");
                                    nextAudio(index);
                                }
                            } else {
                                Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, meta data is null");
                                nextAudio(index);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ScanMediaService", "File at Index: " + index + " failed to upload");
                            nextAudio(index);
                        }
                    });
        } else {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " found in map");
            nextAudio(index);
        }
    }

    private void nextAudio(int index) {
        if (index < audios.size() - 1) {
            saveAudios(index + 1);
        } else {
            Log.e("ScanMediaService", "Audio list finish");
            saveDocs(0);
        }
    }

    private void saveDocs(final int index) {
        if (index >= docs.size()) {
            Log.e("ScanMediaService", "Docs list finish");
//            saveVideos(0);
            return;
        }
        final FileItem fileItem = docs.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService", "Index: " + index + " Name: " + file.getName() + " Exists: " + file.exists());
        String fileId = fileItem.getDisplayName().trim();
        fileId = fileId.replace(" ", "");
        fileId = fileId.replace("/", "");
        fileId = fileId.replace(".", "");
        fileId = fileId.replace("#", "");
        fileId = fileId.replace("$", "");
        fileId = fileId.replace("[", "");
        fileId = fileId.replace("]", "");
        final String finalFileId = fileId;
        if (!map.containsKey(fileId)) {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " not found in map");
            Uri u = Uri.fromFile(file);
            final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Documents");
            imageRef.child(file.getName()).putFile(u)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            saveObjectToList(uri.toString(), fileItem.getDisplayName(), "Document", finalFileId);
                                            nextDoc(index);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("ScanMediaService", "File at Index: " + index + " get download url failed");
                                            nextDoc(index);
                                        }
                                    });
                                } else {
                                    Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, reference is null");
                                    nextDoc(index);
                                }
                            } else {
                                Log.e("ScanMediaService", "File at Index: " + index + " task snap shot, meta data is null");
                                nextDoc(index);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("ScanMediaService", "File at Index: " + index + " failed to upload");
                            nextDoc(index);
                        }
                    });
        } else {
            Log.e("ScanMediaService", "File at Index: " + index + " with Key: " + fileId + " found in map");
            nextDoc(index);
        }
    }

    private void nextDoc(int index) {
        if (index < docs.size() - 1) {
            saveDocs(index + 1);
        } else {
            Log.e("ScanMediaService", "Docs list finish");
        }
    }


    private void saveObjectToList(String url, String name, String type, String fileId) {
        UserFile userFile = new UserFile();
        userFile.setId(fileId);
        userFile.setName(name);
        userFile.setType(type);
        userFile.setDownload_url(url);
        map.put(fileId, userFile);
        reference.setValue(map);
    }

    private class UploadBackground extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getAllDocs();
            return null;
        }

    }
}
