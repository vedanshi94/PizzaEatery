package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class HistoryFrag extends Fragment{

	ArrayList<String> oId;
	ArrayList<String> total;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view=inflater.inflate(R.layout.history,container,false);

		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
		String uname =(mSharedPreference.getString("Name", null));
		
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
		jobj.putOpt("uname",uname);
		
		String mName="GetOrderInfo";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONArray re=jobj2.getJSONArray("GetOrderInfoResult");
		
		oId = new ArrayList<String>();
		total=new ArrayList<String>();
		ArrayList<String> status = new ArrayList<String>();
		ArrayList<String> dt = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			oId.add(re.getJSONObject(i).getString("order_id"));
			status.add(re.getJSONObject(i).getString("status"));
			dt.add(re.getJSONObject(i).getString("date"));
			total.add(re.getJSONObject(i).getString("total"));
		}
		
		ListView lv=(ListView)view.findViewById(R.id.listView1);
		History_row ad=new History_row(oId, status, dt, getActivity());
		lv.setAdapter(ad);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id=oId.get(arg2);
				String tot=total.get(arg2);
				Bundle arg=new Bundle();
				arg.putString("o_id", id);
				arg.putString("tot", tot);
				FragmentTransaction ft=getActivity().getFragmentManager().beginTransaction();
				
				History2Frag hf2 = new History2Frag();
				hf2.setArguments(arg);
			
				ft.addToBackStack(null);
				ft.replace(R.id.content_frame, hf2);
				
				ft.commit();
			}
		});
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return view;
	}
}
