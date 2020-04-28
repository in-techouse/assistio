package lcwu.fyp.asistio.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Calendar;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.User;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    String strFstName, strLastName, strPhone, strEmail;
    private TextView profileFirstName, profileLastName, profilePhone, profileEmail;
    private Session session;
    private Button btnUpdate;
    private User user;
    Helpers helpers;
    ProgressBar updateProgress;
    private DatabaseReference databaseReference;
    private ImageView imageView;
    private boolean isImage;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new Session(UserProfileActivity.this);
        user = session.getUser();
        helpers = new Helpers();

        profileFirstName = findViewById(R.id.profileFirstName);
        profileLastName = findViewById(R.id.profileLastName);
        profilePhone = findViewById(R.id.profilePhone);
        profileEmail = findViewById(R.id.profileEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        isImage = false;


        profileFirstName.setText(user.getFirst_Name());
        profileLastName.setText(user.getLast_Name());
        profilePhone.setText(user.getPhone_no());
        profileEmail.setText(user.getEmail());
        btnUpdate.setOnClickListener(this);
        updateProgress = findViewById(R.id.updateProgress);
        imageView = findViewById(R.id.userImage);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (user.getImage() != null && !user.getImage().equalsIgnoreCase("")) {
            Glide.with(getApplicationContext()).load(user.getImage()).into(imageView);
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.profile));
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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnUpdate: {
                //Check Internet
                boolean isConn = helpers.isConnected(UserProfileActivity.this);
                if (!isConn) {
                    helpers.showError(UserProfileActivity.this, "Internet Error", "No internet found check your internet connection and try again?");
                    return;
                }

                strFstName = profileFirstName.getText().toString();
                strLastName = profileLastName.getText().toString();
                strEmail = profileEmail.getText().toString();
                strPhone = profilePhone.getText().toString();


                boolean flag = isValid();
                if (flag) {
                    Log.e("Profile", "Validation Successful");
                    if (isImage) {
                        Log.e("Profile", "Image Found");
                        uploadImage();
                    } else {
                        Log.e("Profile", "No Image Found");
                        updateProgress.setVisibility(View.VISIBLE);
                        btnUpdate.setVisibility(View.GONE);
                        //Update Data here
                        updateDatabase();
                    }
                }
                break;
            }

            case R.id.fab: {
                boolean flag = hasPermissions(UserProfileActivity.this, PERMISSIONS);
                if (!flag) {
                    ActivityCompat.requestPermissions(UserProfileActivity.this, PERMISSIONS, 1);
                } else {
                    openGallery();
                }
                break;
            }

        }
    }

    private void uploadImage() {
        updateProgress.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Users").child(user.getId());
        Uri selectedMediaUri = Uri.parse(imagePath.toString());

        File file = new File(selectedMediaUri.getPath());
        Log.e("file", "in file object value " + file.toString());
        Log.e("Profile", "Uri: " + selectedMediaUri.getPath() + " File: " + file.exists());
//
//        if(!file.exists()){
//            Log.e("Uri" , "file not exists showing error");
//            registrationProgress.setVisibility(View.GONE);
//            editSubmitBtn.setVisibility(View.VISIBLE);
//            helpers.showError(EditUserProfile.this, "ERROR!", "Something went wrong.\n Please try again later.");
//            return;
//        }
        Calendar calendar = Calendar.getInstance();

        storageReference.child(calendar.getTimeInMillis() + "").putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("Profile", "in OnSuccess " + uri.toString());
                        user.setImage(uri.toString());
                        updateProgress.setVisibility(View.GONE);
                        btnUpdate.setVisibility(View.VISIBLE);
                        updateDatabase();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Profile", "Downlaod Url: " + e.getMessage());
                        updateProgress.setVisibility(View.GONE);
                        btnUpdate.setVisibility(View.VISIBLE);
                        helpers.showError(UserProfileActivity.this, "ERROR!", "Something went wrong.\n Please try again later.");
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Profile", "Upload Image Url: " + e.getMessage());
                updateProgress.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                helpers.showError(UserProfileActivity.this, "ERROR!", "Something went wrong.\n Please try again later.");
            }
        });
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            openGallery();
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

    private boolean isValid() {
        boolean flag = true;
        if (strFstName.length() < 3) {
            profileFirstName.setError("Enter a valid name");
        } else {
            profileFirstName.setError(null);
        }
        if (strLastName.length() < 3) {
            profileLastName.setError("Enter a valid name");
            flag = false;
        } else {
            profileLastName.setError(null);
        }
        if (strPhone.length() != 11 || !Patterns.PHONE.matcher(strPhone).matches()) {
            profilePhone.setError("Enter a valid phone no");
            flag = false;
        } else {
            profilePhone.setError(null);
        }

        return flag;

    }

    private void updateDatabase() {
        updateProgress.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        user.setEmail(strEmail);
        user.setFirst_Name(strFstName);
        user.setLast_Name(strLastName);
        user.setPhone_no(strPhone);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getId());
        databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateProgress.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                session.setSession(user);

                Toast toast = Toast.makeText(getApplicationContext(), "Profile Updated", Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();
                Intent in = new Intent(UserProfileActivity.this, Dashboard.class);
                startActivity(in);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateProgress.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                helpers.showError(UserProfileActivity.this, "ERROR!", "Something went wrong.\n Please try again later.");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Profile", "Gallery Call Back Received in Fragment with Request Code: " + requestCode);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri image = data.getData();
                    if (image != null) {
                        Glide.with(getApplicationContext()).load(image).into(imageView);
                        imagePath = image;
                        isImage = true;
                    }
                }
            }
        }
    }
}
