package lcwu.fyp.asistio.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ShowAudioAdaptor;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowAudios extends AppCompatActivity {

    List<UserFile> userFiles = new ArrayList<>();
    List<String> audios = new ArrayList<>();
    List<UserFile> userAudios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_audios);


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
        if(userFile == null){
            finish();
            return;
        }
        userFiles = userFile.getUserFiles();
        Log.e("Audios" , "received : "+ userFiles.size());
        for ( UserFile file: userFiles) {
            Log.e("Audios" , "received : "+file.getName());
            if(file.getType().equals("Audios")){
                audios.add(file.getDownload_url());
                userAudios.add(file);
                Log.e("Audios" , "in Audios : " + audios);
            }
        }
        Log.e("Audios" , "in Audios : "+audios.size());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        ShowAudioAdaptor mAdapter = new ShowAudioAdaptor(userAudios , getApplicationContext());
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
