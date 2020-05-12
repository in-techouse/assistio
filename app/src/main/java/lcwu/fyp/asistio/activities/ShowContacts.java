package lcwu.fyp.asistio.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ShowContactAdapter;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.Contact;
import lcwu.fyp.asistio.model.User;

public class ShowContacts extends AppCompatActivity {
    private LinearLayout loading;
    private RecyclerView contacts;
    private List<Contact> contactList, filteredList;
    private Session session;
    private User user;
    private Helpers helpers;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Contacts");
    private ValueEventListener listener;
    private ShowContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contacts);

        loading = findViewById(R.id.loading);
        contacts = findViewById(R.id.contacts);
        contactList = new ArrayList<>();
        filteredList = new ArrayList<>();
        session = new Session(getApplicationContext());
        user = session.getUser();
        helpers = new Helpers();

        adapter = new ShowContactAdapter(getApplicationContext());
        contacts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contacts.setAdapter(adapter);
        loadContacts();
    }

    private void loadContacts() {
        if (!helpers.isConnected(getApplicationContext())) {
            helpers.showError(ShowContacts.this, "ERROR!", "No internet connection found.\nConnect to a network and try again.");
            loading.setVisibility(View.GONE);
            contacts.setVisibility(View.VISIBLE);
            return;
        }

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listener != null)
                    reference.child(user.getId()).removeEventListener(listener);
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Contact contact = d.getValue(Contact.class);
                    if (contact != null) {
                        contactList.add(contact);
                    }
                }
                Log.e("ShowContacts", "Contact size: " + contactList.size());
                Collections.sort(contactList, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact o1, Contact o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                filteredList.addAll(contactList);
                adapter.addContacts(filteredList);
                loading.setVisibility(View.GONE);
                contacts.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (listener != null)
                    reference.child(user.getId()).removeEventListener(listener);
                helpers.showError(ShowContacts.this, "ERROR!", "Something went wrong.\nPlease try again later.");
                loading.setVisibility(View.GONE);
                contacts.setVisibility(View.VISIBLE);
            }
        };

        reference.child(user.getId()).addValueEventListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_contact_menu, menu);

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("Query", "Query: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.e("Query", "Query: " + newText);
                loading.setVisibility(View.VISIBLE);
                contacts.setVisibility(View.GONE);
                Handler handler = new Handler();
                final Runnable ru = new Runnable() {
                    @Override
                    public void run() {
                        filteredList.clear();
                        Log.e("Query", "Query Final: " + newText);
                        for (int i = 0; i < contactList.size(); i++) {
                            if (contactList.get(i).getName().toLowerCase().contains(newText.toLowerCase())) {
                                filteredList.add(contactList.get(i));
                            }
                        }
                        loading.setVisibility(View.GONE);
                        contacts.setVisibility(View.VISIBLE);
                        adapter.addContacts(filteredList);
                    }
                };
                handler.postDelayed(ru, 100);
                return false;
            }
        });
        return true;
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
    protected void onDestroy() {
        super.onDestroy();
        if (listener != null)
            reference.child(user.getId()).removeEventListener(listener);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
