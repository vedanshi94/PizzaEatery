package com.example.pizzaeateryadmin;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddDel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_del);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_del, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(AddDel.this,RemoveDel.class);
		finish();
		startActivity(i);
	}
	
	public void submit(View v)
	{
		
		EditText e1 =(EditText)findViewById(R.id.editText1);
		EditText e2 =(EditText)findViewById(R.id.editText2);
        HttpJson hj=new HttpJson();
		
		try {
			
			JSONObject jobj = new JSONObject();
			jobj.put("uname", e1.getText().toString());
			jobj.put("password", e2.getText().toString());
			JSONObject jobj1 = new JSONObject();
			jobj1.putOpt("di", jobj);
			JSONObject jobj2=hj.execJson("AddDel",jobj1);
			int re=jobj2.getInt("AddDelResult");
		    if(re==1)
		    {
		    	Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
			}
			else if(re==2)
			{
		        Toast.makeText(this, "Already exist", Toast.LENGTH_SHORT).show();
			}
			else
			{
		        Toast.makeText(this, "Exception Occured", Toast.LENGTH_SHORT).show();
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
