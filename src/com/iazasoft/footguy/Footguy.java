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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.*;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.R.color;
import android.graphics.Color;

public class Footguy extends AppWidgetProvider {
	private static final String LOG_TAG = "footguy";
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");
	public static String FOOTGUY_WIDGET_UPDATE = "com.iazasoft.footguy.WIDGET_UPDATE";
	public static String FOOTGUY_WIDGET_PREFS = "com.iazasoft.footguy.WIDGET_PREFS";
	public static String FOOTGUY_SHOW_UPTIME = "com.iazasoft.footguy.SHOW_UPTIME";
	public static String FOOTGUY_EDIT_PREFS="com.iazasoft.footguy.EDIT_PREFS";
	public static AlarmManager alarmManager;
	AlarmManager alarmManager2;
	public static CharSequence footguy_cs[] = { " oo\n<!>\n_!/", " oo\n<!>\n_!_" ,"=^_^=",};
	static int x=0,tap=0;
	
	public static class UpdateService extends Service {
		int fontsize=12;
		boolean bBorder,bBackground;
        @Override
        public void onStart(Intent intent, int startId) {
        	//Toast.makeText(this, "service",Toast.LENGTH_SHORT).show();
        	//Log.d(LOG_TAG,intent.getAction().toString());
        	if(FOOTGUY_WIDGET_UPDATE.equals(intent.getAction())){
        		//Log.d(LOG_TAG,"update action");
        		//Toast.makeText(this, "update service",Toast.LENGTH_SHORT).show();
        		footTap(this);
        	} 
        	else if(FOOTGUY_WIDGET_PREFS.equals(intent.getAction())){
        			//Log.d(LOG_TAG,"prefs action");
        			//Toast.makeText(this, "prefs service",Toast.LENGTH_SHORT).show();
        			LoadPreferences(this);
        	}
        	else if(FOOTGUY_SHOW_UPTIME.equals(intent.getAction())){
        		show_uptime(this);
        	}
        	stopSelf();
        }

		@Override
		public IBinder onBind(Intent intent) {
			// TODO Auto-generated method stub
			return null;
		}
		private void footTap(Context context){
			RemoteViews views = new RemoteViews(context.getPackageName(),   R.layout.footguy);
            //views.setTextViewText(R.id.textView1, currentTime);
            int n=0;
            //views.setTextViewText(R.id.textView1, Integer.toString(x));
            if (x % 2 == 0)
            {
                    n=0;
            }
            else
            {
                    n=1;
            }
            x++;
<<<<<<< HEAD
            //Log.d("FootguyAPP","tap");
=======
>>>>>>> c831e843652dc6ca2a62322dace8c60f2f1a5ed9
            if(x==120) n=2;
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
            //Intent intent = new Intent(context, Prefs.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
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
            
            //Calendar calendar = Calendar.getInstance();
            //calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            //calendar.setTimeInMillis(SystemClock.elapsedRealtime());
            long time=(SystemClock.elapsedRealtime())/1000;
            int hh=Math.round(time/3600);
            int mm=Math.round((time/60)%60);
            int ss=Math.round(time%60);
            //int h=calendar.get(Calendar.HOUR);
            //int m=calendar.get(Calendar.MINUTE);
            //int s=calendar.get(Calendar.SECOND);
            //String uptime=String.format("droid-uptime: %02dh %02dm %02ds",h,m,s);
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
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
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
	}
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(createClockTickIntent(context)); 
	}

	@Override 
	public void onEnabled(Context context) {
		super.onEnabled(context);
		//Toast.makeText(context, "onEnabled",Toast.LENGTH_SHORT).show();
		Intent updateintent=new Intent(FOOTGUY_WIDGET_PREFS);
		//updateintent.setAction(FOOTGUY_WIDGET_PREFS);
		//context.startService(updateintent);
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager2 = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, updateintent, PendingIntent.FLAG_UPDATE_CURRENT);
		//Calendar calendar2 = Calendar.getInstance();
        //calendar2.setTimeInMillis(System.currentTimeMillis());
        //calendar2.add(Calendar.SECOND, 30);
        alarmManager2.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+1000*60, pendingIntent);
        
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime()+1000*70, 1000, createClockTickIntent(context));
	}
	private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(FOOTGUY_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
        }

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		//Toast.makeText(context, "onUpdate",Toast.LENGTH_SHORT).show();
		Intent updateintent=new Intent(context,UpdateService.class);
		updateintent.setAction(FOOTGUY_WIDGET_PREFS);
		context.startService(updateintent);
		
		if(alarmManager==null) {onEnabled(context);}
	}

}
