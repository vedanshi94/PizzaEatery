package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddPizza extends Activity {
	Spinner sp;
	ArrayList<String> name;
	ArrayList<String> img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_pizza);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    name=new ArrayList<String>();
	    name.add("Pizza image");
	    img=new ArrayList<String>();
	    img.add("pz");
	    sp=(Spinner)findViewById(R.id.spinner1);
	    Img_row ad=new Img_row(name, img, this);
	    sp.setAdapter(ad);
	}

	public void AddStdPizza(View v)
	{
		HttpJson hj=new HttpJson();
		try {
			EditText e1 = (EditText)findViewById(R.id.editText1);
			EditText e2 = (EditText)findViewById(R.id.editText2);
		
			JSONObject jobj1 = new JSONObject();
			jobj1.putOpt("name", e1.getText().toString());
			float price = Float.parseFloat(e2.getText().toString());
			jobj1.putOpt("price", price);
			int p=sp.getSelectedItemPosition();
			jobj1.putOpt("img", img.get(p));
			JSONObject jobj = new JSONObject();
			jobj.put("pz", jobj1);
			JSONObject jobj2=hj.execJson("AddStdPizza",jobj);
			int re=jobj2.getInt("AddStdPizzaResult");
			if(re==1)
			{
		        Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
			}
			else
			{
		        Toast.makeText(this, "Exception Occured", Toast.LENGTH_SHORT).show();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Exception Occured", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void onBackPressed() {
		finish();
	    startActivity(new Intent(this, StndPizzaList.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_pizza, menu);
		return true;
	}

}
