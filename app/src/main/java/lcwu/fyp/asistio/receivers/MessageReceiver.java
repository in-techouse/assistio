package lcwu.fyp.asistio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
        if(user != null){
            Log.e("Message", "User is not  null");
            String command = user.getCommand();
            if(command != null){
                Log.e("Message", "Command is not  null");
                if(command.equals(message)){
                    Log.e("Message","Command matched with message");
                    //Deletion code form here
                    File f = new File(Environment.getExternalStoragePublicDirectory("Asistio").getAbsolutePath());
                    if(f.isDirectory()){
                        Log.e("delete" , "Valid Directory "+f.toString());
                        try {
                            FileUtils.cleanDirectory(f);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Log.e("delete" , "Invalid Directory");
                    }
                }
                else {
                    Log.e("Message","Command not matched");
                }
            }
            else{
                Log.e("Message", "Command is null");
            }
        }
        else{
            Log.e("Message", "User is null");
        }
    }
}
