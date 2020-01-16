package lcwu.fyp.asistio.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class UploadService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        showLogs();
        return START_STICKY;
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
