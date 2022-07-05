package com.example.msgserviceapp;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

@SuppressWarnings("ALL")
public class MyService extends Service {

    private SMSreceiver mSMSreceiver;
    private IntentFilter mIntentFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        //SMS event receiver
        mSMSreceiver = new SMSreceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSMSreceiver, mIntentFilter);
        final String TAG = this.getClass().getSimpleName();
        Log.i(TAG, "SERVICE STARTED!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the SMS receiver
        unregisterReceiver(mSMSreceiver);
    }

    private class SMSreceiver extends BroadcastReceiver
    {
        private final String TAG = this.getClass().getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            String strMessage = "";
            if ( extras != null ) {
                String senders = MainActivity.editTextML.getText().toString();
                String number = MainActivity.editText.getText().toString();

                Object[] smsextras = (Object[]) extras.get( "pdus" );
                for(int i=0; i<smsextras.length; i++) {
                    SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);
                    String strMsgBody = smsmsg.getMessageBody().toString();
                    String strMsgSrc = smsmsg.getOriginatingAddress();
                    strMessage += "SMS from " + strMsgSrc + "\n" + strMsgBody;
                    Log.i(TAG, strMessage);
                    Log.i(TAG, strMsgSrc);
                    Log.i(TAG, senders);
                    if(senders.contains(strMsgSrc)) {
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(number, null, strMessage, null, null);
                            Toast.makeText(context, "Message Sent Successfully!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Message Sending Failed! Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
