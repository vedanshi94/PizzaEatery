package com.example.pizzaeatery;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PinNo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_no);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_pin_no, menu);
		return true;
	}
	
	public void New_pwd(View v)
	{
		JSONObject jobj = new JSONObject();
		  HttpJson hj=new HttpJson();
		  try
		  {
			  SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
			  String uname =(mSharedPreference.getString("Name", null));
			  jobj.put("uname",uname);
			  JSONObject jobj2=hj.execJson("SentMail", jobj);
			  int i= (Integer)jobj2.get("SentMailResult");
			  if(i==2)
			  {
				  Toast.makeText(this, "Exception Occured", Toast.LENGTH_LONG).show();
			  }
			  else
			  {
				  Toast.makeText(this, "New Password Sent To Your Email Account", Toast.LENGTH_LONG).show();
			  }
		  }
		  catch(Exception e)
		  {
			  Toast.makeText(this, "Exception Occured", Toast.LENGTH_LONG).show();
		  }
	}
	
	public void login(View v){
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		String name =(mSharedPreference.getString("Name", null));
		
		EditText et=(EditText)findViewById(R.id.editText1);
		String password=et.getText().toString();
		
		JSONObject jobj = new JSONObject();
		  HttpJson hj=new HttpJson();
		  try
		  {
			  jobj.putOpt("uname", name);
			  jobj.putOpt("password", password);
			  jobj.putOpt("type", "customer");
			  String mName="Login";
			  JSONObject jobj2=hj.execJson(mName, jobj);
				  
			   String response=(String)jobj2.get("LoginResult");
			  if(response.equals(name))
			  {
				  Toast.makeText(this, "Login successful", 10).show();	  
				  
					Intent i=new Intent(PinNo.this,Navigation.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
					//ActivityCompat.finishAffinity(this);
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
