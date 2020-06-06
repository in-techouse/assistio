package lcwu.fyp.asistio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.SelectedContactsAdapter;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.AutoSmsReply;
import lcwu.fyp.asistio.model.Contact;
import lcwu.fyp.asistio.model.User;

public class AutoReply extends AppCompatActivity implements View.OnClickListener {

    private EditText message;
    private EditText reply_message;
    private Button save;
    private Helpers helpers;
    private User user;
    private SelectedContactsAdapter adapter;
    private String strMessage, strReplyMessage;
    private List<Contact> contacts;
    private ProgressBar progressBar;
    private Toolbar autoToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reply);

        autoToolbar=findViewById(R.id.auto_reply_toolbar);

        setSupportActionBar(autoToolbar);
        getSupportActionBar().setTitle("Auto Reply");
        helpers = new Helpers();
        Session session = new Session(getApplicationContext());
        user = session.getUser();

        message = findViewById(R.id.message);
        save = findViewById(R.id.save);
        reply_message = findViewById(R.id.reply_message);
        ImageView select_contacts = findViewById(R.id.select_contacts);
        GridView contactsGrid = findViewById(R.id.contactsGrid);
        progressBar = findViewById(R.id.progressBar);
        save.setOnClickListener(this);
        select_contacts.setOnClickListener(this);

        contacts = new ArrayList<>();
        adapter = new SelectedContactsAdapter();
        contactsGrid.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.save: {
                if (!helpers.isConnected(getApplicationContext())) {
                    helpers.showError(AutoReply.this, "ERROR", "No internet connection found.\nConnect to a network and try again.");
                    return;
                }
                boolean flag = isValid();
                if (flag) {
                    // Save to database
                    progressBar.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AutoReply");
                    String autoReplyId = reference.child(user.getId()).push().getKey(); // Will return a unique id.
                    AutoSmsReply autoReply = new AutoSmsReply();
                    autoReply.setId(autoReplyId);
                    autoReply.setMessage(strMessage);
                    autoReply.setReplyMessage(strReplyMessage);
                    autoReply.setContactList(contacts);
                    reference.child(user.getId()).child(autoReplyId).setValue(autoReply)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    save.setVisibility(View.VISIBLE);
                                    helpers.showSuccess(AutoReply.this, "", "Auto reply preferences have been saved successfully.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    save.setVisibility(View.VISIBLE);
                                    helpers.showError(AutoReply.this, "ERROR", "Something went wrong.\nPlease try again later.");

                                }
                            });
                }
                break;
            }
            case R.id.select_contacts: {
                Intent intent = new Intent(AutoReply.this, ReadContacts.class);
                startActivityForResult(intent, 20);
                break;
            }
        }
    }

    private boolean isValid() {
        boolean flag = true;
        String error = "";
        strMessage = message.getText().toString();
        strReplyMessage = reply_message.getText().toString();

        if (strMessage.length() < 1) {
            message.setError("This field is required.");
            flag = false;
        } else {
            message.setError(null);
        }

        if (strReplyMessage.length() < 1) {
            reply_message.setError("This field is required.");
            flag = false;
        } else {
            reply_message.setError(null);
        }

        if (contacts.size() < 1) {
            error = "Please select some contacts first.";
            helpers.showError(AutoReply.this, "ERROR!", error);
            flag = false;
        }


        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 20 && resultCode == RESULT_OK) {
            Log.e("Result", "Result OK");
            if (data != null) {
                Log.e("Result", "Data OK");
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Log.e("Result", "Bundle OK");
                    contacts = (List<Contact>) bundle.getSerializable("contacts");
                    if (contacts == null) {
                        contacts = new ArrayList<>();
                        Log.e("SmsSender", "Reinitialized");
                    }
                    adapter.setContacts(contacts);
                    Log.e("SmsSender", "List OK with size: " + contacts.size());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history: {
                Log.e("History", "History Clicked");
                Intent it = new Intent(AutoReply.this, AutoReplyHistory.class);
                startActivity(it);
                break;
            }
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }
}
