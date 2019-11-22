package lcwu.fyp.asistio;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;

    EditText edtEmail, edtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btnLogin: {
                String strEmail = edtEmail.getText().toString();
                String strPassword = edtPassword.getText().toString();

                if (strEmail.length() < 6 || !Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    edtEmail.setError("Enter a valid Email");
                } else {
                    edtEmail.setError(null);
                }
                if (strPassword.length() < 6) {
                    edtPassword.setError("Enter valid password");
                } else {
                    edtPassword.setError(null);
                }
                break;
            }

        }

    }

}
