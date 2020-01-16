package lcwu.fyp.asistio.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView go_to_registration, forgotPassword;
    EditText edtEmail, edtPassword;
    String strEmail , strPassword;
    ProgressBar login_process;
    Helpers helpers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        helpers = new Helpers();
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        go_to_registration = findViewById(R.id.go_to_registration);
        login_process = findViewById(R.id.login_process);
        forgotPassword = findViewById(R.id.fogotPasword);


        btnLogin.setOnClickListener(this);
        go_to_registration.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnLogin: {
                boolean isConn = isConnected();
                if (!isConn) {
                    helpers.showError(LoginActivity.this,  "ERROR","Internet Connection Error");
                    return;
                }

                 strEmail = edtEmail.getText().toString();
                 strPassword = edtPassword.getText().toString();

                   boolean flag = isValid();
                    if (flag) {

                        login_process.setVisibility(View.VISIBLE);
                        btnLogin.setVisibility(View.GONE);

                        //Firebase
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signInWithEmailAndPassword(strEmail,strPassword)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                        String id = strEmail.replace("@","-");
                                        id = id.replace(".","_");

                                        reference.child("Users").child(id).addValueEventListener(new ValueEventListener() {
                                            //reading login data from database

                                            @Override
                                            //if successfully read
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.getValue() != null){
                                                    //Data is Valid
                                                    User u = dataSnapshot.getValue(User.class);
                                                    Session session = new Session(LoginActivity.this);
                                                    session.setSession(u);

                                                    //Start Dashboard Activity
                                                    Intent it = new Intent(LoginActivity.this, Dashboard.class);
                                                    startActivity(it);
                                                    finish();

                                                }
                                                else{
                                                    login_process.setVisibility(View.GONE);
                                                    btnLogin.setVisibility(View.VISIBLE);
                                                    helpers.showError(LoginActivity.this, "ERROR", "Something Went Wrong");
                                                }

                                            }

                                            @Override
                                            //if not successfully read
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                login_process.setVisibility(View.GONE);
                                                btnLogin.setVisibility(View.VISIBLE);
                                                helpers.showError(LoginActivity.this, "ERROR","Something Went Wrong");


                                            }
                                        });
//                                        login_process.setVisibility(View.GONE);
//                                        btnLogin.setVisibility(View.VISIBLE);
//
//                                            Intent it=new Intent(LoginActivity.this,Dashboard.class);
//                                        startActivity(it);
//                                        finish();
//
//                                        Log.e("Login","Success");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                login_process.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                                helpers.showError(LoginActivity.this,  "ERROR",e.getMessage());
                            }
                        });

                    }
                    break;
            }
            case R.id.go_to_registration:{
                Intent it = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(it);
                break;
            }

            case R.id.fogotPasword:{
                Intent it = new Intent(LoginActivity.this, ForgotPasswordAcitivity.class);
                startActivity(it);
                break;
            }

        }

    }

    private boolean isValid(){
        boolean flag =true;
        if (strEmail.length() < 6 || !Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            edtEmail.setError("Enter a valid Email");
            flag= false;
        } else {
            edtEmail.setError(null);
        }
        if (strPassword.length() < 6) {
            edtPassword.setError("Enter valid password");
            flag=false;
        } else {
            edtPassword.setError(null);
        }
        return flag;

    }
    public boolean isConnected(Context c) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)
            connected = true;
        else
            connected = false;
        return  connected;
    }
    // Check Internet Connection
    private boolean isConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo
                (ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            connected = true;
        else
            connected = false;
        return  connected;
    }






}
