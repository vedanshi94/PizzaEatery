package com.example.pizzaeateryadmin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void AssignOrder(View v)
	{
		Intent i=new Intent(MainActivity.this,OrderList.class);
    	startActivity(i);
	}
	
	public void ChangeStatus(View v)
	{
		Intent i=new Intent(MainActivity.this,AssignedOrderList.class);
    	startActivity(i);
	}
	
	public void StdList(View v)
	{
		Intent i=new Intent(MainActivity.this,StndPizzaList.class);
    	startActivity(i);
	}
	
	public void AddIngr(View v)
	{
		Intent i=new Intent(MainActivity.this,AddIngr.class);
    	startActivity(i);
	}
	
	public void DelAdd(View v)
	{
		Intent i=new Intent(MainActivity.this,RemoveDel.class);
    	startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
