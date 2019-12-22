package lcwu.fyp.asistio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import lcwu.fyp.asistio.Director.Helpers;
import lcwu.fyp.asistio.Director.Session;
import lcwu.fyp.asistio.model.User;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnRegister;

    EditText edtFName,edtLName,edtphno,edtemail1,edtpass,edtpass1;
    String strFName , strLName ,strphno ,stremai1 , strpass , strpass1;
    ProgressBar registrationProgress;
    Helpers helpers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegister = findViewById(R.id.btnRegister);
        helpers= new Helpers();

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
                boolean isConn = isConnected();
                if (!isConn){
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
                                    Toast.makeText(RegistrationActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(RegistrationActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();

                    return;
                }
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
                                  DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
                                  //Save  Registration
                                  final User user = new User();
                                  user.setFirst_Name(strFName);
                                  user.setLast_Name(strLName);
                                  user.setPhone_no(strphno);
                                  user.setEmail(stremai1);
                                  String id = stremai1.replace("@","-");
                                  id = id.replace(".","_");
                                  user.setId(id);


                                  // ya line data save kar da ge
                                 reference.child("Users").child(id) .setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void aVoid) {
                                         Session session=new Session(RegistrationActivity.this);
                                         session.setSession(user);
                                         //Start Dashboard Activity
                                         Intent it=new Intent(RegistrationActivity.this,Dashboard.class);
                                         startActivity(it);
                                         finish();

                                     }
                                 }).addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {
                                         registrationProgress.setVisibility(View.GONE);
                                         btnRegister.setVisibility(View.VISIBLE);
                                         helpers.showError(RegistrationActivity.this,"ERROR","Something went wrong");


                                     }
                                 });



//                                  registrationProgress.setVisibility(View.GONE);
//                                 btnRegister.setVisibility(View.VISIBLE);
//                                  Intent it=new Intent(RegistrationActivity.this,Dashboard.class);
//                                 startActivity(it);
//                                 finish();
//                                  Log.e("Registration" , "Success");

                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  registrationProgress.setVisibility(View.GONE);
                                  btnRegister.setVisibility(View.VISIBLE);
                                  new FancyGifDialog.Builder(RegistrationActivity.this)
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
                                                  Toast.makeText(RegistrationActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                                              }
                                          })
                                          .OnNegativeClicked(new FancyGifDialogListener() {
                                              @Override
                                              public void OnClick() {
                                                  Toast.makeText(RegistrationActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                                              }
                                          })
                                          .build();
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



