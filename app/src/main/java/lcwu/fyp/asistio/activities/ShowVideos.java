package lcwu.fyp.asistio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ShowVideosAdapter;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowVideos extends AppCompatActivity {

    Toolbar videos_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videos);

        videos_toolbar=findViewById(R.id.videos_toolbar);

        setSupportActionBar(videos_toolbar);
        getSupportActionBar().setTitle("Videos");

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
        Log.e("intent", "received : " + userFile);
        if (userFile == null) {
            finish();
            return;
        }
        List<UserFile> userVideos = userFile.getUserFiles();

        GridView gridView = findViewById(R.id.gridview);
        ShowVideosAdapter adapter = new ShowVideosAdapter(getApplicationContext(), userVideos);
        gridView.setAdapter(adapter);
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
