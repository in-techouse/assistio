package lcwu.fyp.asistio.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media2.exoplayer.external.upstream.LoaderErrorThrower;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.utils.Utils;

import java.io.File;

import lcwu.fyp.asistio.R;


public class DownloadService extends Service {

    private String url , name =" " , type;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        PRDownloader.initialize(getApplicationContext());
//          this getter is just for example purpose, can differ
        if (intent !=null && intent.getExtras()!=null){
            url = intent.getExtras().getString("DocURL");
            name = intent.getExtras().getString("DocName");
            type = intent.getExtras().getString("DocType");
        }
        Log.e("Service" , "am here"+name);
        Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();
        Log.e("download" , "before if with type "+type);
        if(type.equals("Audios")){
            Log.e("download" , "in Audios if");
            File dir = new File(Environment.getExternalStorageDirectory() + "/Asistio/Audios/");
            dir.mkdirs();
            downloader(name , url , dir.toString());

        }
        else if(type.equals("Documents")){

             File dir = new File(Environment.getExternalStorageDirectory() + "/Asistio/Documents/");
             dir.mkdirs();
             downloader(name , url , dir.toString());
         }
        else if(type.equals("Image")){
        }
        else if(type.equals("Videos") ){
            File dir = new File(Environment.getExternalStorageDirectory() + "/Asistio/Videos/");
            dir.mkdirs();
            downloader(name , url , dir.toString());
        }



        return flags;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Service" , "am here2");


//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                newString= null;
//            } else {
//                url = extras.getString("DocURL");
//                name = extras.getString("DocName");
//            }
//
//            url = (String) savedInstanceState.getSerializable("DocURL");
//            name = (String) savedInstanceState.getSerializable("DocName");
//            Log.e("Service" , "inService : "+url);
        return null;
    }

    void downloader(String name , String url ,  String dir){
        Log.e("download" , "Param Values"+name+" "+url);

        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

// Setting timeout globally for the download network requests:
        PRDownloaderConfig configer = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), configer);





        int downloadId = PRDownloader.download(url, dir, name)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.e("download" , "in Start");
                        notifyMe(name , true);

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.e("download" , "completed");
                        notifyMe(name , false);

                    }

                    @Override
                    public void onError(Error error) {
                        Log.e("download" , "error "+error.toString());
                    }
                });

    }

    public void notifyMe(String name , boolean toDisplay){


        if(toDisplay == false){
            NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(getApplicationContext(), "1");
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
            mBuilder.setTicker("Asistio");
            mBuilder.setSmallIcon(R.drawable.home);
            mBuilder.setOngoing(false);
            mBuilder.setProgress(100, 100, false);
            mBuilder.setContentTitle("Assistio");
            mBuilder.setContentText(name+"  downloaded");
            mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(1, mBuilder.build());
            }
        }
        else {

        }

    }
}
