package com.iazasoft.footguy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.*;
import android.util.Log;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.R.color;
import android.graphics.Color;

public class Footguy extends AppWidgetProvider {
	private static final String LOG_TAG = "footguy: ";
	private static final DateFormat df = new SimpleDateFormat("hh:mm:ss");

	/**
	 * Custom Intent name that is used by the AlarmManager to tell us to update the clock once per second.
	 */
	public static String FOOTGUY_WIDGET_UPDATE = "com.iazasoft.footguy.WIDGET_UPDATE";
	public static String FOOTGUY_WIDGET_PREFS = "com.iazasoft.footguy.WIDGET_PREFS";
	public static String FOOTGUY_EDIT_PREFS="com.iazasoft.footguy.EDIT_PREFS";
	AlarmManager alarmManager;
	public static CharSequence footguy_cs[] = { " oo\n<!>\n_!/", " oo\n<!>\n_!_" ,"=^_^=",};
	static int x=0;
	int fontsize=12;
	boolean bBorder,bBackground;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (FOOTGUY_WIDGET_UPDATE.equals(intent.getAction())) {
			//Log.d(LOG_TAG, "Clock update");
			// Get the widget manager and ids for this widget provider, then call the shared
			// clock update method.
			
			ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
		    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		    int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
		    for (int appWidgetID: ids) {
				updateAppWidget(context, appWidgetManager, appWidgetID);

		    }
		}
		else if(FOOTGUY_WIDGET_PREFS.equals(intent.getAction())){
			Log.d("FFF","PREFS MESSAGE");
				LoadPreferences(context);
		    }
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
	private void LoadPreferences(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(FOOTGUY_EDIT_PREFS, 0);
		bBorder = sharedPreferences.getBoolean("ckBorder", true);
		bBackground = sharedPreferences.getBoolean("ckBackground",true);
		String footguyColor=sharedPreferences.getString("FootguyColor","white");
		fontsize=sharedPreferences.getInt("myFontSize",12);
		ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
	    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
	    int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
	    for (int appWidgetID: ids) {
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
	    	appWidgetManager.updateAppWidget(appWidgetID, views);
	    	updateAppWidget(context, appWidgetManager, appWidgetID);
	    }
	}
	private PendingIntent createClockTickIntent(Context context) {
        Intent intent = new Intent(FOOTGUY_WIDGET_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
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
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000, createClockTickIntent(context));
	}


	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;
		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			Intent intent = new Intent(context, Prefs.class);
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			// Create an Intent to launch ExampleActivity
			//Intent intent = new Intent(context, WidgetExampleActivity.class);
			//PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	intent, 0);
			if(alarmManager==null) {onEnabled(context);}
			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.footguy);
			LoadPreferences(context);
			//views.setOnClickPendingIntent(R.id.button, pendingIntent);
			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			views.setOnClickPendingIntent(R.id.button, pendingIntent);
			appWidgetManager.updateAppWidget(appWidgetId, views);


			// Update The clock label using a shared method
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	public static void updateAppWidget(Context context,	AppWidgetManager appWidgetManager, int appWidgetId) {
		String currentTime =  df.format(new Date());
		RemoteViews views = new RemoteViews(context.getPackageName(),	R.layout.footguy);
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
		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

}
