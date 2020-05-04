package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import lcwu.fyp.asistio.R;

public class AutoReply extends AppCompatActivity implements View.OnClickListener{

    private EditText message;
    private EditText reply_message;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reply);

        message= findViewById(R.id.message);
        save = findViewById(R.id.save);
        reply_message = findViewById(R.id.reply_message);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
