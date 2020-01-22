package lcwu.fyp.asistio.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import lcwu.fyp.asistio.R;

public class ForgotPasswordAcitivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtEmail;
    Button NewPassword;
    ProgressBar change_Password;
    String strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_acitivity);

        edtEmail = findViewById(R.id.edtEmail);
        NewPassword = findViewById(R.id.NewPassword);
        change_Password = findViewById(R.id.change_Password);


        NewPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.NewPassword: {
                strEmail = edtEmail.getText().toString();

                boolean flag = isValid();
                if (flag) {
                    change_Password.setVisibility(View.VISIBLE);
                    NewPassword.setVisibility(View.GONE);
                    //Firebase
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(strEmail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    change_Password.setVisibility(View.GONE);
                                    NewPassword.setVisibility(View.VISIBLE);
                                    Log.e("Login", "Success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    change_Password.setVisibility(View.GONE);
                                    NewPassword.setVisibility(View.VISIBLE);
                                    Log.e("Login", "Fail " + e.getMessage());
                                }
                            });

                }
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
