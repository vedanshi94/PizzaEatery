package com.example.pizzaeateryadmin;

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

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String auname =(mSharedPreference.getString("aName", null));
		if(auname!=null){
			Intent in=new Intent(LoginActivity.this, PasswordActivity.class);
			startActivity(in);
			finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
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
			  jobj.putOpt("type", "admin");
			  String mName="Login";
			  JSONObject jobj2=hj.execJson(mName, jobj);
				  
			   String response=(String)jobj2.get("LoginResult");
			  if(response.equals(uname))
			  {
					Toast.makeText(this, "Login successful", 10).show();	  
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
					Editor editor = prefs.edit();
					editor.putString("aName", uname);
					editor.commit();
					Intent i=new Intent(LoginActivity.this,MainActivity.class);
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
