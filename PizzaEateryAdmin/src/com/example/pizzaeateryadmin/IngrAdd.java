package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class IngrAdd extends Activity {
	Spinner sp;
	ArrayList<String> name;
	ArrayList<String> img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingr_add);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    Spinner sp1 = (Spinner)findViewById(R.id.spinner1);
	    String list[] = {"Base","Cheese","Veggies","Toppings","Sauce"};
	    ArrayAdapter<String> ar = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list);
	    sp1.setAdapter(ar);
	    
	    name=new ArrayList<String>();
	    name.add("Ingredient image");
	    img=new ArrayList<String>();
	    img.add("ingr");
	    sp=(Spinner)findViewById(R.id.spinner2);
	    Img_row ad=new Img_row(name, img, this);
	    sp.setAdapter(ad);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_ingr_add, menu);
		return true;
	}
	
	public void Submit(View v)
	{
		HttpJson hj=new HttpJson();
		try {
			EditText e1 = (EditText)findViewById(R.id.editText1);
			EditText e2 = (EditText)findViewById(R.id.editText2);
		
			Spinner sp1 = (Spinner)findViewById(R.id.spinner1);
			JSONObject jobj1 = new JSONObject();
			jobj1.putOpt("name", e1.getText().toString());
			float price = Float.parseFloat(e2.getText().toString());
			jobj1.putOpt("price", price);
			int p=sp.getSelectedItemPosition();
			jobj1.putOpt("img", img.get(p));
			jobj1.putOpt("type",sp1.getSelectedItem().toString());
			JSONObject jobj = new JSONObject();
			jobj.put("pz", jobj1);
			JSONObject jobj2=hj.execJson("AddIngr",jobj);
			int re=jobj2.getInt("AddIngrResult");
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
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "Exception Occured", Toast.LENGTH_SHORT).show();
		}
	}

}
