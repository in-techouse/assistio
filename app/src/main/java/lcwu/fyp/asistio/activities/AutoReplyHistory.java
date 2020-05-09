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
import lcwu.fyp.asistio.adapters.AutoReplyHistoryAdapter;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.AutoSmsReply;
import lcwu.fyp.asistio.model.User;

public class AutoReplyHistory extends AppCompatActivity {
    private LinearLayout loading;
    private RecyclerView historyList;
    private List<AutoSmsReply> data;
    private AutoReplyHistoryAdapter adapter;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AutoReply");
    private ValueEventListener listener;
    private User user;
    private Helpers helpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reply_history);

        loading = findViewById(R.id.loading);
        historyList = findViewById(R.id.historyList);

        adapter = new AutoReplyHistoryAdapter(AutoReplyHistory.this);
        historyList.setLayoutManager(new LinearLayoutManager(AutoReplyHistory.this));
        historyList.setAdapter(adapter);
        Session session = new Session(getApplicationContext());
        user = session.getUser();
        helpers = new Helpers();
        data = new ArrayList<>();
        loadHistory();
    }

    private void loadHistory() {
        if (user == null) {
            helpers.showError(AutoReplyHistory.this, "ERROR!", "Something went wrong.\nPlease try again later");
            return;
        }
        if (!helpers.isConnected(AutoReplyHistory.this)) {
            helpers.showError(AutoReplyHistory.this, "ERROR!", "No internet connection found.\nPlease connect to a network try again later");
            return;
        }
        loading.setVisibility(View.VISIBLE);
        historyList.setVisibility(View.GONE);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    AutoSmsReply reply = d.getValue(AutoSmsReply.class);
                    if (reply != null)
                        data.add(reply);
                }
                adapter.setReplyList(data);
                loading.setVisibility(View.GONE);
                historyList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                helpers.showError(AutoReplyHistory.this, "ERROR!", "Something went wrong.\nPlease try again later");
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

    public void deleteItem(String id) {
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
