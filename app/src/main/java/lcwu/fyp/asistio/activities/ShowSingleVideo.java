package lcwu.fyp.asistio.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.khizar1556.mkvideoplayer.MKPlayer;
import java.util.ArrayList;
import java.util.List;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowSingleVideo extends AppCompatActivity {


//    List<UserFile> userFiles = new ArrayList<>();
    private List<UserFile> userVideos = new ArrayList<>();
//    ArrayList<String> videos = new ArrayList<>();
    private ListUserFile listUserFile;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_video);

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

//        UserFile userFile = (UserFile) extras.getSerializable("selectedVideo");
        listUserFile = (ListUserFile) extras.getSerializable("files");
//        int i  = (Integer) extras.getSerializable("index");
//        Log.e("intent", "received in new : " + userFile);



        if (listUserFile == null) {
            finish();
            return;
        }
        if(listUserFile.getUserFiles() == null){
            finish();
            return;
        }

        index = extras.getInt("index", 0);

        Log.e("intent", "files received in new : " + listUserFile);
        Log.e("intent", "received in new : " + index);
        userVideos = listUserFile.getUserFiles();
//        Log.e("intent", "received in new : " + userFile.getId());

        MKPlayer mkplayer = new  MKPlayer(ShowSingleVideo.this);
//        Log.e("intent", "received in new : " + userFile.getDownload_url());
        mkplayer.play(userVideos.get(index).getDownload_url());
//
        mkplayer.setPlayerCallbacks(new MKPlayer.playerCallbacks() {
            @Override
            public void onNextClick() {
                //It is the method for next song.It is called when you pressed the next icon
                //Do according to your requirement
                if(index < userVideos.size()){
                    index++;
                    mkplayer.play(userVideos.get(index).getDownload_url());
                }
            }
            @Override
            public void onPreviousClick() {
                //It is the method for previous song.It is called when you pressed the previous icon
                //Do according to your requirement
                if(index > 0){
                    index--;
                    mkplayer.play(userVideos.get(index).getDownload_url());
                }

            }
        });

        // Once it's prepared, the progress indicator goes away and the controls become enabled for the user to begin playback.
    }



    @Override
    public void onBackPressed() {
        finish();
        return;
    }

}
