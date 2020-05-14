package lcwu.fyp.asistio.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import java.io.File;

import lcwu.fyp.asistio.R;


public class DownloadService extends Service {

    private String url, name = " ", type;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        PRDownloader.initialize(getApplicationContext());
        if (intent == null)
            return flags;

        if (intent.getExtras() == null)
            return flags;

        url = intent.getExtras().getString("DocURL");
        name = intent.getExtras().getString("DocName");
        type = intent.getExtras().getString("DocType");
        Log.e("Download", "Url: " + url);
        Log.e("Download", "name: " + name);
        Log.e("Download", "Type: " + type);
        Toast.makeText(this, "Downloading " + name.toUpperCase(), Toast.LENGTH_SHORT).show();
        Log.e("Download", "before if with type " + type);
        switch (type) {
            case "Audio": {
                Log.e("Download", "in Audios if");
                File dir = new File(Environment.getExternalStorageDirectory() + "/Asistio/Audios/");
                dir.mkdirs();
                downloader(name, url, dir.toString());

                break;
            }
            case "Document": {
                File dir = new File(Environment.getExternalStorageDirectory() + "/Asistio/Documents/");
                dir.mkdirs();
                downloader(name, url, dir.toString());
                break;
            }
            case "Image":
                break;
            case "Video": {
                File dir = new File(Environment.getExternalStorageDirectory() + "/Asistio/Videos/");
                dir.mkdirs();
                downloader(name, url, dir.toString());
                break;
            }
        }
        return flags;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("Download", "onBind Called");
        return null;
    }

    void downloader(String name, String url, String dir) {
        Log.e("Download", "Param Values" + name + " " + url);

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
                        Log.e("Download", "onStartOrResume");
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
                        Log.e("Download", "completed");
                        notifyMe(name);

                    }

                    @Override
                    public void onError(Error error) {
                        Log.e("Download", "error " + error.toString());
                    }
                });

    }

    public void notifyMe(String name) {
        Log.e("Download", "Notify me called");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1");
        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        mBuilder.setTicker("Asistio");
        mBuilder.setSmallIcon(R.drawable.home);
        mBuilder.setContentTitle("Assistio");
        mBuilder.setContentText(name + "  downloaded successfully.");
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, mBuilder.build());
        }

    }
}
