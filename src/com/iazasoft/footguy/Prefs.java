package com.iazasoft.footguy;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Bundle;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.*;
import android.content.*;
import android.graphics.Color;
import android.util.Log;
import android.text.Html;

public class Prefs extends Activity {
	public static String FOOTGUY_WIDGET_PREFS = "com.iazasoft.footguy.WIDGET_PREFS";
	public static String FOOTGUY_EDIT_PREFS="com.iazasoft.footguy.EDIT_PREFS";
	CheckBox ckBorder,ckBackground;
	Spinner lstColor;
	SeekBar sbFontSize;
	TextView footguyFontSizeText;
	int fontsize=12,deltafont=4;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    final CharSequence[] items = 
			{
				"=^_^=",
				"<o>", 
				"\\o/\n" +
				"!!",
		        "   ^__^\n"+
		        "   (oo)\\_______\n"+
		        "   (__)\\       )\\/\\\n"+
		        "       ||----w |\n"+
		        "       ||     ||",
		        " oo\n"+
		        "<!>\n"+
		        "_!_\n" +
		        ""
				};
	    setContentView(R.layout.prefs);
	    ckBorder=(CheckBox)findViewById(R.id.ckBorder);
	    ckBackground=(CheckBox)findViewById(R.id.ckBackground);
	    lstColor=(Spinner)findViewById(R.id.lstColor);
	    sbFontSize=(SeekBar)findViewById(R.id.seekBar1);
	    footguyFontSizeText=(TextView)findViewById(R.id.footguyFontSizeText);
	    ckBorder.setOnClickListener(new CheckBox.OnClickListener(){
	    	@Override
	    	public void onClick(View arg0){
	    		String res;
	    		boolean mybool;
	    		if(ckBorder.isChecked()) {mybool=true;} else {mybool=false;}
	    		SavePreferencesBool("ckBorder",mybool);
	    	}
	    });
	    ckBackground.setOnClickListener(new CheckBox.OnClickListener(){
	    	@Override
	    	public void onClick(View arg0){
	    		String res;
	    		boolean mybool;
	    		if(ckBackground.isChecked()) {mybool=true;ckBorder.setEnabled(true);} else {mybool=false;ckBorder.setEnabled(false);}
	    		SavePreferencesBool("ckBackground",mybool);
	    	}
	    });
	    lstColor.setOnItemSelectedListener(new OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	            int item = lstColor.getSelectedItemPosition();
	            SavePreferencesStr("FootguyColor",lstColor.getSelectedItem().toString());
	        }


	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {
	        	
	        }
	    });
	    sbFontSize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            public void onStopTrackingTouch(SeekBar arg0) {
                 SavePreferencesInt("myFontSize",fontsize);
            }
           
            public void onStartTrackingTouch(SeekBar arg0) {
                 
            }
           
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
            	fontsize=arg1+deltafont;
                footguyFontSizeText.setText("font size: "+ fontsize);
                 
                 
            }
     });
	    TextView foo = (TextView)findViewById(R.id.textView2);
	    String author=items[4] + "footguy for Android. by iazasoft." + "\n2012.";
	    foo.setText(author);
	    LoadPreferences();
	}
	@Override
	public void onDestroy(){
		//Log.d("FFF","PREFS stopped");
		sendUpdateTrigger();
		super.onDestroy();
	}
	private void SavePreferencesStr(String key, String value){
		SharedPreferences sharedPreferences = getSharedPreferences(FOOTGUY_EDIT_PREFS, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	private void SavePreferencesBool(String key, boolean value){
		SharedPreferences sharedPreferences = getSharedPreferences(FOOTGUY_EDIT_PREFS, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	private void SavePreferencesInt(String key, int value){
		SharedPreferences sharedPreferences = getSharedPreferences(FOOTGUY_EDIT_PREFS, MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	private void LoadPreferences(){
		SharedPreferences sharedPreferences = getSharedPreferences(FOOTGUY_EDIT_PREFS, MODE_PRIVATE);
		boolean bBorder = sharedPreferences.getBoolean("ckBorder",true);
		boolean bBackground = sharedPreferences.getBoolean("ckBackground",true);
		fontsize=sharedPreferences.getInt("myFontSize",12);
		ckBorder.setChecked(bBorder);
		ckBackground.setChecked(bBackground);
		if(!bBackground) ckBorder.setEnabled(false); else ckBorder.setEnabled(true);
		ckBorder.invalidate();
		ckBackground.invalidate();
		String ss = sharedPreferences.getString("FootguyColor", "white");
		lstColor.setSelection(mparseColor(ss));
		lstColor.invalidate();
		sbFontSize.setProgress(fontsize-deltafont);
		sbFontSize.invalidate();
		footguyFontSizeText.setText("font size: "+ Integer.toString(fontsize));
	}
	private void sendUpdateTrigger(){
		//Intent intent = new Intent(FOOTGUY_WIDGET_PREFS);
        
        Intent intent = new Intent(this,Footguy.class);
        intent.setAction(FOOTGUY_WIDGET_PREFS);
        //sendBroadcast(intent);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        try{
        		pendingIntent.send();
        }
        catch(Exception e){
        	Log.d("FFF INTENT",e.toString());
        }
	}
	private int mparseColor(String s){
		if(s.equals("white")) return 0;
		else if(s.equals("yellow")) return 1;
		else if(s.equals("lt gray")) return 2;
		else if(s.equals("red")) return 3;
		else if(s.equals("green")) return 4;
		else if(s.equals("blue")) return 5;
		else if(s.equals("cyan")) return 6;
		else if(s.equals("magenta")) return 7;
		else if(s.equals("gray")) return 8;
		else if(s.equals("black")) return 9;
		else return 9;
	}
}
