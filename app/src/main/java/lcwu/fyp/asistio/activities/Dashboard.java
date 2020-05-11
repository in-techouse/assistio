package lcwu.fyp.asistio.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mzelzoghbi.zgallery.ZGrid;
import com.mzelzoghbi.zgallery.entities.ZColor;
import com.zcw.togglebutton.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.LastLocation;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.User;
import lcwu.fyp.asistio.model.UserFile;
import lcwu.fyp.asistio.services.ScanMediaService;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
    };
    private Helpers helpers;
    private User user;
    private Session session;
    private CircleImageView profile_image;
    private DrawerLayout drawer;
    private ToggleButton toggleButton;
    private FusedLocationProviderClient locationProviderClient;
    private List<UserFile> userFiles = new ArrayList<>();
    private List<UserFile> images = new ArrayList<>();
    private List<UserFile> videos = new ArrayList<>();
    private List<UserFile> audios = new ArrayList<>();
    private List<UserFile> documents = new ArrayList<>();
    private ArrayList<String> imagesList = new ArrayList<>();
    private TextView tvContacts, tvDocuments, tvImages, tvVideos, tvAudio, tvNotes;
    private Intent mServiceIntent;
    private boolean isLoaded = false;


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
        TextView profile_name = header.findViewById(R.id.profile_name);
        TextView profile_email = header.findViewById(R.id.profile_email);
        profile_name.setText(user.getFirst_Name() + " " + user.getLast_Name());
        profile_email.setText(user.getEmail());


        RelativeLayout contactsBox = findViewById(R.id.contactsBox);
        RelativeLayout documentsBox = findViewById(R.id.documentsBox);
        RelativeLayout imagesBox = findViewById(R.id.imagesBox);
        RelativeLayout videosBox = findViewById(R.id.videosBox);
        RelativeLayout audiosBox = findViewById(R.id.audiosBox);
        RelativeLayout notesBox = findViewById(R.id.notesBox);

        toggleButton = findViewById(R.id.toggleButton);
        tvContacts = findViewById(R.id.contacts);
        tvImages = findViewById(R.id.images);
        tvAudio = findViewById(R.id.audios);
        tvVideos = findViewById(R.id.videos);
        tvDocuments = findViewById(R.id.documents);
        tvNotes = findViewById(R.id.notes);

        contactsBox.setOnClickListener(this);
        documentsBox.setOnClickListener(this);
        imagesBox.setOnClickListener(this);
        videosBox.setOnClickListener(this);
        audiosBox.setOnClickListener(this);
        notesBox.setOnClickListener(this);

        tvContacts.setText(user.getContacts() + "");
        tvImages.setText(user.getImages() + "");
        tvAudio.setText(user.getAudios() + "");
        tvVideos.setText(user.getVideos() + "");
        tvDocuments.setText(user.getDocuments() + "");
        tvNotes.setText(user.getNotes() + "");
        userFiles = new ArrayList<>();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(Dashboard.this);

        setDefaultApp();
        serviceCalling();
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                Log.e("Dashboard", "Syncing is: " + on);
                session.setSync(on);
                serviceCalling();
            }
        });

        loadFiles();
    }

    private void serviceCalling() {
        // Service calling
        boolean flag = session.getSync();
        if (flag) {
            Log.e("Dashboard", "Syncing is On");
            toggleButton.setToggleOn();
            startServices();
        } else {
            Log.e("Dashboard", "Syncing is Off");
            toggleButton.setToggleOff();
        }
    }

    public void setDefaultApp() {

        List<IntentFilter> filters = new ArrayList<>();
        List<ComponentName> activities = new ArrayList<>();
        getPackageManager().getPreferredActivities(filters, activities, null);
        Log.e("Dashboard", "Filers Size: " + filters.size());
        Log.e("Dashboard", "Activities Size: " + activities.size());
        boolean flag = false;
        for (ComponentName component : activities) {
            if (component.getPackageName().equals(getPackageName())) {
                flag = true;
                Log.e("Dashboard", "Activity, Package Name: " + component.getPackageName());
                Log.e("Dashboard", "Activity, Class Name: " + component.getClassName());
            }
        }

        if (flag) {
            return;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
        dialog.setMessage("To use ASISTIO, without any flaw, please set ASISTIO as DEFAULT MESSAGE APPLICATION.\nIf not you will not be able to use some major functionality.");
        dialog.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivityForResult(new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT).putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName()), 23);
            }
        });
        dialog.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Dashboard", "On Activity Result Called, Request Code: " + requestCode + " Result Code: " + resultCode);
        if (requestCode == 23) {
            setDefaultApp();
        }
    }

    private boolean hasPermissions(Context c, String... permission) {
        for (String p : permission) {
            if (ActivityCompat.checkSelfPermission(c, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void startServices() {
        Log.e("Dashboard", "in StartService");
        if (hasPermissions(Dashboard.this, PERMISSIONS)) {
            Log.e("Dashboard", "Permission Granted");
            ScanMediaService scanMediaService = new ScanMediaService();
            ScanMediaService.dashboard = Dashboard.this;
            mServiceIntent = new Intent(getBaseContext(), scanMediaService.getClass());
            if (!isMyServiceRunning(scanMediaService.getClass())) {
                startService(mServiceIntent);
            }
            Log.e("Dashboard", "After Execution");
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(Dashboard.this, PERMISSIONS, 10);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        super.onDestroy();
    }

    private void getDeviceLocation() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                helpers.showError(Dashboard.this, "ERROR!", "Something went wrong.\nPlease try again later. " + ex.getMessage());
            }
            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                helpers.showError(Dashboard.this, "ERROR!", "Something went wrong.\nPlease try again later. " + ex.getMessage());
            }
            if (!gps_enabled && !network_enabled) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
                dialog.setMessage("Oppss.Your Location Service is off.\nPlease turn on your Location and Tr again Later");
                dialog.setPositiveButton("Let me On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                return;
            }

            locationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                Location location = task.getResult();
                                if (location != null) {
                                    try {
                                        Date d = new Date();
                                        String formattedDate = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss a").format(d);

                                        Geocoder geocoder = new Geocoder(Dashboard.this);
                                        List<Address> addresses = null;
                                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                        if (addresses != null && addresses.size() > 0) {
                                            Address address = addresses.get(0);
                                            String strAddress = "";
                                            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                                strAddress = strAddress + "" + address.getAddressLine(i);
                                            }
                                            Log.e("Dashboard", "Address: " + strAddress);

                                            LastLocation lastLocation = new LastLocation();
                                            lastLocation.setAddress(strAddress);
                                            lastLocation.setDatetime(formattedDate);
                                            lastLocation.setLatitude(location.getLatitude());
                                            lastLocation.setLongitude(location.getLongitude());
                                            lastLocation.setTimeStamps(d.getTime());
                                            lastLocation.setBrand(Build.BRAND);
                                            lastLocation.setModel(Build.MODEL);
                                            lastLocation.setSerialNumber(Build.SERIAL);
                                            helpers.saveLastLocation(lastLocation, user.getId());
                                        }
                                    } catch (Exception e) {
                                        Log.e("Dashboard", "Exception: " + e.getMessage());
                                        helpers.showError(Dashboard.this, "ERROR!", "Something went wrong.\nPlease try again later.");
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            helpers.showError(Dashboard.this, "ERROR!", "Something went wrong.\nPlease try again later.");
                        }
                    });
        } catch (Exception e) {
            helpers.showError(Dashboard.this, "ERROR!", "Something went wrong.\nPlease try again later. " + e.getMessage());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startServices();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_autoreply: {
                Intent it = new Intent(Dashboard.this, AutoReply.class);
                startActivity(it);
                break;
            }
            case R.id.nav_profile: {
                Intent it = new Intent(Dashboard.this, UserProfileActivity.class);
                startActivity(it);
                break;
            }
            case R.id.nav_smssch: {
                Intent it = new Intent(Dashboard.this, SmsScheduler.class);
                startActivity(it);
                break;
            }
            case R.id.set_command: {
                Intent it = new Intent(Dashboard.this, SetCommand.class);
                startActivity(it);
                break;
            }

            case R.id.nav_speech: {
                Intent it = new Intent(Dashboard.this, SpeechNotes.class);
                startActivity(it);
                break;
            }
            case R.id.last_location: {
                Intent it = new Intent(Dashboard.this, ViewLastLocation.class);
                startActivity(it);
                break;
            }
            case R.id.mobileInfo: {
                Intent it = new Intent(Dashboard.this, MobileModel.class);
                startActivity(it);
                break;
            }
            case R.id.nav_logout: {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent it = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(it);
                finish();
                session.destroySession();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private void loadFiles() {
        Log.e("get URL", "inside function");
        DatabaseReference dataref = FirebaseDatabase.getInstance().getReference();
        dataref.child("UserFiles").child(user.getId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            userFiles.clear();
                            images.clear();
                            videos.clear();
                            audios.clear();
                            documents.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                UserFile uFile = ds.getValue(UserFile.class);
                                if (uFile != null) {
                                    userFiles.add(uFile);
                                    switch (uFile.getType()) {
                                        case "Image": {
                                            images.add(uFile);
                                            break;
                                        }
                                        case "Video": {
                                            videos.add(uFile);
                                            break;
                                        }
                                        case "Audio": {
                                            audios.add(uFile);
                                            break;
                                        }
                                        case "Document": {
                                            documents.add(uFile);
                                            break;
                                        }
                                    }
                                }
                            }

                            isLoaded = true;

                            user.setImages(images.size());
                            user.setVideos(videos.size());
                            user.setAudios(audios.size());
                            user.setDocuments(documents.size());

                            session.setSession(user);

                            tvContacts.setText(user.getContacts() + "");
                            tvImages.setText(user.getImages() + "");
                            tvAudio.setText(user.getAudios() + "");
                            tvVideos.setText(user.getVideos() + "");
                            tvDocuments.setText(user.getDocuments() + "");
                            tvNotes.setText(user.getNotes() + "");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("get URL", "in cancelled");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (!isLoaded)
            return;

        switch (id) {
            case R.id.contactsBox: {
                Intent in = new Intent(Dashboard.this, ShowContacts.class);
                startActivity(in);
                break;
            }
            case R.id.documentsBox: {
                Intent in = new Intent(Dashboard.this, ShowDocuments.class);
                ListUserFile listUserFile = new ListUserFile();
                listUserFile.setUserFiles(documents);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", listUserFile);
                in.putExtras(bundle);
                startActivity(in);
                break;
            }
            case R.id.imagesBox: {
                imagesList.clear();
                for (UserFile file : images) {
                    imagesList.add(file.getDownload_url());
                }

                ZGrid.with(Dashboard.this, imagesList)
                        .setToolbarColorResId(R.color.colorPrimaryDark)
                        .setTitle("Asistio")
                        .setToolbarTitleColor(ZColor.WHITE)
                        .setSpanCount(3)
                        .setGridImgPlaceHolder(R.color.colorPrimary)
                        .show();
                break;
            }
            case R.id.videosBox: {
                Intent in = new Intent(Dashboard.this, ShowVideos.class);
                ListUserFile listUserFile = new ListUserFile();
                listUserFile.setUserFiles(videos);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", listUserFile);
                in.putExtras(bundle);
                startActivity(in);
                break;
            }
            case R.id.audiosBox: {
                Intent in = new Intent(Dashboard.this, ShowAudios.class);
                ListUserFile listUserFile = new ListUserFile();
                listUserFile.setUserFiles(audios);
                Bundle bundle = new Bundle();
                bundle.putSerializable("files", listUserFile);
                in.putExtras(bundle);
                startActivity(in);
                break;
            }
            case R.id.notesBox: {
                Intent in = new Intent(Dashboard.this, ShowNotes.class);
                startActivity(in);
                break;
            }
        }
    }
}
