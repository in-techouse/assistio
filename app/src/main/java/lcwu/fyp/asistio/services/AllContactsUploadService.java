package lcwu.fyp.asistio.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.Contact;
import lcwu.fyp.asistio.model.User;

public class AllContactsUploadService extends Service {
    private HashMap<String, Contact> map = new HashMap<>();
    private User user;
    private ValueEventListener listener;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Session session = new Session(this);
        user = session.getUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Contacts").child(user.getId());
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (listener != null)
                    reference.removeEventListener(listener);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Contact contact = data.getValue(Contact.class);
                    if (contact != null) {
                        map.put(data.getKey(), contact);
                    }
                }
                Log.e("ContactService", "Contacts are: " + map.size());
                new ScanContacts().execute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                if (listener != null)
                    reference.removeEventListener(listener);
            }
        };
        reference.addValueEventListener(listener);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    class ScanContacts extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            List<Contact> list = new ArrayList<>();

            try {
                ContentResolver cr = getContentResolver(); //Activity/Application android.content.Context
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    list = new ArrayList<>();
                    Set<String> set = new HashSet<>();
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (pCur != null) {
                                while (pCur.moveToNext()) {
                                    String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                                    String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    String str = contactName + "," + contactNumber;
                                    set.add(str);
                                    Log.e("Contacts", "Contact Name: " + contactName + " Contact Number: " + contactNumber);
                                    break;
                                }
                                pCur.close();
                            } else {
                                list = null;
                                Log.e("Contacts", "PCursor is Null");
                            }

                        }

                    } while (cursor.moveToNext());
                    list = new ArrayList<>();
                    for (String str : set) {
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
                } else {
                    list = null;
                    Log.e("Contacts", "Cursor is Null");
                }
            } catch (Exception e) {
                list = null;
                Log.e("Contacts", "Exception: " + e.getMessage());
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
            super.onPostExecute(contacts);
            if (contacts != null) {
                for (Contact c : contacts) {
                    String id = c.getNumber();
                    id = id.replace(" ", "");
                    id = id.replace("/", "");
                    id = id.replace(".", "");
                    id = id.replace("#", "");
                    id = id.replace("$", "");
                    id = id.replace("[", "");
                    id = id.replace("]", "");
                    map.put(id, c);
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Contacts").child(user.getId());
                reference.setValue(map);
            }
        }
    }
}
