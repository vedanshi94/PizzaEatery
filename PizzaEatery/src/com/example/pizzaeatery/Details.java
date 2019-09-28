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

public class Details extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}
	
	public void Authentication(View v)
	{
	  EditText e1=(EditText)findViewById(R.id.editText1);
	  EditText e2=(EditText)findViewById(R.id.editText2);
	  EditText e3=(EditText)findViewById(R.id.editText3);
	  EditText e4=(EditText)findViewById(R.id.editText4);
	  String uname=e1.getText().toString();
	  String password=e2.getText().toString();
	  String emailid=e3.getText().toString();
	  String mobno=e4.getText().toString();
	  
	  if(uname.equals("")||password.equals("")||emailid.equals("")||mobno.equals(""))
	  {
		  Toast.makeText(this, "Enter all the data", 10).show();
		  return;
	  }
	  JSONObject jobj = new JSONObject();
	  JSONObject jobj1=new JSONObject();
	  HttpJson hj=new HttpJson();
	  try
	  {
		  jobj.putOpt("uname", uname);
		  jobj.putOpt("password", password);
		  jobj.putOpt("emailid", emailid);
		  jobj.putOpt("mobno", mobno);
		  jobj1.putOpt("ci", jobj);
		  
		  String mName="Authentication";
		  JSONObject jobj2=hj.execJson(mName, jobj1);
			  
		   String response=(String)jobj2.get("AuthenticationResult");
		  if(response.equals("a"))
		  {
			  Toast.makeText(this, "registered successfully", 10).show();	  
			  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				Editor editor = prefs.edit();
				editor.putString("Name", uname);
				editor.commit();
				Intent i=new Intent(Details.this,PinNo.class);
				i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				
				//ActivityCompat.finishAffinity(this);
		  }
		  else
		  {
			  Toast.makeText(this, "username already exist", 10).show();
		  }
		  
	  }
	  catch(Exception e)
	  {
		  Toast.makeText(this, "exception occured", 10).show();
	  }
	  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_details, menu);
		return true;
	}

}
