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

import lcwu.fyp.asistio.Director.Helpers;

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
                boolean isConn = helpers.isConnected(LoginActivity.this);
                if (!isConn) {
                    helpers.showError(LoginActivity.this , "Internet Error","There is no internet connection"   );
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
                                helpers.showError(LoginActivity.this, "Login Error" , e.getMessage());

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
}
