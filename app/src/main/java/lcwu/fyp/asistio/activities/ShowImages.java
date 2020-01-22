package lcwu.fyp.asistio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowImages extends AppCompatActivity {

     ArrayList<UserFile> userFile = new ArrayList<UserFile>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            ListUserFile userFile = (ListUserFile) extras.getSerializable("files");
            Log.e("intent" , "recievec : "+userFile);
        }
//        Intent i = getExtra();
//            Bundle bundle = getEx
//        userFile = (ArrayList<UserFile>) getIntent().getSerializableExtra("UserFile");
//        userFile = (ArrayList<UserFile>) i.getSerializableExtra("UserFile");
//        Log.e("showImages" , "inshowImages : "+userFile);

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
