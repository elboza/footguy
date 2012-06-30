package com.iazasoft.footguy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import android.R.color;
import android.graphics.Color;

public class Footguy extends AppWidgetProvider {
	private static final String LOG_TAG = "footguy";
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");
	public static String FOOTGUY_WIDGET_UPDATE = "com.iazasoft.footguy.WIDGET_UPDATE";
	public static String FOOTGUY_WIDGET_PREFS = "com.iazasoft.footguy.WIDGET_PREFS";
	public static String FOOTGUY_EDIT_PREFS="com.iazasoft.footguy.EDIT_PREFS";
	public static AlarmManager alarmManager;
	public static CharSequence footguy_cs[] = { " oo\n<!>\n_!/", " oo\n<!>\n_!_" ,"=^_^=",};
	static int x=0;
	
	public static class UpdateService extends Service {
		int fontsize=12;
		boolean bBorder,bBackground;
        @Override
        public void onStart(Intent intent, int startId) {
        	//Toast.makeText(this, "service",Toast.LENGTH_SHORT).show();
        	if(FOOTGUY_WIDGET_UPDATE.equals(intent.getAction())){
        		Log.d(LOG_TAG,"update action");
        		//Toast.makeText(this, "update service",Toast.LENGTH_SHORT).show();
        		footTap(this);
        	} 
        	else if(FOOTGUY_WIDGET_PREFS.equals(intent.getAction())){
        			Log.d(LOG_TAG,"prefs action");
        			//Toast.makeText(this, "prefs service",Toast.LENGTH_SHORT).show();
        			LoadPreferences(this);
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
            if(x==120) n=2;
            if(x>121) x=0;
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
            Intent intent = new Intent(context, Prefs.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.button, pendingIntent);
            ComponentName thisWidget = new ComponentName(this, Footguy.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, views);
            //manager.updateAppWidget(context, manager, thisWidget);
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
				(FOOTGUY_WIDGET_PREFS.equals(intent.getAction()))) {
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
		Intent updateintent=new Intent(context,UpdateService.class);
		updateintent.setAction(FOOTGUY_WIDGET_PREFS);
		context.startService(updateintent);
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000, createClockTickIntent(context));
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
