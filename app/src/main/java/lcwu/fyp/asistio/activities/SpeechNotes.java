package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import lcwu.fyp.asistio.R;

public class SpeechNotes extends AppCompatActivity implements View.OnClickListener {

    private EditText notes;
    private ImageView record_ur_voice;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_notes);

        notes = findViewById(R.id.notes);
        record_ur_voice= findViewById(R.id.record_ur_voice);
        save = findViewById(R.id.save);
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
