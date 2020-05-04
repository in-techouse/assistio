package lcwu.fyp.asistio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.Contact;
import lcwu.fyp.asistio.model.User;

public class AutoReply extends AppCompatActivity implements View.OnClickListener {

    private EditText message;
    private EditText reply_message;
    private Button save;
    private Helpers helpers;
    private User user;
    private Session session;
    private ImageView select_contacts;
    private GridView contactsGrid;
    private String strMessage, strReplyMessage;
    private List<Contact> contacts;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reply);

        helpers = new Helpers();
        session = new Session(getApplicationContext());
        user = session.getUser();

        message = findViewById(R.id.message);
        save = findViewById(R.id.save);
        reply_message = findViewById(R.id.reply_message);
        select_contacts = findViewById(R.id.select_contacts);
        contactsGrid = findViewById(R.id.contactsGrid);
        progressBar = findViewById(R.id.progressBar);
        save.setOnClickListener(this);
        select_contacts.setOnClickListener(this);

        contacts = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.save: {
                if (helpers.isConnected(getApplicationContext())) {
                    helpers.showError(AutoReply.this, "ERROR", "No internet connection found.\nConnect to a network and try again.");
                    return;
                }
                boolean flag = isValid();
                if (flag) {
                    // Save to database
                    progressBar.setVisibility(View.VISIBLE);
                    save.setVisibility(View.GONE);
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
            flag = false;
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
