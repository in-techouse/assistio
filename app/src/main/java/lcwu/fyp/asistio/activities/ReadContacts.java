package lcwu.fyp.asistio.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.ContactAdapter;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.model.Contact;

public class ReadContacts extends AppCompatActivity implements View.OnClickListener{

    private List<Contact> contactList, filteredList;
    private RecyclerView contacts;
    private ProgressBar progress;
    private ContactAdapter adapter;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_contacts);

        contacts = findViewById(R.id.contacts);
        progress = findViewById(R.id.progress);
        adapter = new ContactAdapter(getApplicationContext());
        done = findViewById(R.id.done);
        done.setOnClickListener(this);

        contacts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        contacts.setAdapter(adapter);
        contactList = new ArrayList<>();
        filteredList = new ArrayList<>();

        new FetchContacts().execute();
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
    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_sms_finder, menu);
//
//        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
//
//        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
//
//
//        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.e("Query", "Query: " + query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(final String newText) {
//                Log.e("Query", "Query: " + newText);
//                progress.setVisibility(View.VISIBLE);
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        filteredList.clear();
//                        Log.e("Query", "Query Final: " + newText);
//                        for (int i=0; i< contactList.size(); i++){
//                            if(contactList.get(i).getName().toLowerCase().contains(newText.toLowerCase())){
//                                filteredList.add(contactList.get(i));
//                            }
//                        }
//                        progress.setVisibility(View.GONE);
//                        adapter.addContacts(filteredList);
//                    }
//                }.run();
//                return false;
//            }
//        });
        return true;
    }

//    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.done:{
                List<Contact> contacts = adapter.getContacts();
                List<Contact> selected = new ArrayList<>();

                for (Contact c: contacts){
                    if(c.isSelected()){
                        selected.add(c);
                    }
                }

                if(selected.size() > 0){
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("contacts", (Serializable) selected);
                    resultIntent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
                else{
//                    Helpers.showError(ReadContacts.this, "Contact Error", "Select some contacts first.");
                }

                break;
            }
        }
    }

    class FetchContacts extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contacts.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            List<Contact> list = new ArrayList<>();

            try{
                ContentResolver cr = getContentResolver(); //Activity/Application android.content.Context
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if(cursor != null && cursor.moveToFirst())
                {
                    list = new ArrayList<>();
                    Set<String> set = new HashSet<>();
                    do
                    {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                            if(pCur != null){
                                while (pCur.moveToNext())
                                {
                                    String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                                    String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    String str = contactName+","+contactNumber;
                                    set.add(str);
                                    Log.e("Contacts", "Contact Name: " + contactName + " Contact Number: " + contactNumber);
                                    break;
                                }
                                pCur.close();
                            }
                            else{
                                list = null;
                                Log.e("Contacts", "PCursor is Null");
                            }

                        }

                    } while (cursor.moveToNext());
                    list = new ArrayList<>();
                    for (String str: set){
                        String[] s = str.split(",");
                        Contact contact = new Contact(s[0], s[1], false);
                        list.add(contact);
                    }
                    cursor.close();

                    Collections.sort(list, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact o1, Contact o2) {
                            return o1.getName().compareToIgnoreCase(o2.getName());
                        }
                    });
                }
                else{
                    list = null;
                    Log.e("Contacts", "Cursor is Null");
                }
            }
            catch (Exception e){
                list = null;
                Log.e("Contacts", "Exception: " + e.getMessage());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Contact> contactBOS) {
            super.onPostExecute(contactBOS);

            if(contactBOS != null){
                contactList.addAll(contactBOS);
                filteredList.addAll(contactBOS);
                adapter.addContacts(filteredList);
            }
            contacts.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }
}
