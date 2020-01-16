package lcwu.fyp.asistio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.appus.splash.Splash;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Session;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Splash.Builder splash = new Splash.Builder(this, getSupportActionBar());
        splash.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//        splash.setBackgroundImage(getResources().getDrawable(R.drawable.ic_launcher_background));
        splash.setSplashImage(getResources().getDrawable(R.drawable.logo));
//        splash.setAnimationType(Splash.AnimationType.TYPE_2);
        splash.perform();

        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                // Old Code
//                Intent it = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(it);
//                finish();
                // New Code
                Session session = new Session(SplashActivity.this);
                if(session.getUser() != null){
                    Intent it = new Intent(SplashActivity.this, Dashboard.class);
                    startActivity(it);
                    finish();
                }
                else{
                    Intent it = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        }.start();

    }
}
