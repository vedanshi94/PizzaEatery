package com.example.pizzaeatery_dboy;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
	    SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String duname =(mSharedPreference.getString("dName", null));
		if(duname!=null){
			Intent in=new Intent(MainActivity.this, Password.class);
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
	
	public void login(View v){
		EditText et=(EditText)findViewById(R.id.editText1);
		String uname=et.getText().toString();
		
		EditText et1=(EditText)findViewById(R.id.editText2);
		String password=et1.getText().toString();
		
		JSONObject jobj = new JSONObject();
		  HttpJson hj=new HttpJson();
		  try
		  {
			  jobj.putOpt("uname", uname);
			  jobj.putOpt("password", password);
			  jobj.putOpt("type", "deliveryboy");
			  String mName="Login";
			  JSONObject jobj2=hj.execJson(mName, jobj);
				  
			   String response=(String)jobj2.get("LoginResult");
			  if(response.equals(uname))
			  {
					Toast.makeText(this, "Login successful", 10).show();	  
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
					Editor editor = prefs.edit();
					editor.putString("dName", uname);
					editor.commit();
					Intent i=new Intent(this,OrderAssgnd.class);
					startActivity(i);
					finish();
			  }
			  else
			  {
				  Toast.makeText(this, "Wrong Password", 10).show();
			  }
			  
		  }
		  catch(Exception e)
		  {
			  Toast.makeText(this, "exception occured", 10).show();
		  }
		
	}

}
