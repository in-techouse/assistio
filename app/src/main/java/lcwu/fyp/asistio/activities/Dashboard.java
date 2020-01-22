package lcwu.fyp.asistio.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zcw.togglebutton.ToggleButton;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.User;
import lcwu.fyp.asistio.model.UserFile;
import lcwu.fyp.asistio.services.ScanMediaService;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Helpers helpers;
    private User user;
    private Session session;
    private CircleImageView profile_image;
    private TextView profile_name;
    private TextView profile_email;
    private DrawerLayout drawer;
    private ToggleButton toggleButton;
    private TextView contacts;
    private TextView images, videos, audios, notes, documents;
    private LinearLayout contactsBox , documentsBox , imagesBox , videosBox , audiosBox , notesBox;
    private List<UserFile> userFile = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        helpers = new Helpers();
        session = new Session(Dashboard.this);
        user = session.getUser();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        profile_image = header.findViewById(R.id.profile_image);
        profile_name = header.findViewById(R.id.profile_name);
        profile_email = header.findViewById(R.id.profile_email);
        profile_name.setText(user.getFirst_Name()+ " "+ user.getLast_Name());
        profile_email.setText(user.getEmail());


        contactsBox =  findViewById(R.id.contactsBox);
        documentsBox = findViewById(R.id.documentsBox);
        imagesBox = findViewById(R.id.imagesBox);
        videosBox = findViewById(R.id.videosBox);
        audiosBox = findViewById(R.id.audiosBox);
        notesBox = findViewById(R.id.notesBox);

        toggleButton = findViewById(R.id.toggleButton);
        contacts =   findViewById(R.id.contacts);
        images = findViewById(R.id.images);
        audios = findViewById(R.id.audios);
        videos = findViewById(R.id.videos);
        documents = findViewById(R.id.documents);
        notes = findViewById(R.id.notes);



        contactsBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this , ShowContacts.class);
                startActivity(in);
            }
        });
        documentsBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this , ShowDocuments.class);
                startActivity(in);
            }
        });
        imagesBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this , ShowImages.class);
                Log.e("intent" , "goint to images : "+userFile);
                ListUserFile listUserFile = new ListUserFile();
                listUserFile.setUserFiles(userFile);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", listUserFile);
                in.putExtras(bundle);
                startActivity(in);
            }
        });
        videosBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this , ShowVideos.class);
                startActivity(in);
            }
        });
        audiosBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this , ShowAudios.class);
                startActivity(in);
            }
        });
        notesBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent in = new Intent(Dashboard.this , ShowNotes.class);
                startActivity(in);
            }
        });




        contacts.setText(user.getContacts()+"");
        images.setText(user.getImages()+"");
        audios.setText(user.getAudios()+"");
        videos.setText(user.getVideos()+"");
        documents.setText(user.getDocuments()+"");
        notes.setText(user.getNotes()+"");
//






//        boolean flag = session.getSync();
//        startServices();
//        if(flag){
//            System.out.println("in if with flag" + flag);
//            toggleButton.setToggleOn();
//            startServices();
//        }
//        else{
//            System.out.println("in else with flag" +flag);
//            toggleButton.setToggleOff();
//        }
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                session.setSync(on);
            }
        });

        loadFiles();
    }


    private boolean askForPermission(){
        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, 10);

            return false;
        }
            return true;
    }
    public void startServices(){



//        Intent serviceIntent = new Intent("lcwu.fyp.asistio.services.ScanMediaService");
//        serviceIntent.setPackage(this.getPackageName());
//        serviceIntent.setAction("lcwu.fyp.asistio.services.ScanMediaService");
//        startService(serviceIntent);
//
//        startService(new Intent(this, ScanMediaService.class));

        Log.e("Service", "Starting");
        if (askForPermission()){
            Log.e("Service", "in askForPermission");
            Log.e("Service", "in StartService");
            ScanMediaService.dashboard = Dashboard.this;
            startService(new Intent(Dashboard.this, ScanMediaService.class));
            Log.e("Service", "After exe");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startServices();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.nav_autoreply:{
                Intent it= new Intent(Dashboard.this,AutoReply.class);
                startActivity(it);
                break;
            }
            case R.id.nav_smssch:{
                Intent it= new Intent(Dashboard.this,SmsSchedular.class);
                startActivity(it);
                break;
            }

            case R.id.nav_speech:{
                Intent it= new Intent(Dashboard.this,SpeechNotes.class);
                startActivity(it);
                break;
            }
            case R.id.nav_logout:{
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent it= new Intent(Dashboard.this,LoginActivity.class);
                startActivity(it);
                finish();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void loadFiles(){
        Log.e("get URL" , "inside function");
        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
        dataref.child("UserFiles").child(user.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.e("get URL" , "inside if");
                            for (DataSnapshot ds   :  dataSnapshot.getChildren()){
                                UserFile uFiles = ds.getValue(UserFile.class);
//                                indashboard.add(uFiles);
                                userFile.add(uFiles);
                            }
                            Log.e("get URL" , "Recieved : "+userFile.size());
                            Log.e("get URL" , "Recieved : "+userFile);
                            Toast.makeText(Dashboard.this, "You can move now", Toast.LENGTH_LONG).show();
                        }
                  }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("get URL" , "in cancelled");
                    }
                });
//        Log.e("ListUserFile" , "in loadfiles");
//        ListUserFile listUserFile = session.getUserFiles();
//        List<UserFile> userFiles = listUserFile.getUserFiles();
//        Log.e("ListUserFile" , "file size : " + session.getUserFiles().getUserFiles().size());
//        Log.e("ListUserFile" , "going to lop");
//        for(UserFile file: userFiles){
//            Log.e("ListUserFile" , "session with data : " + file.getName());
//        }
    }
}
