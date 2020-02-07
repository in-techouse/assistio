package lcwu.fyp.asistio.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MessageReceiver extends BroadcastReceiver {

    public static final String pdu_type ="pdu";
    public static final String f = "format";
    private Context c;
    private String number, message;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Message", "Message Received");
        c = context;
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs;
        String strMessage = "";
        String from = "";
        if (bundle != null){
            String format = bundle.getString(f);
            Object[]pdus = (Object[]) bundle.get(pdu_type);

            if (pdus != null){
                boolean isVersionM=(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++){
                    //Check Android version and use appropriate creat from pdu

                    if (isVersionM) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    //Build the message to Show
                    from = msgs[i].getOriginatingAddress();
                    strMessage = msgs[i].getMessageBody();

                    //Log and display the SMS message
                    Log.e("Message", "Message:" +strMessage);
                    Log.e("Message","From:" +from);
                    number = from;
                    message = strMessage;
                    checkMessage(strMessage , from);
                }
            }
        }
    }

    private  void checkMessage(String message , String number) {
    }
}
