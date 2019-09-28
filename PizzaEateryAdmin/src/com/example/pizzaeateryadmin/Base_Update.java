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

public class Base_Update extends Activity {
	ArrayList<String> bname,bprice,bimg;
	ArrayList<Integer> bid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base__update);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        HttpJson hj=new HttpJson();
        try
        {
		JSONObject jobj2=hj.execJson("GetBaseInfo");
		JSONArray re=jobj2.getJSONArray("GetBaseInfoResult");
		
		bid=new ArrayList<Integer>();
		bname = new ArrayList<String>();
		bprice = new ArrayList<String>();
		bimg = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			bid.add(re.getJSONObject(i).getInt("base_id"));
			bname.add(re.getJSONObject(i).getString("name"));
			bprice.add(re.getJSONObject(i).getString("price"));
			bimg.add(re.getJSONObject(i).getString("img"));
		}
		ListView lv = (ListView)findViewById(R.id.listView1);
		IngrRow ad=new IngrRow(bid,bname, bprice, bimg, this,1);
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
		getMenuInflater().inflate(R.menu.activity_base__update, menu);
		return true;
	}

}
