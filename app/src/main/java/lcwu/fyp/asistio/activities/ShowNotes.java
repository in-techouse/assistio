package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import lcwu.fyp.asistio.R;

public class ShowNotes extends AppCompatActivity {

    Toolbar notes_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        notes_toolbar = findViewById(R.id.notes_toolbar);


        setSupportActionBar(notes_toolbar);
        getSupportActionBar().setTitle("Notes");


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
