package com.example.pizzaeatery_dboy;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class OrderAssgnd extends Activity {

	ArrayList<String> uname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		registerForGCM();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_assgnd);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String duname =(mSharedPreference.getString("dName", null));
		
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
		jobj.putOpt("uname",duname);
		
		String mName="GetAssgndOrder";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONArray re=jobj2.getJSONArray("GetAssgndOrderResult");
		
		ArrayList<String> oId = new ArrayList<String>();
		ArrayList<String> addr=new ArrayList<String>();
		uname = new ArrayList<String>();
		ArrayList<String> dt = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			oId.add(re.getJSONObject(i).getString("order_id"));
			uname.add(re.getJSONObject(i).getString("uname"));
			dt.add(re.getJSONObject(i).getString("date"));
			addr.add(re.getJSONObject(i).getString("addr"));
		}
		
		ListView lv=(ListView)findViewById(R.id.listView1);
		OrderAssgnd_row ad=new OrderAssgnd_row(oId, uname, addr, dt, this);
		lv.setAdapter(ad);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String un=uname.get(arg2);
				Intent in=new Intent(getApplicationContext(), Track.class);
				in.putExtra("uname", un);
				  
				startActivity(in);
			}
		});
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_order_assgnd, menu);
		return true;
	}
	
	private void registerForGCM() 
	{
		try 
		{
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
		}
		catch (Exception e) 
		{
			Log.i("Checking Exception is ", ""+e);
		}
		
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params) 
			{
				
				try
				{
					GCMRegistrar.register(OrderAssgnd.this, "354340905135");
				
					//Device Registration
					if(GCMRegistrar.isRegistered(OrderAssgnd.this))
					{					
						Log.i("GCM Device Registration","  registerd  ");
					}
					else
					{
						Log.i("GCM Device Registration"," not registerd  ");
						GCMRegistrar.setRegisteredOnServer(OrderAssgnd.this, true);
					}
				}
				catch (Exception e)
				{
					Log.i("Exception id ", ""+e);
				}
								
				return null;
			}			
		};

		if (GCMRegistrar.getRegistrationId(this) == "")
		{
			task.execute(null,null,null);
		}		
		
	}

}
