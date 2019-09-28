package com.example.pizzaeateryadmin;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class AddIngr extends Activity {

	ArrayList<String> bname,bprice,bimg,cname,cprice,cimg,vname,vprice,vimg,tname,tprice,timg,sname,sprice,simg;
	ArrayList<Integer> bid,cid,vid,tid,sid;
	ArrayList<String> ingr;
	String item = "";
	Spinner sp1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ingr);
		
			sp1=(Spinner)findViewById(R.id.spinner1);
			ingr = new ArrayList<String>();
			ingr.add("Base");
			ingr.add("Cheese");
			ingr.add("Veggies");
			ingr.add("Topping");
			ingr.add("Sauce");
			final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, ingr);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp1.setAdapter(dataAdapter);
			sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					item = ingr.get(arg2);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
	        });
		
	}

	public void AddIng(View v)
	{
		Intent i = new Intent(AddIngr.this, IngrAdd.class);
		startActivity(i);
	}
	
	public void Rmv(View v)
	{
		if(item.equals("Base"))
		{
		Intent i = new Intent(AddIngr.this, Base_Update.class);
		startActivity(i);
		}
		else if(item.equals("Cheese"))
		{
		Intent i = new Intent(AddIngr.this, Cheese_Update.class);
		startActivity(i);
		}
		else if(item.equals("Topping"))
		{
		Intent i = new Intent(AddIngr.this, Topping_Update.class);
		startActivity(i);
		}
		else if(item.equals("Veggies"))
		{
		Intent i = new Intent(AddIngr.this, Veggies_Update.class);
		startActivity(i);
		}
		else
		{
		Intent i = new Intent(AddIngr.this, Sauce_Update.class);
		startActivity(i);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_ingr, menu);
		return true;
	}

}
