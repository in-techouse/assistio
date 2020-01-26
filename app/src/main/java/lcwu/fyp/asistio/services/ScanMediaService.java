package lcwu.fyp.asistio.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jiajunhui.xapp.medialoader.MediaLoader;
import com.jiajunhui.xapp.medialoader.bean.FileItem;
import com.jiajunhui.xapp.medialoader.bean.FileResult;
import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.activities.Dashboard;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.User;
import lcwu.fyp.asistio.model.UserFile;

public class ScanMediaService extends Service {
    private DatabaseReference reference;
    public static Dashboard dashboard;
    private List<FileItem> images, videos, audios, docs;
    private User user;
    private List<UserFile> userFiles;

    public ScanMediaService() {
        videos = new ArrayList<>();
        audios = new ArrayList<>();
        docs  = new ArrayList<>();
        images = new ArrayList<>();
        userFiles = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Session session = new Session(this);
        user = session.getUser();
        reference  = FirebaseDatabase.getInstance().getReference().child("UserFiles").child(user.getId());
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        getAllDocs();
        return START_STICKY;
    }


    private void getAllDocs(){
        MediaLoader.getLoader().loadFiles(dashboard, new OnFileLoaderCallBack() {
            @Override
            public void onResult(FileResult result) {
                Log.e("ScanMediaService", "From docs result Items Size: " +result.getItems().size());
                for(FileItem file : result.getItems()){
                    if(file.getDisplayName() != null && file.getDisplayName().length() > 0){
                        if(file.getDisplayName().contains(".")){
                            String extension = file.getDisplayName().substring(file.getDisplayName().lastIndexOf("."));
                            Log.e("ScanMediaService", "Extension: " + extension + " Doc Name: " + file.getDisplayName() + " Path: " + file.getPath());
                            extension = extension.toLowerCase();
                            if(extension.contains(".mkv") || extension.contains(".flv") || extension.contains(".avi") || extension.contains(".wmv") || extension.contains(".mp4") || extension.contains(".mpg") || extension.contains(".m4v")){
                                //Video
                                Log.e("Video extentions" , "Video extension is  : "+extension);
                                videos.add(file);
                            }
                            else if (extension.contains(".jpg") || extension.contains(".png") || extension.contains(".jpeg") || extension.contains(".gif")){
                                // Image
                                System.out.println("hehe");
                                Log.e("Image extentions" , "Image extension is  : "+extension);
                                images.add(file);
                            }
                            else if (extension.contains(".mp3") || extension.contains(".3gp") || extension.contains(".amr") || extension.contains(".opus") || extension.contains(".wav")){
                                // Audio
                                audios.add(file);
                            }
                            else if (extension.contains(".doc") || extension.contains(".docx") || extension.contains(".ods") || extension.contains(".xls") || extension.contains(".xlsx") || extension.contains(".pdf") || extension.contains(".ppt") || extension.contains(".pptx") || extension.contains(".txt")){
                                docs.add(file);
                                //docs
                            }
                        }
                    }
                }
                Log.e("ScanMediaService",  "Images List Length: " + images.size());
                Log.e("ScanMediaService",  "Audios List Length: " + audios.size());
                Log.e("ScanMediaService",  "Videos List Length: " + videos.size());
                for (FileItem fileItem : videos){
                    Log.e("ScanMediaService",  "ScanMediaService Video Name: " + fileItem.getDisplayName());
                }
                for (FileItem fileItem : images){
                    Log.e("ScanMediaService",  "ScanMediaService Image Name: " + fileItem.getDisplayName());
                }


                new UploadBackground().execute();
            }
        });
    }
    public void notifyMe(){
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(getApplicationContext(), "1");
        mBuilder.setAutoCancel(false);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setTicker("Asissito");
        mBuilder.setContentInfo("Syncing Data");
        mBuilder.setSmallIcon(R.drawable.home);
        mBuilder.setOngoing(true);
        mBuilder.setProgress(0, 0, true);
        mBuilder.setContentTitle("Syncing Data");
        mBuilder.setContentText("Keep Patience");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(2, mBuilder.build());
        }
    }

    public void notifyAfterSync(){
        NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(getApplicationContext(), "1");
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setTicker("Asissito");
        mBuilder.setContentInfo("Data Synced");
        mBuilder.setSmallIcon(R.drawable.home);
        mBuilder.setContentTitle("Data Synced");
        mBuilder.setContentText("Your data is synced with Asisstio.");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(2, mBuilder.build());
        }
    }

    public void saveDocs(){
        List<FileItem> fileItems = new ArrayList<>();
        fileItems.add(docs.get(6));
        fileItems.add(docs.get(7));
        fileItems.add(docs.get(8));
        Log.e("Docs" , "Doc name"+fileItems);
        for(final FileItem f : fileItems){
            File file = new File(f.getPath());
            final Uri u = Uri.fromFile(file);

            //Data Insertion
            final StorageReference docRef = FirebaseStorage.getInstance().getReference().child(user.getId()).child("docs");
            docRef.child(file.getName()).putFile(u)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //To get Download URI
                        Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                saveObjectToList(url, f.getDisplayName(), "Document");
//                                Log.e("msg" , "doc URL is "+url);
                            }
                        });
                        Toast.makeText(getApplicationContext(), "Docs Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Docs Failed", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getApplicationContext(), "Docs being Uploaded...", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

//    public void saveAudios(){
//        List<FileItem> fileItems = new ArrayList<>();
//        fileItems.add(audios.get(0));
//        fileItems.add(audios.get(1));
//        fileItems.add(audios.get(2));
//        for (final FileItem f : fileItems){
//            File file = new File(f.getPath());
//            Uri u = Uri.fromFile(file);
//
//            //Data Insertion
//            final StorageReference audioRef = FirebaseStorage.getInstance().getReference().child(user.getId()).child("audios");
//            audioRef.child(file.getName()).putFile(u)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //To get Download URI
//                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
//                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String url = uri.toString();
//                                    saveObjectToList(url, f.getDisplayName(), "Audios");
////                                    Log.e("msg" , "Audio URL is "+url);
//                                }
//                            });
//                            Toast.makeText(getApplicationContext(), "Audios Uploaded", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getApplicationContext(), "Audios Underway", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(), "Audios Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        }
//
//    }

//    public void saveVideos(){
//
////        for(FileItem f : videos){
////            Log.e("Video data" , "Video data: "+f.getDisplayName());
////        }
//        List<FileItem> fileItems = new ArrayList<>();
//        fileItems.add(videos.get(0));
//        fileItems.add(videos.get(1));
//        fileItems.add(videos.get(2));
//        for (final FileItem f : fileItems){
//            File file = new File(f.getPath());
//            Uri u = Uri.fromFile(file);
//
//            //data Insertion
//            final  StorageReference videoRef = FirebaseStorage.getInstance().getReference().child(user.getId()).child("videos");
//            videoRef.child(file.getName()).putFile(u)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            //To get Download URI
//                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
//                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String url = uri.toString();
//                                    saveObjectToList(url, f.getDisplayName(), "Videos");
////                                    Log.e("msg" , "Video URL is "+url);
//                                }
//                            });
//                            Toast.makeText(getApplicationContext(), "Videos Uploaded", Toast.LENGTH_SHORT).show();
//
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
////                            Toast.makeText(getApplicationContext(), "Videos Getting Uploaded", Toast.LENGTH_SHORT).show();
//
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getApplicationContext(), "Videos Uploaded Failed", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//
//    }
//

    public void saveDocuments(final int index){
        final FileItem fileItem = docs.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService" , "Name: "+file.getName() + " Exists : "+file.exists());
        Uri u = Uri.fromFile(file);
        final StorageReference docRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Documents");
        docRef.child(file.getName()).putFile(u)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getMetadata()!=null){
                            if(taskSnapshot.getMetadata().getReference()!=null){
                                taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                saveObjectToList(uri.toString() , fileItem.getDisplayName() , "Documents");
                                                if(index!=2){
                                                    saveDocuments(index+1);
                                                }else{
                                                    Log.e("pos" , "Documents Completed");
                                                    //Save URLs
                                                    saveURLs();
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if(index!=2){
                                                    saveDocuments(index+1);
                                                }
                                                else{
                                                    //
                                                    //Save URLs
                                                    saveURLs();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(index != 2){
                            saveDocuments(index+1);
                        }
                        else{
                            //
                            //Save URLs
                            saveURLs();
                        }

                    }
                });
    }

    public void saveAudios(final int index){
        final FileItem fileItem = audios.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService" , "Name: "+file.getName() + " Exists : "+file.exists());
        Uri u = Uri.fromFile(file);
        final StorageReference audioRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Audios");
        audioRef.child(file.getName()).putFile(u)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getMetadata()!=null){
                            if(taskSnapshot.getMetadata().getReference()!=null){
                                taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                saveObjectToList(uri.toString() , fileItem.getDisplayName() , "Audios");
                                                if(index!=2){
                                                    saveAudios(index+1);
                                                }
                                                else{
                                                    //going to save URLs
                                                    saveURLs();
                                                    Log.e("pos" , "Audios Completed");
                                                    //Start Uploading Documents
//                                                    saveDocuments(0);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if(index!=2){
                                                    saveAudios(index+1);
                                                }
                                                else{
                                                    //to be updated
                                                    saveURLs();
                                                    Log.e("pos" , "Audios Completed");
                                                    //Start Uploading Documents
//                                                    saveDocuments(0);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(index != 2){
                            saveAudios(index+1);
                        }
                        else{
                            //to be updated
                            saveURLs();
                            Log.e("pos" , "Audios Completed");
                            //Start Uploading Documents
//                            saveDocuments(0);
                        }
                    }
                });
    }

    public void saveVideos(final int index){
        Log.e("pos" , "in saveVideos");
        final FileItem fileItem = videos.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService", "Name: " + file.getName() + " Exists: " + file.exists());
        Uri u = Uri.fromFile(file);
        final StorageReference videoRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Videos");
        videoRef.child(file.getName()).putFile(u)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getMetadata() != null){
                            if(taskSnapshot.getMetadata().getReference() != null){
                                taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                saveObjectToList(uri.toString() , fileItem.getDisplayName() , "Videos");
                                                if(index!=2){
                                                    saveVideos(index+1);
                                                    Log.e("pos" , "Uploading "+index);
                                                }
                                                else {
                                                    Log.e("pos" , "goint to saveURLs");
//                                                    saveURLs();
                                                    //Start Uploading Audios
                                                    Log.e("pos" , "goint to auidos");
                                                    saveAudios(0);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                if(index != 2){
                                                    saveVideos(index+1);
                                                }
                                                else{
                                                    //
                                                    Log.e("pos" , "goint to auidos");
                                                    saveAudios(0);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(index != 2){
                            saveVideos(index+1);
                        }
                        else{
                            //
                            Log.e("pos" , "goint to auidos");
                            saveAudios(0);
                        }

                    }
                });
    }

    public void saveImages(final int index){
//        Toast.makeText(this, "Images are being Uploaded", Toast.LENGTH_SHORT).show();
        final FileItem fileItem = images.get(index);
        final File file = new File(fileItem.getPath());
        Log.e("ScanMediaService", "Name: " + file.getName() + " Exists: " + file.exists());
        Uri u = Uri.fromFile(file);
        final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId()).child("Images");
        imageRef.child(file.getName()).putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if(taskSnapshot.getMetadata() != null){
                    if(taskSnapshot.getMetadata().getReference() != null){
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveObjectToList(uri.toString(), fileItem.getDisplayName(), "Image");
                                if(index < 50){
                                    saveImages(index+1);
                                }
                                else{
                                    // Start Uploading Videos
//                                    saveURLs();
                                    saveVideos(0);
//                                    Log.e("UserFile", "User Files List Size: " + userFiles.size());
//                                    for(UserFile userFile : userFiles){
//                                        Log.e("UserFile", "Name: " + userFile.getName() + " URl: " + userFile.getDownload_url());
//                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if(index < 50){
                                    saveImages(index+1);
                                }
                                else{
                                    saveVideos(0);
//                                    Log.e("UserFile", "User Files List Size: " + userFiles.size());
//                                    for(UserFile userFile : userFiles){
//                                        Log.e("UserFile", "Name: " + userFile.getName() + " URl: " + userFile.getDownload_url());
//                                    }
                                }
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(index < 50){
                    saveImages(index+1);
                }
                else{
                    saveVideos(0);
//                    Log.e("UserFile", "User Files List Size: " + userFiles.size());
//                    for(UserFile userFile : userFiles){
//                        Log.e("UserFile", "Name: " + userFile.getName() + " URl: " + userFile.getDownload_url());
//                    }
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("in onBind");
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        System.out.println("in onDestroy");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    private void saveObjectToList(String url, String name, String type){
        String id = reference.push().getKey();
        UserFile userFile = new UserFile();
        userFile.setId(id);
        userFile.setDownload_url(url);
        userFile.setName(name);
        userFile.setType(type);
        userFiles.add(userFile);
        Log.e("Save Object" , "UserFile name: "+userFile.getName());
        Log.e("Save Object" , "UserFile URL: "+userFile.getDownload_url());
        Log.e("Save Object" , "UserFile type: "+userFile.getType());
    }

    private void saveURLs(){
        Log.e("url" , "in Save URL");
//        final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(user.getId()).child("images");
//        DatabaseReference urlRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId()).child("URLs");
        reference.setValue(userFiles)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        notifyAfterSync();
//                        ListUserFile listUserFile = ListUserFile.getInstance();
//                        Log.e("ListUserFile", "Size of List: " + listUserFile.getUserFiles().size());
//                        listUserFile.setUserFiles(userFiles);
//                        Log.e("ListUserFile", "Size of List: " + listUserFile.getUserFiles().size());
//                        session.setFiles(listUserFile);
//                        ListUserFile file = session.getUserFiles();
//                        List<UserFile> files = file.getUserFiles();
//                        Log.e("session" , "Going to loop"+session.getUserFiles().getUserFiles().size());
//                        for (UserFile filess: files ) {
//                            Log.e("session" , "in Session : " + filess.getName());
//                        }
//                        Log.e("session" , "in Session : " + session.getUserFiles());
//                        Toast.makeText(getApplicationContext(), "Links Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("URL" , "URLs failed: ");
//                        Toast.makeText(getApplicationContext(), "Links Saving Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class UploadBackground extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  Show Notification to user
            notifyMe();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            saveImages(0);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  Remove previous Notification to user
            // Show new notification of all done
        }
    }

}
