package com.iazasoft.footguy;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by drugo on 25/8/16.
 */
public class ScreenListenerService extends Service {
    BroadcastReceiver mReceiver=null;

    @Override
    public void onCreate() {
        super.onCreate();

        // Toast.makeText(getBaseContext(), "Service on create", Toast.LENGTH_SHORT).show();

        // Register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenBroadcastReceiver();
        registerReceiver(mReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {

        boolean screenOn = false;
        final String SCREEN_ON="com.iazasoft.footguy.WAKEUP";
        final String SCREEN_OFF="com.iazasoft.footguy.SLEEP";
        String action;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){}

        //  Toast.makeText(getBaseContext(), "Service on start :"+screenOn,
        //Toast.LENGTH_SHORT).show();

        if (!screenOn) {

            // your code here
            // Some time required to start any service
            //Toast.makeText(getBaseContext(), "Screen on, ", Toast.LENGTH_SHORT).show();
            //Log.d("FOOTGUY","screen on ...");
            action=SCREEN_ON;

        } else {

            // your code here
            // Some time required to stop any service to save battery consumption
            //Toast.makeText(getBaseContext(), "Screen off,", Toast.LENGTH_SHORT).show();
            //Log.d("FOOTGUY","screen off ...");
            action=SCREEN_OFF;
        }
        Intent intent_to_send = new Intent();
        intent_to_send.setAction(action);
        sendBroadcast(intent_to_send);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {

        Log.i("ScreenListener", "Service  distroy");
        if(mReceiver!=null)
            unregisterReceiver(mReceiver);

    }
}
