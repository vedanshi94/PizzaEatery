package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartFrag extends Fragment{

	TextView tv;
	Float cartTot;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view=inflater.inflate(R.layout.cart,container,false);
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
		String uname =(mSharedPreference.getString("Name", null));
		
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
			
			jobj.putOpt("uname",uname);  
			
			String mName="GetCart";
			JSONObject jobj2=hj.execJson(mName, jobj);
			JSONArray re=jobj2.getJSONArray("GetCartResult");
			
			ArrayList<String> name = new ArrayList<String>();
			ArrayList<String> price = new ArrayList<String>();
			ArrayList<Integer> quant = new ArrayList<Integer>();
			ArrayList<Integer> pId = new ArrayList<Integer>();
			ArrayList<String> img = new ArrayList<String>();
			ArrayList<String> total = new ArrayList<String>();
			
			for(int i=0;i<re.length();i++)
			{
				name.add(re.getJSONObject(i).getString("name"));
				price.add(re.getJSONObject(i).getString("price"));
				quant.add(re.getJSONObject(i).getInt("quantity"));
				pId.add(re.getJSONObject(i).getInt("pizza_id"));
				total.add(re.getJSONObject(i).getString("total"));
				img.add(re.getJSONObject(i).getString("img"));	
			}
			
			cartTot=0f;
			tv=(TextView)view.findViewById(R.id.textView2);
			for(int i=0;i<total.size();i++){
				cartTot=cartTot+Float.parseFloat(total.get(i));
			}
			tv.setText(cartTot.toString());
			
			ListView lv=(ListView)view.findViewById(R.id.listView1);
			Cart_row ad=new Cart_row(pId, name, price, quant, total, img, getActivity());
			lv.setAdapter(ad); 
			
			view.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					FragmentManager fm=getFragmentManager();
					FragmentTransaction ft=fm.beginTransaction();
					while(fm.popBackStackImmediate());
					TabFrag tf = new TabFrag(getActivity().getActionBar());
					ft.replace(R.id.content_frame, tf,"m");					
					ft.commit();
					Toast.makeText(getActivity().getApplicationContext(),"Back to Menu Clicked",Toast.LENGTH_LONG).show();
				}
			});
			
			view.findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(cartTot==0){
						Toast.makeText(getActivity().getApplicationContext(),"Your cart is empty...",Toast.LENGTH_LONG).show();
						return;
					}
					else{
					Intent in = new Intent(getActivity(), DelAddr.class);
					getActivity().startActivity(in);
					Toast.makeText(getActivity().getApplicationContext(),"Enter and Select Location...",Toast.LENGTH_LONG).show();
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
