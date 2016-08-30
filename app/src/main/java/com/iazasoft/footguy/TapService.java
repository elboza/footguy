package com.iazasoft.footguy;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by drugo on 25/8/16.
 */
public class TapService extends Service {
    public static String FOOTGUY_WIDGET_UPDATE = "com.iazasoft.footguy.WIDGET_UPDATE";
    static Handler mHandler;
    private Runnable xTask;

    @Override
    public void onCreate() {
        super.onCreate();

        //Log.d("FOOTGUY","start tap service ...");
        mHandler=new Handler();

        xTask = new Runnable() {

            @Override
            public void run() {
                //Log.d("Handlers", "Calls");
                /** Do something **/
                Intent intent = new Intent(FOOTGUY_WIDGET_UPDATE);
                sendBroadcast(intent);
                mHandler.postDelayed(xTask, 1000);
            }
        };

        mHandler.postDelayed(xTask,1000);

    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        //Log.d("FOOTGUY","stop tap service ...");
        mHandler.removeCallbacks(xTask);


    }
}
