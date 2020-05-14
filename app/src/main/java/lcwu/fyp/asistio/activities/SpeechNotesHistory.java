package lcwu.fyp.asistio.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.SpeechNotesHistoryAdapter;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.SpeechNotesObject;
import lcwu.fyp.asistio.model.User;

public class SpeechNotesHistory extends AppCompatActivity {

    private LinearLayout loading;
    private RecyclerView historyList;
    private List<SpeechNotesObject> data;
    private SpeechNotesHistoryAdapter adapter;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SpeechNotes");
    private ValueEventListener listener;
    private User user;
    private Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_notes_history);

        loading = findViewById(R.id.loading);
        historyList = findViewById(R.id.historyList);

        adapter = new SpeechNotesHistoryAdapter(SpeechNotesHistory.this);
        historyList.setLayoutManager(new LinearLayoutManager(SpeechNotesHistory.this));
        historyList.setAdapter(adapter);
        Session session = new Session(getApplicationContext());
        user = session.getUser();
        helpers = new Helpers();
        data = new ArrayList<>();
        loadHistory();
    }

    private void loadHistory() {
        if (user == null) {
            helpers.showError(SpeechNotesHistory.this, "ERROR!", "Something went wrong.\nPlease try again later");
            return;
        }
        if (!helpers.isConnected(SpeechNotesHistory.this)) {
            helpers.showError(SpeechNotesHistory.this, "ERROR!", "No internet connection found.\nPlease connect to a network try again later");
            return;
        }
        loading.setVisibility(View.VISIBLE);
        historyList.setVisibility(View.GONE);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    SpeechNotesObject reply = d.getValue(SpeechNotesObject.class);
                    if (reply != null)
                        data.add(reply);
                }
                adapter.setNotesList(data);
                loading.setVisibility(View.GONE);
                historyList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                helpers.showError(SpeechNotesHistory.this, "ERROR!", "Something went wrong.\nPlease try again later");
                loading.setVisibility(View.GONE);
                historyList.setVisibility(View.VISIBLE);
            }
        };
        reference.child(user.getId()).addValueEventListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null)
            reference.child(user.getId()).removeEventListener(listener);
    }

    public void deleteNote(String id) {
        loading.setVisibility(View.VISIBLE);
        historyList.setVisibility(View.GONE);
        reference.child(user.getId()).child(id).removeValue();
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
