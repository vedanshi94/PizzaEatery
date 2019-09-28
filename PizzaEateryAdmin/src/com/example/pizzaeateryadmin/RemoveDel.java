package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class RemoveDel extends Activity {

	ArrayList<String> del_name,pwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_del);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        HttpJson hj=new HttpJson();
		
		try {
			JSONObject jobj2=hj.execJson("GetDeliveryboyList");
			JSONArray re=jobj2.getJSONArray("GetDeliveryboyListResult");
			
			pwd = new ArrayList<String>();
		    del_name=new ArrayList<String>();
		    for(int i=0;i<re.length();i++)
			{
				del_name.add(re.getJSONObject(i).getString("uname"));
				pwd.add(re.getJSONObject(i).getString("password"));
			}
			
			ListView lv=(ListView)findViewById(R.id.listView1);
		    Del_row del = new Del_row(del_name,pwd,this);
			lv.setAdapter(del);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void AddDel(View v)
	{
		Intent i = new Intent(this,AddDel.class);
		startActivity(i);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_remove_del, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(RemoveDel.this,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(i);
	}
}
