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
import com.zcw.togglebutton.ToggleButton;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.User;
import lcwu.fyp.asistio.services.ScanMediaService;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Helpers helpers;
    private User user;
    private Session session;
    private CircleImageView profile_image;
    private TextView profile_name;
    private TextView profile_email;
    private DrawerLayout drawer;
    private ToggleButton toggleButton;
    private TextView contacts, images, videos, audios, notes, documents;

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



        toggleButton = findViewById(R.id.toggleButton);
        contacts = findViewById(R.id.contacts);
        images = findViewById(R.id.images);
        audios = findViewById(R.id.audios);
        videos = findViewById(R.id.videos);
        documents = findViewById(R.id.documents);
        notes = findViewById(R.id.notes);


        contacts.setText(user.getContacts()+"");
        images.setText(user.getImages()+"");
        audios.setText(user.getAudios()+"");
        videos.setText(user.getVideos()+"");
        documents.setText(user.getDocuments()+"");
        notes.setText(user.getNotes()+"");

        boolean flag = session.getSync();
        startServices();
        if(flag){
            System.out.println("in if with flag" + flag);
            toggleButton.setToggleOn();
            startServices();
        }
        else{
            System.out.println("in else with flag" +flag);
            toggleButton.setToggleOff();
        }

        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                session.setSync(on);
            }
        });


    }

    private boolean askForPermission(){
        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(Dashboard.this,Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS}, 10);

            return false;
        }
            return true;
    }
    public void startServices(){



//        Intent serviceIntent = new Intent("lcwu.fyp.asistio.services.ScanMediaService");
//        serviceIntent.setPackage(this.getPackageName());
//        serviceIntent.setAction("lcwu.fyp.asistio.services.ScanMediaService");
//        startService(serviceIntent);

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
}
