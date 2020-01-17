package lcwu.fyp.asistio.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.jiajunhui.xapp.medialoader.MediaLoader;
import com.jiajunhui.xapp.medialoader.bean.AudioItem;
import com.jiajunhui.xapp.medialoader.bean.AudioResult;
import com.jiajunhui.xapp.medialoader.bean.FileItem;
import com.jiajunhui.xapp.medialoader.bean.FileResult;
import com.jiajunhui.xapp.medialoader.bean.PhotoItem;
import com.jiajunhui.xapp.medialoader.bean.PhotoResult;
import com.jiajunhui.xapp.medialoader.bean.VideoItem;
import com.jiajunhui.xapp.medialoader.bean.VideoResult;
import com.jiajunhui.xapp.medialoader.callback.OnAudioLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnPhotoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lcwu.fyp.asistio.activities.Dashboard;

public class ScanMediaService extends Service {

    public static Dashboard dashboard;
    private List<FileItem> images, videos, audios, docs;

    public ScanMediaService() {
        images = videos = audios = docs = new ArrayList<>();
    }

    //    public static void initDashboard(Dashboard d){
//
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//        getAllImages();
//        getAllVideo();
//        getAllAudios();
        getAllDocs();
        return START_STICKY;
    }

//    private void getAllImages(){
//        MediaLoader.getLoader().loadPhotos(dashboard, new OnPhotoLoaderCallBack() {
//            @Override
//            public void onResult(PhotoResult result) {
//                Log.e("SacnMediaService", "Photo Items Size: " +result.getItems().size());
//                for (PhotoItem photo : result.getItems()){
//                    Log.e("SacnMediaService", "Photo Display Name: " + photo.getDisplayName() + " Path: " + photo.getPath());
//                }
//            }
//
//        });
//    }
//
//    private void getAllAudios(){
//        MediaLoader.getLoader().loadAudios(dashboard, new OnAudioLoaderCallBack() {
//            @Override
//            public void onResult(AudioResult result) {
//                Log.e("SacnMediaService", "Audios Items Size: " +result.getItems().size());
//                for (AudioItem audio : result.getItems()){
//                    Log.e("SacnMediaService", "Audio Display Name: " + audio.getDisplayName() + " Path: " + audio.getPath());
//
//                }
//
//            }
//        });
//    }
//
//    private void getAllVideo(){
//        MediaLoader.getLoader().loadVideos(dashboard, new OnVideoLoaderCallBack() {
//            @Override
//            public void onResult(VideoResult result) {
//                Log.e("SacnMediaService", "Videos Items Size: " +result.getItems().size());
//                for(VideoItem video : result.getItems()){
//                    Log.e("SacnMediaService", "Video Display Name: " + video.getDisplayName() + " Path: " + video.getPath());
//                }
//            }
//        });
//    }



    private void getAllDocs(){
        MediaLoader.getLoader().loadFiles(dashboard, new OnFileLoaderCallBack() {
            @Override
            public void onResult(FileResult result) {
                Log.e("SacnMediaService", "From docs result Items Size: " +result.getItems().size());
                for(FileItem file : result.getItems()){
                    if(file.getDisplayName() != null && file.getDisplayName().length() > 0){
                        if(file.getDisplayName().contains(".")){
                            String extension = file.getDisplayName().substring(file.getDisplayName().lastIndexOf("."));
                            Log.e("SacnMediaService", "Extension: " + extension + " Docs Name: " + file.getDisplayName() + " Path: " + file.getPath());
                            extension = extension.toLowerCase();
                            if (extension.contains("jpg") || extension.contains("png") || extension.contains("jpeg") || extension.contains("gif")){
                                // Image
                                images.add(file);
                            }
                            else if (extension.contains("mp3") || extension.contains(".3gp") || extension.contains(".amr") || extension.contains(".opus") || extension.contains(".wav")){
                                // Audio
                                audios.add(file);
                            }
                            else if(extension.contains(".mkv") || extension.contains(".flv") || extension.contains(".avi") || extension.contains(".wmv") || extension.contains("mp4") || extension.contains(".mpg") || extension.contains(".m4v")){
                                //Video
                                videos.add(file);
                            }
                            else if (extension.contains(".doc") || extension.contains(".docx") || extension.contains(".htm") || extension.contains(".html") || extension.contains(".odt") || extension.contains(".ods") || extension.contains(".xls") || extension.contains(".xlsx") || extension.contains(".pdf") || extension.contains(".ppt") || extension.contains(".pptx") || extension.contains(".txt")){
                                docs.add(file);
                                //docs
                            }
                        }
                    }
                }
                System.out.println("Completed Result : Images lenght"+images.size()+"Images : "+images);
                System.out.println("Audios : length : "+audios.size()+ "Audios " +audios);
                System.out.println("Videos : length : "+videos.size()+"Videos " + videos);
                System.out.println(" Docs : length  : "+docs.size()+" docs "+docs);

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

}
