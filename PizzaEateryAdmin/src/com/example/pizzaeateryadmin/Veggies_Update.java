package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class Veggies_Update extends Activity {

	ArrayList<String> vname,vprice,vimg;
	ArrayList<Integer> vid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_veggies__update);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        HttpJson hj=new HttpJson();
        try
        {
		JSONObject jobj2=hj.execJson("GetVeggiesInfo");
		JSONArray re=jobj2.getJSONArray("GetVeggiesInfoResult");
		
		vid=new ArrayList<Integer>();
		vname = new ArrayList<String>();
		vprice = new ArrayList<String>();
		vimg = new ArrayList<String>();

		
		for(int i=0;i<re.length();i++)
		{
			vid.add(re.getJSONObject(i).getInt("veggies_id"));
			vname.add(re.getJSONObject(i).getString("name"));
			vprice.add(re.getJSONObject(i).getString("price"));
			vimg.add(re.getJSONObject(i).getString("img"));
		}
		ListView lv = (ListView)findViewById(R.id.listView1);
		IngrRow ad=new IngrRow(vid,vname, vprice, vimg, this,3);
		lv.setAdapter(ad);
        }
        catch(Exception e)
        {
        	Toast.makeText(this, "Exception Occured", Toast.LENGTH_LONG);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_veggies__update, menu);
		return true;
	}

}
