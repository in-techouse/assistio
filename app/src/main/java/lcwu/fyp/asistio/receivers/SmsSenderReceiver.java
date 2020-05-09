package lcwu.fyp.asistio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

public class SmsSenderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SmsSender", "Broadcast Received");

        if (intent != null) {
            Log.e("SmsSender", "From Intent");
            // Get Message
            String message = intent.getStringExtra("message");
            if (message != null) {

                ArrayList<String> contacts = intent.getStringArrayListExtra("contacts");
                if (contacts != null) {
                    Log.e("SmsSender", "Contacts is Not Null, size is: " + contacts.size());
                    for (String s : contacts) {
                        Log.e("SmsSender", "Contact is: " + s);
                        String[] arr = s.split(",");
                        sendSMS(context, arr[1], arr[0], message);
                    }
                } else {
                    Log.e("SmsSender", "Contacts is Null");
                }
            } else {
                Log.e("SmsSender", "Message is Null");
            }
        } else {
            Log.e("SmsSender", "Intent is Null");
        }
    }

    public void sendSMS(Context context, String phoneNo, String name, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
