package lcwu.fyp.asistio.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.SelectedContactsAdapter;
import lcwu.fyp.asistio.director.Helpers;
import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.Contact;
import lcwu.fyp.asistio.model.MesssageScheduler;
import lcwu.fyp.asistio.model.User;
import lcwu.fyp.asistio.receivers.SmsSenderReceiver;

public class SmsScheduler extends AppCompatActivity implements View.OnClickListener {
    private EditText message;
    private List<Contact> contacts;
    private SelectedContactsAdapter adapter;
    private TextView date, time;
    private int year, month, day, hour, minute;
    private Helpers helpers;
    private String str_message;
    private Button save;
    private ProgressBar progress;
    private Session session;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_schedular);

        helpers = new Helpers();
        session = new Session(getApplicationContext());
        user = session.getUser();

        message = findViewById(R.id.message);
        save = findViewById(R.id.save);
        ImageView select_contacts = findViewById(R.id.select_contacts);
        ImageView select_date = findViewById(R.id.select_date);
        ImageView select_time = findViewById(R.id.select_time);
        date = findViewById(R.id.date);
        progress = findViewById(R.id.progress);
        time = findViewById(R.id.time);
        save.setOnClickListener(this);
        select_contacts.setOnClickListener(this);
        select_time.setOnClickListener(this);
        select_date.setOnClickListener(this);
        contacts = new ArrayList<>();
        adapter = new SelectedContactsAdapter();
        GridView contactsGrid = findViewById(R.id.contactsGrid);
        contactsGrid.setAdapter(adapter);
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
                        Log.e("Scheduler", "Reinitialized");
                    }
                    adapter.setContacts(contacts);
                    Log.e("Scheduler", "List OK with size: " + contacts.size());
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.save: {
                if (!helpers.isConnected(getApplicationContext())) {
                    helpers.showError(SmsScheduler.this, "ERROR", "No internet connection found.\nConnect to a network and try again.");
                    return;
                }

                boolean flag = true;
                str_message = message.getText().toString();
                String str_date = date.getText().toString();
                String str_time = time.getText().toString();
                String error = "";
                if (str_message.length() < 2) {
                    message.setError("Enter a valid message");
                    flag = false;
                } else {
                    message.setError(null);
                }

                if (str_date.length() < 1) {
                    flag = false;
                    error = error + "Select SMS Scheduling date.\n";
                }
                if (str_time.length() < 1) {
                    flag = false;
                    error = error + "Select SMS Scheduling time.\n";
                }

                if (contacts.size() < 1) {
                    flag = false;
                    error = error + "Select some contacts first.\n";
                }

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd, MMM yyyy hh:mm");
                    Date date = sdf.parse(str_date + " " + str_time);
                    Date current_time = new Date();

                    Log.e("Date", "Message Time: " + date.toString() + " Current Time: " + current_time.toString());
                    if (date.before(current_time)) {
                        flag = false;
                        error = error + "Select a valid date and time first.\nYou can't select a time which is passed";
                    }
                } catch (Exception e) {
                    flag = false;
                    error = error + "Select a valid date and time first.\nYou can't select a time which is passed";
                }
                if (flag) {
                    registerSms();
                } else if (error.length() > 1) {
                    helpers.showError(SmsScheduler.this, "SMS Scheduling Error", error);
                }
                break;
            }
            case R.id.select_date: {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog startTime = new DatePickerDialog(SmsScheduler.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                        try {
                            year = y;
                            month = monthOfYear;
                            day = dayOfMonth;
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(y, monthOfYear, dayOfMonth);
                            String d = new SimpleDateFormat("EEEE, dd, MMM yyyy").format(newDate.getTime());
                            date.setText(d);
                        } catch (Exception e) {
                            Log.e("Date", "Date parsing exception: " + e.getMessage());
                        }

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                startTime.show();
                break;
            }
            case R.id.select_time: {
                Calendar mcurrentTime = Calendar.getInstance();
                int h = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int m = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(SmsScheduler.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        time.setText(selectedHour + ": " + selectedMinute);
                    }
                }, h, m, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
            }
            case R.id.select_contacts: {
                Intent intent = new Intent(SmsScheduler.this, ReadContacts.class);
                startActivityForResult(intent, 20);
                break;
            }
        }
    }

    private void registerSms() {
        try {
            ArrayList<String> list = new ArrayList<>();
            for (Contact c : contacts) {
                list.add(c.getName() + "," + c.getNumber());
                Log.e("List", "List Size: " + list.size());
            }

            MesssageScheduler messsageScheduler = new MesssageScheduler();
            messsageScheduler.setMessage(str_message);
            messsageScheduler.setContactList(contacts);

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            String str_date_time = new SimpleDateFormat("EEEE, dd, MMM yyyy HH:mm").format(calendar.getTime());

            messsageScheduler.setTime(str_date_time);

            Intent intent = new Intent(SmsScheduler.this, SmsSenderReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putString("message", str_message);
            bundle.putStringArrayList("contacts", list);
            intent.putExtras(bundle);
            String number = (year + month) + "" + (day + hour) + minute + "";
            int num = Integer.parseInt(number);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(SmsScheduler.this, num, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Log.e("Scheduler", "Message: " + str_message);
            Log.e("Scheduler", "Date & Time: " + str_date_time);
            Log.e("Scheduler", "Contacts: " + contacts.size());
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                saveToDatabase(messsageScheduler);
            }

        } catch (Exception e) {
            e.printStackTrace();
            helpers.showError(SmsScheduler.this, "ERROR!", "Something went wrong.\nPlease try again later");
            Log.e("Scheduler", "Time Exception: " + e.getMessage());
        }
    }

    private void saveToDatabase(MesssageScheduler scheduler) {
        progress.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("SmsScheduler");
        String schedulerId = reference.child(user.getId()).push().getKey(); // Will return a unique id.
        scheduler.setId(schedulerId);
        reference.child(user.getId()).child(scheduler.getId()).setValue(scheduler)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progress.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);
                        helpers.showSuccess(SmsScheduler.this, "", "SMS Scheduler preferences have been saved successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.setVisibility(View.GONE);
                        save.setVisibility(View.VISIBLE);
                        helpers.showError(SmsScheduler.this, "ERROR", "Something went wrong.\nPlease try again later.");
                    }
                });
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
                Intent it = new Intent(SmsScheduler.this, SmsSchedulerHistory.class);
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
