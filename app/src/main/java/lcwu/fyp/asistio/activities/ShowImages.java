package lcwu.fyp.asistio.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.firebase.database.core.view.View;
import com.mzelzoghbi.zgallery.ZGallery;
import com.mzelzoghbi.zgallery.ZGrid;
import com.mzelzoghbi.zgallery.entities.ZColor;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowImages extends AppCompatActivity {

     List<UserFile> userFiles = new ArrayList<>();
     List<UserFile> userImages = new ArrayList<>();
     ArrayList<String> images = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);

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
        Log.e("intent" , "recievec : "+userFile);
        if(userFile == null){
            finish();
            return;
        }
        userFiles = userFile.getUserFiles();
        Log.e("intent" , "received : "+userFile);
        for ( UserFile file: userFiles) {
            Log.e("intent" , "received : "+file.getName());
            if(file.getType().equals("Image")){
                images.add(file.getDownload_url());
                userImages.add(file);
            }
        }

        gridActivity();
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

    public void gridActivity() {
        ZGrid.with(this, images)
                .setToolbarColorResId(R.color.colorPrimary)
                .setTitle("Assistio")
                .setToolbarTitleColor(ZColor.WHITE)
                .setSpanCount(3)
                .setGridImgPlaceHolder(R.color.colorPrimary)
                .show();
    }
}
