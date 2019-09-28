package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OrderList extends Activity {
	ArrayList<String> status,total,date,uname;
	ArrayList<Integer> order_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		HttpJson hj=new HttpJson();
		

		try {
			JSONObject jobj2=hj.execJson("ViewOrderList");
			JSONArray re=jobj2.getJSONArray("ViewOrderListResult");
			
			order_id=new ArrayList<Integer>();
			status = new ArrayList<String>();
			total = new ArrayList<String>();
			date = new ArrayList<String>();
			uname=new ArrayList<String>();
			
			for(int i=0;i<re.length();i++)
			{
				order_id.add(re.getJSONObject(i).getInt("order_id"));
				status.add(re.getJSONObject(i).getString("status"));
				total.add(re.getJSONObject(i).getString("total"));
				date.add(re.getJSONObject(i).getString("date"));
				uname.add(re.getJSONObject(i).getString("uname"));
			}
			jobj2=hj.execJson("GetDeliveryboyList");
		    re=jobj2.getJSONArray("GetDeliveryboyListResult");
			ListView lv=(ListView)findViewById(R.id.listView1);
			Order_row or=new Order_row(uname,status, total, date, order_id , this);
			lv.setAdapter(or);
			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					Intent i = new Intent(OrderList.this,OrderDetail.class);
					i.putExtra("total", total.get(arg2));
					int o= order_id.get(arg2).intValue();
					i.putExtra("o_id", o);
					startActivity(i);
				}
			});
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
	    	        Toast.makeText(this,"Exception Occured", Toast.LENGTH_SHORT).show();
		  }
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent i = new Intent(OrderList.this,MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(i);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_order_list, menu);
		return true;
	}

}
