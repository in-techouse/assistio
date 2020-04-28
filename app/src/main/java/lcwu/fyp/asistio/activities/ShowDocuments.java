package lcwu.fyp.asistio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ShowDocsAdapter;
import lcwu.fyp.asistio.model.ListUserFile;
import lcwu.fyp.asistio.model.UserFile;

public class ShowDocuments extends AppCompatActivity {

    List<UserFile> userFiles = new ArrayList<>();
    List<UserFile> userDocuments = new ArrayList<>();
    ArrayList<String> documents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_documents);

        Intent it = getIntent();
        if (it == null) {
            finish();
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        ListUserFile userFile = (ListUserFile) extras.getSerializable("files");
        Log.e("intent", "recievec : " + userFile);
        if (userFile == null) {
            finish();
            return;
        }
        userFiles = userFile.getUserFiles();
        for (UserFile file : userFiles) {
            Log.e("intent", "received : " + file.getName());
            if (file.getType().equals("Documents")) {
                documents.add(file.getDownload_url());
                userDocuments.add(file);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerDocView);

        ShowDocsAdapter adapter = new ShowDocsAdapter(userDocuments, this);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowDocuments.this);
        recyclerView.setLayoutManager(layoutManager);

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
