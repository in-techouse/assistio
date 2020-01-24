package lcwu.fyp.asistio.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.VideoView;

import com.google.firebase.database.core.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;

import im.ene.toro.PlayerSelector;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ShowVideosAdapter;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowVideos extends AppCompatActivity {


    //Customm Work
    // Array of strings storing country names
    String[] countries = new String[] {
            "India",
            "Pakistan",
            "Sri Lanka",
            "China",
            "Bangladesh",
            "Nepal",
            "Afghanistan",
            "North Korea",
            "South Korea",
            "Japan"
    };

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.drawable.common_google_signin_btn_icon_dark,
            R.drawable.common_google_signin_btn_icon_dark_focused,
            R.drawable.common_google_signin_btn_icon_disabled,
            R.drawable.common_google_signin_btn_text_dark_focused,
            R.drawable.common_full_open_on_phone,
            R.drawable.clock,
            R.drawable.calendar,
            R.drawable.auto,
            R.drawable.cloud,
            R.drawable.common_google_signin_btn_text_dark
    };


    List<UserFile> userFiles = new ArrayList<>();
    List<UserFile> userVideos = new ArrayList<>();
    ArrayList<String> videos = new ArrayList<>();
//    static int LAYOUT_RES = R.layout.vh_exoplayer_basic;;
    Container container;
    Context context;
    private Object MediaPlayer;

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
//                ButterKnife.bind(this, videos);
            }
        }
        Log.e("vidoes" , "Going to videos");

        GridView gridView = findViewById(R.id.gridview);
        ShowVideosAdapter adapter = new ShowVideosAdapter(getApplicationContext() , userVideos);
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
        gridView.setAdapter(adapter);


        // Each row in the list stores country name, currency and flag
//        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
//
//        for(int i=0;i<10;i++){
//            HashMap<String, String> hm = new HashMap<String,String>();
//            hm.put("txt", countries[i]);
//            hm.put("flag", Integer.toString(flags[i]) );
//            aList.add(hm);
//        }
//
//
//        // Keys used in Hashmap
//        String[] from = { "flag","txt"};
//
//        // Ids of views in listview_layout
//        int[] to = { R.id.flag,R.id.txt};
//
//        // Instantiating an adapter to store each items
//        // R.layout.listview_layout defines the layout of each item
//        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.gridview_layout, from, to);
//
//        // Getting a reference to gridview of MainActivity
//        GridView gridView =  findViewById(R.id.gridview);
//
//        // Setting an adapter containing images to the gridview
//        gridView.setAdapter(adapter);

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
