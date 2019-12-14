package lcwu.fyp.asistio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    TextView go_to_registration;
    EditText edtEmail, edtPassword;
    String strEmail , strPassword;
    ProgressBar login_process;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        go_to_registration = findViewById(R.id.go_to_registration);
        btnLogin.setOnClickListener(this);
        go_to_registration.setOnClickListener(this);

        login_process = findViewById(R.id.login_process);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnLogin: {
                boolean isConn = isConnected();
                if (!isConn) {
                    //show error message
                    new FancyGifDialog.Builder(this)
                            .setTitle("ERROR")
                            .setMessage("Internet Connection Error")
                            .setNegativeBtnText("Cancel")
                            .setPositiveBtnBackground("#FF4081")
                            .setPositiveBtnText("Ok")
                            .setNegativeBtnBackground("#FFA9A7A8")
                            .setGifResource(R.drawable.bcb5aea7be9a3c8bd8be1b0d345d76e9)   //Pass your Gif here
                            .isCancellable(true)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(LoginActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();
                    strEmail = edtEmail.getText().toString();
                    strPassword = edtPassword.getText().toString();
                }

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
                                        login_process.setVisibility(View.GONE);
                                        btnLogin.setVisibility(View.VISIBLE);

                                            Intent it=new Intent(LoginActivity.this,Dashboard.class);
                                        startActivity(it);
                                        finish();

                                        Log.e("Login","Success");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                login_process.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                                new FancyGifDialog.Builder(LoginActivity.this)
                                        .setTitle("ERROR")
                                        .setMessage(e.getMessage())
                                        .setNegativeBtnText("Cancel")
                                        .setPositiveBtnBackground("#FF4081")
                                        .setPositiveBtnText("Ok")
                                        .setNegativeBtnBackground("#FFA9A7A8")
                                        .setGifResource(R.drawable.bcb5aea7be9a3c8bd8be1b0d345d76e9)   //Pass your Gif here
                                        .isCancellable(true)
                                        .OnPositiveClicked(new FancyGifDialogListener() {
                                            @Override
                                            public void OnClick() {
                                                Toast.makeText(LoginActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .OnNegativeClicked(new FancyGifDialogListener() {
                                            @Override
                                            public void OnClick() {
                                                Toast.makeText(LoginActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .build();
                                Log.e("Login","Fail " + e.getMessage());
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
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            connected = true;
        else
            connected = false;
        return  connected;
    }

        public boolean isConnected() {
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
                connected = true;
            else
                connected = false;
            return  connected;
        }



}
