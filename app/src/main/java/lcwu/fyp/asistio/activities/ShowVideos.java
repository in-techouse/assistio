package lcwu.fyp.asistio.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.annotation.GlideModule;
//import com.bumptech.glide.module.AppGlideModule;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ShowVideosAdapter;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowVideos extends AppCompatActivity {



    List<UserFile> userFiles = new ArrayList<>();
    List<UserFile> userVideos = new ArrayList<>();
    ArrayList<String> videos = new ArrayList<>();

    //Toro Work
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videos);
//        container = findViewById(R.id.my_fancy_videos);
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

        GridView gridView = findViewById(R.id.gridview);
        ShowVideosAdapter adapter = new ShowVideosAdapter(getApplicationContext() , userVideos);
        gridView.setAdapter(adapter);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.acitvity_show_videos, menu);
        return true;
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
