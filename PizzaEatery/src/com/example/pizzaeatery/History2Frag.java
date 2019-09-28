package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class History2Frag extends Fragment{

	ArrayList<String> name;
	ArrayList<String> price;
	ArrayList<String> quant;
	ArrayList<Integer> pId;
	ArrayList<String> img;
	ArrayList<String> total;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.history2,container,false);
		
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
			Bundle b=getArguments();
			String oId=b.getString("o_id");
			String tot=b.getString("tot");
		jobj.putOpt("order_id",oId);   /* id from the previous frag's listview's item */
		
		String mName="GetOrderDetail";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONArray re=jobj2.getJSONArray("GetOrderDetailResult");
		
		name = new ArrayList<String>();
		price = new ArrayList<String>();
		quant = new ArrayList<String>();
		pId = new ArrayList<Integer>();
		img = new ArrayList<String>();
		total = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			name.add(re.getJSONObject(i).getString("name"));
			price.add(re.getJSONObject(i).getString("price"));
			quant.add(re.getJSONObject(i).getString("quant"));
			pId.add(re.getJSONObject(i).getInt("pizza_id"));
			total.add(re.getJSONObject(i).getString("total"));
			img.add(re.getJSONObject(i).getString("image"));
			
		}
		TextView tv=(TextView)view.findViewById(R.id.textView4);
		tv.setText(oId);
		tv=(TextView)view.findViewById(R.id.textView2);
		tv.setText(tot);
		
		ListView lv=(ListView)view.findViewById(R.id.listView1);
		History2_row ad=new History2_row(pId, name, price, quant, total, img, getActivity());
		lv.setAdapter(ad); 
		
		view.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String a;
				 
				SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
				String usrname =(mSharedPreference.getString("Name", null));
				
				JSONObject jobj=new JSONObject();
				JSONObject jobj1=new JSONObject();
				JSONObject jobj2;
				try{
					
					for(int i=0;i<pId.size();i++){
						
					jobj.putOpt("uname", usrname);
					jobj.putOpt("pizza_id", pId.get(i));
					jobj.putOpt("quantity", quant.get(i));
					jobj1.putOpt("ct", jobj);
					
					HttpJson hj=new HttpJson();
	
					jobj2=hj.execJson("AddtoCart",jobj1);
				    a=jobj2.getString("AddtoCartResult");
					    
					}
					Toast.makeText(getActivity().getApplicationContext(),"Successfully added in cart",Toast.LENGTH_LONG).show();
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return view;
	}

}
