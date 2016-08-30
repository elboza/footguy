/*****************************************************************
 * 
 * 	Footguy for Android
 * 
 * by Fernando Iazeolla
 * 
 * this code is distributed under GPLv2 licence.
 * 
 * 
 *****************************************************************/
package com.iazasoft.footguy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.*;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.os.IBinder;
import android.os.SystemClock;
import android.graphics.Color;
import android.os.Handler;

public class Footguy extends AppWidgetProvider {
	private static final String LOG_TAG = "footguy";
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");
	public static String FOOTGUY_WIDGET_UPDATE = "com.iazasoft.footguy.WIDGET_UPDATE";
	public static String FOOTGUY_WIDGET_PREFS = "com.iazasoft.footguy.WIDGET_PREFS";
	public static String FOOTGUY_SHOW_UPTIME = "com.iazasoft.footguy.SHOW_UPTIME";
	public static String FOOTGUY_EDIT_PREFS="com.iazasoft.footguy.EDIT_PREFS";
	public static String FOOTGUY_WAKEUP="com.iazasoft.footguy.WAKEUP";
	public static String FOOTGUY_SLEEP="com.iazasoft.footguy.SLEEP";
	public static AlarmManager alarmManager;
	AlarmManager alarmManager2;
	public static CharSequence footguy_cs[] = { " oo\n<!>\n_!/", " oo\n<!>\n_!_" ,"=^_^="," o-\n<!>\n_!/"};
	static int x=0,tap=0;
	static Handler mHandler;
	static Context mContext;
	private Runnable xTask;
	private static boolean awake=false;
	
	public static class UpdateService extends Service {
		int fontsize=12;
		boolean bBorder,bBackground;
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
        	if(FOOTGUY_WIDGET_UPDATE.equals(intent.getAction())){
        		footTap(this);
        	} 
        	else if(FOOTGUY_WIDGET_PREFS.equals(intent.getAction())){
        			LoadPreferences(this);
        	}
        	else if(FOOTGUY_SHOW_UPTIME.equals(intent.getAction())){
        		show_uptime(this);
        	}
        	stopSelf();
			return START_STICKY;
        }

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		private void footTap(Context context){
			RemoteViews views = new RemoteViews(context.getPackageName(),   R.layout.footguy);
            int n=0;
            if (x % 2 == 0)
            {
                    n=0;
            }
            else
            {
                    n=1;
            }
            x++;
            //Log.d("FootguyAPP","tap");
            if(x==120) n=3;
            if(x>121) x=0;
            if (tap>0) tap++;
            if(tap==2){
            	tap=0;
            	Intent intent = new Intent(FOOTGUY_SHOW_UPTIME);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.button, pendingIntent);
            }
            views.setTextViewText(R.id.textView1, footguy_cs[n]);
            ComponentName thisWidget = new ComponentName(this, Footguy.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);
		}
		private void LoadPreferences(Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences(FOOTGUY_EDIT_PREFS, 0);
            bBorder = sharedPreferences.getBoolean("ckBorder", true);
            bBackground = sharedPreferences.getBoolean("ckBackground",true);
            String footguyColor=sharedPreferences.getString("FootguyColor","white");
            fontsize=sharedPreferences.getInt("myFontSize",12);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.footguy);
            if(bBackground){
                    if(bBorder){
                            views.setImageViewResource(R.id.imageView1, R.drawable.border);
                    }else{
                            views.setImageViewResource(R.id.imageView1, R.drawable.background);
                    }
            }else
            {
                    views.setImageViewResource(R.id.imageView1, R.drawable.trasparent);
            }
            views.setTextColor(R.id.textView1,mparseColor(footguyColor));
            views.setFloat(R.id.textView1, "setTextSize", fontsize);
            tap=0;
            Intent intent = new Intent(FOOTGUY_SHOW_UPTIME);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);
            ComponentName thisWidget = new ComponentName(this, Footguy.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);
            //manager.updateAppWidget(context, manager, thisWidget);
        }
		private void show_uptime(Context context){
			tap++;
			Intent intent = new Intent(context, Prefs.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.footguy);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);
            ComponentName thisWidget = new ComponentName(this, Footguy.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views); 

            long time=(SystemClock.elapsedRealtime())/1000;
            int hh=Math.round(time/3600);
            int mm=Math.round((time/60)%60);
            int ss=Math.round(time%60);
            String uptime=String.format("droid-uptime: %02dh %02dm %02ds",hh,mm,ss);
			Toast.makeText(context, uptime,Toast.LENGTH_SHORT).show();
		}
        private int mparseColor(String s){
    		if(s.equals("white")) return Color.WHITE;
    		else if(s.equals("yellow")) return Color.YELLOW;
    		else if(s.equals("lt gray")) return Color.LTGRAY;
    		else if(s.equals("red")) return Color.RED;
    		else if(s.equals("green")) return Color.GREEN;
    		else if(s.equals("blue")) return Color.BLUE;
    		else if(s.equals("cyan")) return Color.CYAN;
    		else if(s.equals("magenta")) return Color.MAGENTA;
    		else if(s.equals("gray")) return Color.GRAY;
    		else if(s.equals("black")) return Color.BLACK;
    		else return Color.BLACK;
    	}
	}


	private void wakeup(Context c){

		Intent i0 = new Intent(c,TapService.class);
		c.startService(i0);
	}

	private void sleep(Context c){
		Intent i0 = new Intent(c,TapService.class);
		c.stopService(i0);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		//Log.d("FOOTGUY","ricevuto intent...");
		if ((FOOTGUY_WIDGET_UPDATE.equals(intent.getAction()))
				||
				(FOOTGUY_WIDGET_PREFS.equals(intent.getAction()))
				||
				(FOOTGUY_SHOW_UPTIME.equals(intent.getAction()))
				) {
			Intent updateintent=new Intent(context,UpdateService.class);
			updateintent.setAction(intent.getAction());
			context.startService(updateintent);
		}
		if(FOOTGUY_WAKEUP.equals(intent.getAction())){
			//Log.d("FOOTGUY","wakeup ...");
			//if(awake) Log.d("awake","true");
			//if(!awake) Log.d("awake","false");
			if(!awake) wakeup(context);
			awake=true;
		}
		if(FOOTGUY_SLEEP.equals(intent.getAction())){
			//Log.d("FOOTGUY","sleep ...");
			//if(awake) Log.d("awake","true");
			//if(!awake) Log.d("awake","false");
			if(awake) sleep(context);
			awake=false;
		}
	}
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		//mHandler.removeCallbacks(xTask);
		sleep(context);
		Intent i0 = new Intent(context,ScreenListenerService.class);
		context.stopService(i0);
	}

	@Override 
	public void onEnabled(Context context) {
		super.onEnabled(context);
		awake=false;
		mContext=context;
		Toast.makeText(context, "onEnabled",Toast.LENGTH_SHORT).show();
		Intent i1 = new Intent(context,UpdateService.class);
		context.startService(i1);
		Intent updateintent=new Intent(FOOTGUY_WIDGET_PREFS);
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateintent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager2.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+1000*60, pendingIntent);
        

        //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+1000*10, 1000, createClockTickIntent(context));

		wakeup(context);
		Intent i0 = new Intent(context,ScreenListenerService.class);
		context.startService(i0);
	}

	private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(FOOTGUY_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
        }

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Intent updateintent=new Intent(context,UpdateService.class);
		updateintent.setAction(FOOTGUY_WIDGET_PREFS);
		context.startService(updateintent);
		
		if(alarmManager==null) {onEnabled(context);}
	}

}
