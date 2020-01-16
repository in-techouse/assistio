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

import lcwu.fyp.asistio.activities.Dashboard;

public class ScanMediaService extends Service {

    public static Dashboard dashboard;

//    public static void initDashboard(Dashboard d){
//
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        getAllImages();
        getAllVideo();
        getAllAudios();
        getAllDocs();
        return START_STICKY;
    }

    private void getAllImages(){
        MediaLoader.getLoader().loadPhotos(dashboard, new OnPhotoLoaderCallBack() {
            @Override
            public void onResult(PhotoResult result) {
                Log.e("Service", "Photo Items Size: " +result.getItems().size());
                for (PhotoItem photo : result.getItems()){
                    Log.e("Service", "Photo Display Name: " + photo.getDisplayName() + " Path: " + photo.getPath());
                }
            }

        });
    }

    private void getAllAudios(){
        MediaLoader.getLoader().loadAudios(dashboard, new OnAudioLoaderCallBack() {
            @Override
            public void onResult(AudioResult result) {
                Log.e("Service", "Audios Items Size: " +result.getItems().size());
                for (AudioItem audio : result.getItems()){
                    Log.e("Service", "Audio Display Name: " + audio.getDisplayName() + " Path: " + audio.getPath());

                }

            }
        });
    }

    private void getAllVideo(){
        MediaLoader.getLoader().loadVideos(dashboard, new OnVideoLoaderCallBack() {
            @Override
            public void onResult(VideoResult result) {
                Log.e("Service", "Videos Items Size: " +result.getItems().size());
                for(VideoItem video : result.getItems()){
                    Log.e("Service", "Video Display Name: " + video.getDisplayName() + " Path: " + video.getPath());
                }
            }
        });
    }



    private void getAllDocs(){
        MediaLoader.getLoader().loadFiles(dashboard, new OnFileLoaderCallBack() {
            @Override
            public void onResult(FileResult result) {
                Log.e("Service", "From docs result Items Size: " +result.getItems().size());
                for(FileItem file : result.getItems()){
                    Log.e("Service", "Docs Name: " + file.getDisplayName() + " Path: " + file.getPath());

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

    private void showLogs(){
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Log.e("Service", "Hello I'm from Service");
                showLogs();
            }
        }.start();
    }
}
