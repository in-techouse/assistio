package lcwu.fyp.asistio.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lcwu.fyp.asistio.R;
import lcwu.fyp.asistio.adapters.SelectedContactsAdapter;
import lcwu.fyp.asistio.model.Contact;

public class SmsSchedular extends AppCompatActivity implements View.OnClickListener {

   // private PasswordDialog dialog;
    private EditText message;
    private Button save;
    private ImageView select_contacts, select_date, select_time;
    private List<Contact> contacts;
    private GridView contactsGrid;
    private SelectedContactsAdapter adapter;
    private TextView date, time;
    private int year, month, day, hour, minute;
    private String str_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_schedular);

        message = findViewById(R.id.message);
        save = findViewById(R.id.save);
        select_contacts = findViewById(R.id.select_contacts);
        select_date = findViewById(R.id.select_date);
        select_time = findViewById(R.id.select_time);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        save.setOnClickListener(this);
        select_contacts.setOnClickListener(this);
        select_time.setOnClickListener(this);
        select_date.setOnClickListener(this);
        contacts = new ArrayList<>();
           adapter = new SelectedContactsAdapter();
        contactsGrid = findViewById(R.id.contactsGrid);
            contactsGrid.setAdapter(adapter);

//            dialog = new PasswordDialog(this);


//            dialog.setDialogResult(new PasswordDialog.DialogResult() {
//                @Override
//                public void finish(boolean result) {
//                    Log.e("Password", "Result: " + result);
//                    if(result){
//                        registerSms();
//                    }
//                    else{
//                        Helpers.showError(SmsScheduler.this, "SMS SCHEDULER", "Your SMS is not scheduled.\nYour password might be incorrect or You cancelled the operation.");
//                    }
//                }
//            });

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
                        Log.e("SmsSender", "Reinitialized");
                    }
                  adapter.setContacts(contacts);
                    Log.e("SmsSender", "List OK with size: " + contacts.size());
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.save:{
                boolean flag = true;
                str_message = message.getText().toString();
                String str_date = date.getText().toString();
                String str_time = time.getText().toString();
                String error = "";
                if(str_message.length() < 2){
                    message.setError("Enter a valid message");
                    flag = false;
                }
                else{
                    message.setError(null);
                }

                if(str_date.length() < 1){
                    flag = false;
                    error = error + "Select SMS Scheduling date.\n";
                }
                if(str_time.length() < 1){
                    flag = false;
                    error = error + "Select SMS Scheduling time.\n";
                }

                if(contacts.size() < 1){
                    flag = false;
                    error = error + "Select some contacts first.\n";
                }

                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd, MMM yyyy hh:mm");
                    Date date = sdf.parse(str_date + " " + str_time);
                    Date current_time = new Date();

                    Log.e("Date", "Message Time: " + date.toString() + " Current Time: " + current_time.toString());
                    if(date.before(current_time)){
                        flag = false;
                        error = error + "Select a valid date and time first.\nYou can't select a time which is passed";
                    }
                }
                catch (Exception e){
                    flag = false;
                    error = error + "Select a valid date and time first.\nYou can't select a time which is passed";
                }
//
//                if(flag){
//                    dialog.show();
//                }
//                else if(error.length() > 1){
//                    Helpers.showError(SmsScheduler.this, "SMS Scheduling Error", error);
//                }
                break;
            }
            case R.id.select_date:{
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog startTime = new DatePickerDialog(SmsSchedular.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                        try{
                            year = y;
                            month = monthOfYear;
                            day = dayOfMonth;
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(y, monthOfYear, dayOfMonth);
                            String d = new SimpleDateFormat("EEEE, dd, MMM yyyy").format(newDate.getTime());
                            date.setText(d);
                        }
                        catch (Exception e){
                            Log.e("Date", "Date parsing exception: " + e.getMessage());
                        }

                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                startTime.show();
                break;
            }
            case R.id.select_time:{
                Calendar mcurrentTime = Calendar.getInstance();
                int h = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int m = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(SmsSchedular.this, new TimePickerDialog.OnTimeSetListener() {
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
                Intent intent = new Intent(SmsSchedular.this, ReadContacts.class);
                startActivityForResult(intent, 20);
                break;
            }
        }
    }

    private void registerSms(){
        try{
            String message = "You scheduled a message for, ";
            ArrayList<String> list = new ArrayList<>();
            for (Contact c : contacts){
                list.add(c.getName() + "," + c.getNumber());
                Log.e("List", "List Size: " + list.size());
                message = message + c.getName() + ", ";
            }

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            String str_date_time = new SimpleDateFormat("EEEE, dd, MMM yyyy HH:mm").format(calendar.getTime());

//            Intent intent = new Intent(SmsScheduler.this, SmsSenderReceiver.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("message", str_message);
//            bundle.putStringArrayList("contacts", list);
//            intent.putExtras(bundle);
//            String number = (year+month)+""+(day+hour)+minute+"";
//            int num = Integer.parseInt(number);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(SmsScheduler.this, num, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//
//            message = message + "at " + str_date_time + ".\nThe message is: " + str_message;
//
//            saveToDatabase(message);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("SmsSender", "Time Exception: " + e.getMessage());
        }
    }

//    private void saveToDatabase(String text){
//        Helpers.sendNotification(getApplicationContext(), "SMS SCHEDULED SUCCESSFULLY", text);
//        HistoryBO history = new HistoryBO();
//        history.setMessage(text);
//        history.setType(2);
//        long id = Helpers.addToDatabase(getApplicationContext(), history);
//        Helpers.showSuccess(SmsScheduler.this, "SMS SCHEDULED", "Your SMS has been scheduled successfully.");
//    }

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
