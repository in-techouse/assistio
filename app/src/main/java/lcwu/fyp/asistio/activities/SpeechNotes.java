package lcwu.fyp.asistio.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.SpeechNotesObject;
import lcwu.fyp.asistio.model.User;

public class SpeechNotes extends AppCompatActivity implements View.OnClickListener {
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private Button save;
    private TextView txt, clear;
    private Helpers helpers;
    private String text = "";
    private ProgressBar progress;
    private User user;
    private Toolbar speechToolbar;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SpeechNotes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_notes);

        speechToolbar = findViewById(R.id.speechToolbar);

        setSupportActionBar(speechToolbar);
        getSupportActionBar().setTitle("Speech Notes");

        speechToolbar.setBackground(getResources().getDrawable(R.drawable.mygradient));

        Button record = findViewById(R.id.record);
        save = findViewById(R.id.save);
        txt = findViewById(R.id.txt);
        clear = findViewById(R.id.clear);
        progress = findViewById(R.id.progress);

        save.setOnClickListener(this);
        record.setOnClickListener(this);
        clear.setOnClickListener(this);
        helpers = new Helpers();
        Session session = new Session(getApplicationContext());
        user = session.getUser();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.record: {
                promptSpeechInput();
                break;
            }
            case R.id.save: {
                if (text.length() < 1) {
                    return;
                }
                progress.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                SpeechNotesObject notesObject = new SpeechNotesObject();
                String nId = reference.child(user.getId()).push().getKey();
                notesObject.setId(nId);
                notesObject.setNote(text);
                reference.child(user.getId()).child(notesObject.getId()).setValue(notesObject)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progress.setVisibility(View.GONE);
                                save.setVisibility(View.VISIBLE);
                                helpers.showSuccess(SpeechNotes.this, "Speech Notes", "New note saved successfully.");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.setVisibility(View.GONE);
                                save.setVisibility(View.VISIBLE);
                                helpers.showError(SpeechNotes.this, "Speech Notes", "Something went wrong.\nPlease try again later.");

                            }
                        });
                break;
            }
            case R.id.clear: {
                text = "";
                txt.setText(text);
                clear.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            helpers.showError(SpeechNotes.this, "ERROR", "No voice recognition device available");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null && result.size() > 0) {
                        text = text + result.get(0) + " ";
                        txt.setText(text);
                        clear.setVisibility(View.VISIBLE);
                    }
                }
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
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
                Intent it = new Intent(SpeechNotes.this, SpeechNotesHistory.class);
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
}
