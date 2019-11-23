package lcwu.fyp.asistio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;

    EditText edtFName,edtLName,edtphno,edtemail1,edtpass,edtpass1;
    String strFName , strLName ,strphno ,stremai1 , strpass , strpass1;
    ProgressBar registrationProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegister = findViewById(R.id.btnRegister);

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

        switch(id){
            case R.id.btnRegister:{

                strFName= edtFName.getText().toString();
                strLName= edtLName.getText().toString();
                strphno = edtphno.getText().toString();
                stremai1 = edtemail1.getText().toString();
                strpass = edtpass.getText().toString();
                strpass1 = edtpass1.getText().toString();

              boolean flag = isValid();
              if (flag){
                  //Firebase
                  //to show progress load
                  registrationProgress.setVisibility(View.VISIBLE);
                  btnRegister.setVisibility(View.GONE);

                  FirebaseAuth auth = FirebaseAuth.getInstance();
                  auth.createUserWithEmailAndPassword(stremai1,strpass1 )
                          .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                              @Override
                              public void onSuccess(AuthResult authResult) {
                                  registrationProgress.setVisibility(View.GONE);
                                  btnRegister.setVisibility(View.VISIBLE);
                                  Log.e("Registration" , "Success");
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  registrationProgress.setVisibility(View.GONE);
                                  btnRegister.setVisibility(View.VISIBLE);
                                  Log.e("Registration" , "Fail " + e.getMessage());
                              }
                          });
              }

                break;
            }

        }

    }
    private boolean isValid(){
        boolean flag = true;
        if (strFName.length()<3){
            edtFName.setError("Enter a valid name");
        }
        else{
            edtFName.setError(null);
        }
        if (strLName.length()<3){
            edtLName.setError("Enter a valid name");
            flag = false;
        }
        else{
            edtLName.setError(null);
        }
        if (strphno.length()!=11 || !Patterns.PHONE.matcher(strphno).matches()){
            edtphno.setError("Enter a valid phone no");
            flag = false;
        }
        else{
            edtphno.setError(null);
        }
        if(stremai1.length()<6 || !Patterns.EMAIL_ADDRESS.matcher(stremai1).matches()) {
            edtemail1.setError("Enter a valid E-mail");
            flag = false;
        }
        else{
            edtemail1.setError(null);
        }
        if (strpass.length()<6){
            edtpass.setError("Invalid password");
            flag = false;
        }
        else{
            edtpass.setError(null);
        }
        if (strpass1.length()<6){
            edtpass1.setError("Invalid password");
            flag = false;
        }
        else{
            edtpass1.setError(null);
        }
        return  flag;

    }
}



