package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class AssignedOrderList extends Activity {

		ArrayList<String> status,total,date,dname;
		ArrayList<Integer> order_id;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_order_list);
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
			HttpJson hj=new HttpJson();
			
			try {
				JSONObject jobj2=hj.execJson("ViewAssignedOrderList");
				JSONArray re=jobj2.getJSONArray("ViewAssignedOrderListResult");
				
				order_id=new ArrayList<Integer>();
				status = new ArrayList<String>();
				total = new ArrayList<String>();
				date = new ArrayList<String>();
				dname=new ArrayList<String>();
				
				for(int i=0;i<re.length();i++)
				{
					order_id.add(re.getJSONObject(i).getInt("order_id"));
					status.add(re.getJSONObject(i).getString("status"));
					total.add(re.getJSONObject(i).getString("total"));
					date.add(re.getJSONObject(i).getString("date"));
					dname.add(re.getJSONObject(i).getString("uname"));
				}
				
				ListView lv=(ListView)findViewById(R.id.listView1);
				AgnOrder_row or=new AgnOrder_row(dname, status, total, date, order_id , this);
				lv.setAdapter(or);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_assigned_order_list, menu);
		return true;
	}

}
