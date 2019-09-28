package com.example.pizzaeatery;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	public void forget_pwd(View v)
	{
		EditText et=(EditText)findViewById(R.id.editText1);
		String uname=et.getText().toString();
		if(uname.equals(""))
		{
			  Toast.makeText(this, "Enter Username and then press this button", Toast.LENGTH_LONG).show();
			  return;
		}
		  JSONObject jobj = new JSONObject();
		  HttpJson hj=new HttpJson();
		  try
		  {  
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
			  jobj.putOpt("type", "customer");
			  String mName="Login";
			  JSONObject jobj2=hj.execJson(mName, jobj);
				  
			   String response=(String)jobj2.get("LoginResult");
			  if(response.equals(uname))
			  {
					Toast.makeText(this, "Login successful", 10).show();	  
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
					Editor editor = prefs.edit();
					editor.putString("Name", uname);
					editor.commit();
					Intent i=new Intent(Login.this,Navigation.class);
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
