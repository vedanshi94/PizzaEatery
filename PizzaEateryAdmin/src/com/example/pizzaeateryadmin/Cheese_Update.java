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

public class Cheese_Update extends Activity {

	ArrayList<String> cname,cprice,cimg;
	ArrayList<Integer> cid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheese__update);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        HttpJson hj=new HttpJson();
        try
        {
		JSONObject jobj2=hj.execJson("GetCheeseInfo");
		JSONArray re=jobj2.getJSONArray("GetCheeseInfoResult");
		
		cid=new ArrayList<Integer>();
		cname = new ArrayList<String>();
		cprice = new ArrayList<String>();
		cimg = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			cid.add(re.getJSONObject(i).getInt("cheese_id"));
			cname.add(re.getJSONObject(i).getString("name"));
			cprice.add(re.getJSONObject(i).getString("price"));
			cimg.add(re.getJSONObject(i).getString("img"));
		}
		ListView lv = (ListView)findViewById(R.id.listView1);
		IngrRow ad=new IngrRow(cid,cname, cprice, cimg, this,2);
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
		getMenuInflater().inflate(R.menu.activity_cheese__update, menu);
		return true;
	}

}
