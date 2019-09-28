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

public class Sauce_Update extends Activity {

	ArrayList<String> sname,sprice,simg;
	ArrayList<Integer> sid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sauce__update);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        HttpJson hj=new HttpJson();
        try
        {
		JSONObject jobj2=hj.execJson("GetSauceInfo");
		JSONArray re=jobj2.getJSONArray("GetSauceInfoResult");
		
		sid=new ArrayList<Integer>();
		sname = new ArrayList<String>();
		sprice = new ArrayList<String>();
		simg = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			sid.add(re.getJSONObject(i).getInt("sauce_id"));
			sname.add(re.getJSONObject(i).getString("name"));
			sprice.add(re.getJSONObject(i).getString("price"));
			simg.add(re.getJSONObject(i).getString("img"));
		}
		ListView lv = (ListView)findViewById(R.id.listView1);
		IngrRow ad=new IngrRow(sid,sname, sprice, simg, this,5);
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
		getMenuInflater().inflate(R.menu.activity_sauce__update, menu);
		return true;
	}

}
