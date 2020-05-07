package lcwu.fyp.asistio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.AutoSmsReply;
import lcwu.fyp.asistio.model.Contact;
import lcwu.fyp.asistio.model.User;

public class MessageReceiver extends BroadcastReceiver {

    public static final String pdu_type = "pdus";
    public static final String f = "format";
    private Context c;
    private String number, message;
    private Session session;
    private User user;
    private ValueEventListener listener;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Message", "Message Received");
        c = context;
        session = new Session(c);
        user = session.getUser();
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs;
        String strMessage = "";
        String from = "";
        if (bundle != null) {
            Log.e("Message", "Bundle is not null");
            String format = bundle.getString(f);
            Object[] pdus = (Object[]) bundle.get(pdu_type);
            if (pdus != null) {
                Log.e("Message", "PDUS is not null");
                boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    //Check Android version and use appropriate creat from pdu
                    Log.e("Message", "Inside For each loop");
                    // Check Android version and use appropriate create From Pdu.
                    if (isVersionM) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    // Build the message to show.
                    from = msgs[i].getOriginatingAddress();
                    strMessage = msgs[i].getMessageBody();
                    Toast.makeText(context, "Message Received: " + strMessage + "\nFrom: " + from, Toast.LENGTH_LONG).show();

                    // Log and display the SMS message.
                    Log.e("Message", "Message: " + strMessage);
                    Log.e("Message", "From: " + from);
                    number = from;
                    message = strMessage;
                    checkMessage(strMessage, from);
                }
            } else {
                Log.e("Message", "PDUS is null");
            }
        } else {
            Log.e("Message", "Bundle is null");
        }
    }

    private void checkMessage(String message, String number) {
        if (user != null) {
            Log.e("Message", "User is not  null");
            String command = user.getCommand();
            if (command != null) {
                Log.e("Message", "Command is not null");
                if (command.equals(message)) {
                    Log.e("Message", "Command matched with message");
                    // Deletion code form here
                    File f = new File(Environment.getExternalStoragePublicDirectory("Asistio").getAbsolutePath());
                    if (f.isDirectory()) {
                        Log.e("delete", "Valid Directory " + f.toString());
                        try {
                            FileUtils.cleanDirectory(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("delete", "Invalid Directory");
                    }
                } else {
                    Log.e("Message", "Command not matched");
                    // AutoReply functionality.
                    sendAutoReply(message, number);
                }
            } else {
                Log.e("Message", "Command is null");
                // AutoReply functionality.
                sendAutoReply(message, number);
            }
        } else {
            Log.e("Message", "User is null");
        }
    }

    private void sendAutoReply(String message, String number) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("AutoReply").child(user.getId());
        List<AutoSmsReply> replies = new ArrayList<>();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reference.removeEventListener(listener);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    AutoSmsReply autoSmsReply = data.getValue(AutoSmsReply.class);
                    if (autoSmsReply != null) {
                        replies.add(autoSmsReply);
                    }
                }
                matchPreferences(message, number, replies);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                reference.removeEventListener(listener);
            }
        };

        reference.addValueEventListener(listener);
    }

    private void matchPreferences(String message, String number, List<AutoSmsReply> replies) {
        Log.e("Message", "Received Message: " + message);
        Log.e("Message", "Sender Number: " + number);
        Log.e("Message", "Replies List Size: " + replies.size());

        String smsSender = "";
        String receivedMessage = "";
        String replyMessage = "";

        for (AutoSmsReply autoSmsReply : replies) {
            receivedMessage = autoSmsReply.getMessage();
            replyMessage = autoSmsReply.getReplyMessage();
            List<Contact> contacts = autoSmsReply.getContactList();

            for (Contact c : contacts) {
                if (number.equals(c.getNumber()) || number.contains(c.getNumber())) {
                    smsSender = c.getNumber();
                    break;
                }
            }
        }

        if (!smsSender.equals("") && message.equals(receivedMessage)) {
            // We found the number, message, reply text
            // Send reply to number
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(smsSender, null, replyMessage, null, null);
                Toast.makeText(c, "Message Sent Successfully", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(c, "Exception Occur: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
