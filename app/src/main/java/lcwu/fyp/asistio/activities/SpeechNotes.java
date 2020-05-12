package lcwu.fyp.asistio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.director.Helpers;

public class SpeechNotes extends AppCompatActivity implements View.OnClickListener {

    private Button save, record;
    private TextView txt;
    private boolean isRecording = false;
    private SpeechRecognizer recognizer;
    private Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_notes);

        record = findViewById(R.id.record);
        save = findViewById(R.id.save);
        txt = findViewById(R.id.txt);

        save.setOnClickListener(this);
        record.setOnClickListener(this);
        helpers = new Helpers();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.record: {
                if (isRecording) {
                    record.setText("RECORD");
                    record.setBackgroundColor(getResources().getColor(R.color.colorSuccess));
                    record.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.record), null);
                    isRecording = false;
                    stopListening();
                } else {
                    if (!SpeechRecognizer.isRecognitionAvailable(getApplicationContext())) {
                        helpers.showError(SpeechNotes.this, "ERROR", "No voice recognition device available");
                        return;
                    }
                    record.setText("STOP  ");
                    record.setBackgroundColor(getResources().getColor(R.color.colorDanger));
                    record.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.stop), null);
                    isRecording = true;
                    startListening();
                }
                break;
            }
            case R.id.save: {
                break;
            }
        }
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "lcwu.fyp.asistio");
        recognizer = SpeechRecognizer.createSpeechRecognizer(this.getApplicationContext());
        RecognitionListener listener = new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {
                Log.e("SpeechNotes", "Speech Started");
            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {
                Log.e("SpeechNotes", "Speech Ended");
            }

            @Override
            public void onError(int error) {
                Log.e("SpeechNotes", "Error Occurs");
            }

            @Override
            public void onResults(Bundle results) {
                Log.e("SpeechNotes", "Results");
                ArrayList<String> voiceResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (voiceResults == null) {
                    Log.e("SpeechNotes", "No Results");
                } else {
                    Log.e("SpeechNotes", "Results Found");
                    String str = "";
                    for (String match : voiceResults) {
                        str = str + match + " ";
                    }
                    txt.setText(str);
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        };
        recognizer.setRecognitionListener(listener);
        recognizer.startListening(intent);
    }

    private void stopListening() {
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer.destroy();
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
}
