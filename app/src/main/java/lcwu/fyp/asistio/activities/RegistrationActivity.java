package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.model.User;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edtFName, edtLName, edtphno, edtemail1, edtpass, edtpass1;
    String strFName, strLName, strphno, stremai1, strpass, strpass1;
    ProgressBar registrationProgress;
    Helpers helpers;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegister = findViewById(R.id.btnRegister);
        helpers = new Helpers();

        edtFName = findViewById(R.id.edtFName);
        edtLName = findViewById(R.id.edtLName);
        edtphno = findViewById(R.id.edtphno);
        edtemail1 = findViewById(R.id.edtemail1);
        edtpass = findViewById(R.id.edtpass);
        edtpass1 = findViewById(R.id.edtpass1);
        registrationProgress = findViewById(R.id.registrationProgress);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {
            case R.id.btnRegister: {
                boolean isConn = helpers.isConnected(getApplicationContext());
                if (!isConn) {
                    //show error message
                    helpers.showError(RegistrationActivity.this, "ERROR", "No internet connection found.\nConnect to a network and try again.");
                    return;
                }
                strFName = edtFName.getText().toString();
                strLName = edtLName.getText().toString();
                strphno = edtphno.getText().toString();
                stremai1 = edtemail1.getText().toString();
                strpass = edtpass.getText().toString();
                strpass1 = edtpass1.getText().toString();

                boolean flag = isValid();
                if (flag) {
                    //Firebase
                    //to show progress load
                    registrationProgress.setVisibility(View.VISIBLE);
                    btnRegister.setVisibility(View.GONE);

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(stremai1, strpass1)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    if (auth.getCurrentUser() == null) {
                                        registrationProgress.setVisibility(View.GONE);
                                        btnRegister.setVisibility(View.VISIBLE);
                                        helpers.showError(RegistrationActivity.this, "ERROR", "Something went wrong");
                                        return;
                                    }
                                    auth.getCurrentUser().sendEmailVerification()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                                    //Save  Registration
                                                    final User user = new User();
                                                    user.setFirst_Name(strFName);
                                                    user.setLast_Name(strLName);
                                                    user.setPhone_no(strphno);
                                                    user.setEmail(stremai1);
                                                    user.setDocuments(0);
                                                    user.setAudios(0);
                                                    user.setContacts(0);
                                                    user.setVideos(0);
                                                    user.setImages(0);
                                                    user.setNotes(0);
                                                    user.setFirst(false);
                                                    String id = stremai1.replace("@", "-");
                                                    id = id.replace(".", "_");
                                                    user.setId(id);

                                                    // ya line data save kar da ge
                                                    reference.child("Users").child(id).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            registrationProgress.setVisibility(View.GONE);
                                                            btnRegister.setVisibility(View.VISIBLE);
                                                            helpers.showSuccess(RegistrationActivity.this, "EMAIL VERIFICATION", "A verification email has been sent to your given email address.\nPlease verify your email.\nYou can login after your email verification");
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            registrationProgress.setVisibility(View.GONE);
                                                            btnRegister.setVisibility(View.VISIBLE);
                                                            helpers.showError(RegistrationActivity.this, "ERROR", "Something went wrong");
                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    registrationProgress.setVisibility(View.GONE);
                                                    btnRegister.setVisibility(View.VISIBLE);
                                                    helpers.showError(RegistrationActivity.this, "ERROR", "Something went wrong");
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            registrationProgress.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                            helpers.showError(RegistrationActivity.this, "ERROR", e.getMessage());
                            Log.e("Registration", "Fail " + e.getMessage());
                        }
                    });
                }

                break;
            }
        }
    }

    private boolean isValid() {
        boolean flag = true;
        if (strFName.length() < 3) {
            edtFName.setError("Enter a valid name");
            flag = false;
        } else {
            edtFName.setError(null);
        }
        if (strLName.length() < 3) {
            edtLName.setError("Enter a valid name");
            flag = false;
        } else {
            edtLName.setError(null);
        }
        if (strphno.length() != 11 || !Patterns.PHONE.matcher(strphno).matches()) {
            edtphno.setError("Enter a valid phone no");
            flag = false;
        } else {
            edtphno.setError(null);
        }
        if (stremai1.length() < 6 || !Patterns.EMAIL_ADDRESS.matcher(stremai1).matches()) {
            edtemail1.setError("Enter a valid E-mail");
            flag = false;
        } else {
            edtemail1.setError(null);
        }
        if (strpass.length() < 6) {
            edtpass.setError("Invalid password");
            flag = false;
        } else {
            edtpass.setError(null);
        }
        if (strpass1.length() < 6) {
            edtpass1.setError("Invalid password");
            flag = false;
        } else {
            edtpass1.setError(null);
        }
        if (strpass.length() > 5 && strpass1.length() > 5 && !strpass.equals(strpass1)) {
            edtpass1.setError("Password doesn't match.");
            flag = false;
        }
        return flag;
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



