package lcwu.fyp.asistio.activities;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import lcwu.fyp.asistio.R;

public class MobileModel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_model);

        TextView brand = findViewById(R.id.brand);
        TextView model = findViewById(R.id.model);
        TextView serialNumber = findViewById(R.id.serialNumber);
        TextView androidSdk = findViewById(R.id.androidSdk);
        TextView androidVersionCode = findViewById(R.id.androidVersionCode);

        brand.setText(Build.BRAND);
        model.setText(Build.MODEL);
        serialNumber.setText(Build.SERIAL);
        androidSdk.setText(Build.VERSION.SDK_INT + "");
        androidVersionCode.setText(Build.VERSION.RELEASE);
    }
}
