package com.example.pizzaeateryadmin;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_password, menu);
		return true;
	}
	
	public void login(View v){
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		String name =(mSharedPreference.getString("aName", null));
		
		EditText et=(EditText)findViewById(R.id.editText1);
		String password=et.getText().toString();
		
		JSONObject jobj = new JSONObject();
		  HttpJson hj=new HttpJson();
		  try
		  {
			  jobj.putOpt("uname", name);
			  jobj.putOpt("password", password);
			  jobj.putOpt("type", "admin");
			  String mName="Login";
			  JSONObject jobj2=hj.execJson(mName, jobj);
				  
			   String response=(String)jobj2.get("LoginResult");
			  if(response.equals(name))
			  {
				  Toast.makeText(this, "Login successful", 10).show();	  
				  
					Intent i=new Intent(PasswordActivity.this,MainActivity.class);
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
