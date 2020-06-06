package lcwu.fyp.asistio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ShowAudioAdaptor;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowAudios extends AppCompatActivity {

    Toolbar audioToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_audios);

        audioToolbar=findViewById(R.id.audios_toolbar);

        setSupportActionBar(audioToolbar);
        getSupportActionBar().setTitle("Audios");

        Intent it = getIntent();
        if (it == null) {
            finish();
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        ListUserFile userFile = (ListUserFile) extras.getSerializable("files");
        if (userFile == null) {
            finish();
            return;
        }

        List<UserFile> userAudios = userFile.getUserFiles();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        ShowAudioAdaptor mAdapter = new ShowAudioAdaptor(userAudios, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowAudios.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
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
