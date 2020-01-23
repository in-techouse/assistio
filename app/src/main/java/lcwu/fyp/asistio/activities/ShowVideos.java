package lcwu.fyp.asistio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowVideos extends AppCompatActivity {

    List<UserFile> userFiles = new ArrayList<>();
    List<UserFile> userVideos = new ArrayList<>();
    ArrayList<String> videos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videos);

        Intent it = getIntent();
        if (it == null){
            finish();
            return;
        }

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            finish();
            return;
        }

        ListUserFile userFile = (ListUserFile) extras.getSerializable("files");
        Log.e("intent" , "received : "+userFile);
        if(userFile == null){
            finish();
            return;
        }
        userFiles = userFile.getUserFiles();
        Log.e("intent" , "received : "+userFile);
        for ( UserFile file: userFiles) {
            Log.e("intent" , "received : "+file.getName());
            if(file.getType().equals("Videos")){
                videos.add(file.getDownload_url());
                userVideos.add(file);
                Log.e("intent" , "inVedios : "+videos);
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
