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

public class StndPizzaList extends Activity {
	ArrayList<String> name,price,img,type;
	ArrayList<Integer> id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stnd_pizza_list);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
        HttpJson hj=new HttpJson();
		
		try {
			JSONObject jobj2=hj.execJson("GetPizzaInfo");
			JSONArray re=jobj2.getJSONArray("GetPizzaInfoResult");
			
			id=new ArrayList<Integer>();
			name = new ArrayList<String>();
			price = new ArrayList<String>();
			img = new ArrayList<String>();
			type=new ArrayList<String>();
			
			for(int i=0;i<re.length();i++)
			{
				id.add(re.getJSONObject(i).getInt("pizza_id"));
				name.add(re.getJSONObject(i).getString("name"));
				price.add(re.getJSONObject(i).getString("price"));
				type.add(re.getJSONObject(i).getString("type"));
				img.add(re.getJSONObject(i).getString("img"));
			}
			
			ListView lv=(ListView)findViewById(R.id.listView1);
			Pizza_row ad=new Pizza_row(id,name, price, img,this);
			lv.setAdapter(ad);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void onBackPressed() {
		Intent i = new Intent(this,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
	    startActivity(i);
	}
	
	public void AddPizza(View v)
	{
		Intent i=new Intent(StndPizzaList.this,AddPizza.class);
    	startActivity(i); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_stnd_pizza_list, menu);
		return true;
	}

}
