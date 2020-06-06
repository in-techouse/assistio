package lcwu.fyp.asistio.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.User;

public class SetCommand extends AppCompatActivity implements View.OnClickListener {
    EditText edtKey;
    Button btn_key;
    private Session session;
    private User user;
    private Helpers helpers;
    String strKey;
    ProgressBar setkey_progress;
    Toolbar set_command;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_command);

        set_command=findViewById(R.id.set_command);

        setSupportActionBar(set_command);
        getSupportActionBar().setTitle("Set Command");

        helpers = new Helpers();
        edtKey = findViewById(R.id.edtKey);
        btn_key = findViewById(R.id.btn_key);
        setkey_progress = findViewById(R.id.setKey_Progress);
        session = new Session(SetCommand.this);
        user = session.getUser();
        btn_key.setOnClickListener(this);

        if (user.getCommand() != null) {
            edtKey.setText(user.getCommand());
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
            case R.id.btn_key: {
                boolean isConn = isConnected();
                if (!isConn) {
                    helpers.showError(SetCommand.this, "ERROR", "Internet Connection Error");
                    return;
                }
                strKey = edtKey.getText().toString();
                if (strKey.length() < 5) {
                    edtKey.setError("Key must contain 5 characters");
                    return;
                } else {
                    edtKey.setError(null);
                }
                setkey_progress.setVisibility(View.VISIBLE);
                btn_key.setVisibility(View.GONE);

                user.setCommand(strKey);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                reference.child(user.getId()).setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                setkey_progress.setVisibility(View.GONE);
                                btn_key.setVisibility(View.VISIBLE);
                                session.setSession(user);
                                ShowSuccess();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                setkey_progress.setVisibility(View.GONE);
                                btn_key.setVisibility(View.VISIBLE);
                                helpers.showError(SetCommand.this, "Error", "Something went wrong");
                            }
                        });
                break;
            }
        }
    }

    private void ShowSuccess() {
        new FancyGifDialog.Builder(SetCommand.this)
                .setTitle("Key set successfully")
                .setMessage("")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Ok")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.success)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        finish();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        finish();
                    }
                })
                .build();
    }


    // Check Internet Connection
    private boolean isConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()
                == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo
                (ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        return connected;
    }
}

