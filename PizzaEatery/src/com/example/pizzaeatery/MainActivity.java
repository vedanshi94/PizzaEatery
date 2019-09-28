package com.example.pizzaeatery;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String uname =(mSharedPreference.getString("Name", null));
		if(uname!=null){
			Intent in=new Intent(MainActivity.this, PinNo.class);
			startActivity(in);
			finish();
		}
		setContentView(R.layout.activity_main);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void new_ac(View v){
		Intent i=new Intent(MainActivity.this,Details.class);
    	startActivity(i);
    	//ActivityCompat.finishAffinity(this);
	}
	
	public void exist(View v){
		Intent i=new Intent(MainActivity.this,Login.class);
    	startActivity(i);
    	//ActivityCompat.finishAffinity(this);
	}

}
