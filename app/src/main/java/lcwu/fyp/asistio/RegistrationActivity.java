package lcwu.fyp.asistio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;
    TextView go_to_login;
    EditText edtFName,edtLName,edtphno,edtemail1,edtpass,edtpass1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegister = findViewById(R.id.btnRegister);
        go_to_login = findViewById(R.id.go_to_login);
        edtFName = findViewById(R.id.edtFName);
        edtLName = findViewById(R.id.edtLName);
        edtphno = findViewById(R.id.edtphno);
        edtemail1 = findViewById(R.id.edtemail1);
        edtpass = findViewById(R.id.edtpass);
        edtpass1 = findViewById(R.id.edtpass1);


        btnRegister.setOnClickListener(this);
        go_to_login . setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch(id){
            case R.id.btnRegister:{

                String strFName= edtFName.getText().toString();
                String strLName= edtLName.getText().toString();
                String strphno = edtphno.getText().toString();
                String stremail = edtemail1.getText().toString();
                String strpass = edtpass.getText().toString();
                String strpass1 = edtpass1.getText().toString();

                if (strFName.length()<3){
                    edtFName.setError("Enter a valid name");
                }
                else{
                    edtFName.setError(null);
                }
                if (strLName.length()<3){
                    edtLName.setError("Enter a valid name");
                }
                else{
                    edtLName.setError(null);
                }
                if (strphno.length()!=11 || !Patterns.PHONE.matcher(strphno).matches()){
                    edtphno.setError("Enter a valid phone no");
                }
                else{
                    edtphno.setError(null);
                }
                if(stremail.length()<6 || !Patterns.EMAIL_ADDRESS.matcher(stremail).matches()) {
                    edtemail1.setError("Enter a valid E-mail");
                }
                else{
                    edtemail1.setError(null);
                }
                if (strpass.length()<6){
                    edtpass.setError("Invalid password");
                }
                else{
                    edtpass.setError(null);
                }
                if (strpass1.length()<6){
                    edtpass1.setError("Invalid password");
                }
                else{
                    edtpass1.setError(null);
                }


                break;
            }
            case R.id.go_to_login:{
               // Intent it = new Intent(RegistrationActivity.this , LoginActivity.class);
               // startActivity(it);
                break;
            }
        }

    }
}
