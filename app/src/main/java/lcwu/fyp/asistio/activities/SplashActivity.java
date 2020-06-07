package lcwu.fyp.asistio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.appus.splash.Splash;

import lcwu.fyp.asistio.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        Splash.Builder splash = new Splash.Builder(this, getSupportActionBar());
        splash.setBackgroundImage(getResources().getDrawable(R.drawable.splashscreen));
        splash.setSplashImage(getResources().getDrawable(R.drawable.logo));
        splash.perform();


        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                // Old Code
                Intent it = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(it);
                finish();
                // New Code
//                Session session = new Session(SplashActivity.this);
//                if (session.getUser() != null) {
//                    Intent it = new Intent(SplashActivity.this, Dashboard.class);
//                    startActivity(it);
//                    finish();
//                } else {
//                    Intent it = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(it);
//                    finish();
//                }
            }
        }.start();

    }
}
