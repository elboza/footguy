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

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class FootHelp extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foothelp);
		
		TextView textView = ( TextView ) findViewById( R.id.textView2 );
		String text = getString( R.string.helper );
		textView.setText(Html.fromHtml(text));
	}
}