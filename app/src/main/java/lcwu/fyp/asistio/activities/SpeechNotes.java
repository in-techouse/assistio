package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import lcwu.fyp.asistio.R;

public class SpeechNotes extends AppCompatActivity implements View.OnClickListener {

    private Button save, record;
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_notes);

        record = findViewById(R.id.record);
        save = findViewById(R.id.save);
        txt = findViewById(R.id.txt);

        save.setOnClickListener(this);
        record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.record: {
                break;
            }
            case R.id.save: {
                break;
            }
        }
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
}
