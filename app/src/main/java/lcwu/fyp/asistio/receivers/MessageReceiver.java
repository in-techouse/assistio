package lcwu.fyp.asistio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import lcwu.fyp.asistio.director.Session;
import lcwu.fyp.asistio.model.User;

public class MessageReceiver extends BroadcastReceiver {

    public static final String pdu_type = "pdus";
    public static final String f = "format";
    private Context c;
    private String number, message;
    private Session session;
    private User user;
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
        if (bundle != null){
            Log.e("Message", "Bundle is not null");
            String format = bundle.getString(f);
            Object[] pdus = (Object[]) bundle.get(pdu_type);
            if (pdus != null){
                Log.e("Message", "PDUS is not null");
                boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++){
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

                    // Log and display the SMS message.
                    Log.e("Message", "Message: " + strMessage);
                    Log.e("Message", "From: " + from);
                    number = from;
                    message = strMessage;
                    checkMessage(strMessage, from);
                }
            }
            else{
                Log.e("Message", "PDUS is null");
            }
        }
        else{
            Log.e("Message", "Bundle is null");
        }
    }

    private  void checkMessage(String message , String number) {
        String command = user.getCommand();
       if(command.equals(message)){
          Log.e("Message","Command matched with message");
       }
        else {
            Log.e("Message","Command not matched");
       }
    }
}
