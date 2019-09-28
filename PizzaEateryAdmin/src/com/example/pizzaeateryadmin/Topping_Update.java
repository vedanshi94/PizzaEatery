package com.example.pizzaeateryadmin;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class Topping_Update extends Activity {

	ArrayList<String> tname,tprice,timg;
	ArrayList<Integer> tid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topping__update);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        HttpJson hj=new HttpJson();
        try
        {
		JSONObject jobj2=hj.execJson("GetToppingsInfo");
		JSONArray re=jobj2.getJSONArray("GetToppingsInfoResult");
		
		tid=new ArrayList<Integer>();
		tname = new ArrayList<String>();
		tprice = new ArrayList<String>();
		timg = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			tid.add(re.getJSONObject(i).getInt("toppings_id"));
			tname.add(re.getJSONObject(i).getString("name"));
			tprice.add(re.getJSONObject(i).getString("price"));
			timg.add(re.getJSONObject(i).getString("img"));
		}
		ListView lv = (ListView)findViewById(R.id.listView1);
		IngrRow ad=new IngrRow(tid,tname, tprice, timg, this,4);
		lv.setAdapter(ad);
        }
        catch(JSONException e)
        {
        	Toast.makeText(this, "Exception Occured", Toast.LENGTH_LONG);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_topping__update, menu);
		return true;
	}

}
