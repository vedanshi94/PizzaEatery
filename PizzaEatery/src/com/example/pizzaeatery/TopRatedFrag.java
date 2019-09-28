package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TopRatedFrag extends Fragment{
	ArrayList<String> name,price,img,type;
	ArrayList<Integer> id;
	ArrayList<Float> rating;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.pizza_list,container,false);
		
		final HttpJson hj=new HttpJson();
		
		try {
			JSONObject jobj2=hj.execJson("GetToprated");
			JSONArray re=jobj2.getJSONArray("GetTopratedResult");
			
			id=new ArrayList<Integer>();
			name = new ArrayList<String>();
			price = new ArrayList<String>();
			img = new ArrayList<String>();
			rating=new ArrayList<Float>();
			type=new ArrayList<String>();
			
			String a;
			float b;
			
			for(int i=0;i<re.length();i++)
			{
				id.add(re.getJSONObject(i).getInt("pizza_id"));
				name.add(re.getJSONObject(i).getString("name"));
				price.add(re.getJSONObject(i).getString("price"));
				type.add(re.getJSONObject(i).getString("type"));
				img.add(re.getJSONObject(i).getString("img"));
				a=re.getJSONObject(i).getString("rating");
				b=Float.parseFloat(a);
				rating.add(b);
			}
			
			ListView lv=(ListView)view.findViewById(R.id.listView1);
			Pizza_row ad=new Pizza_row(name, price, img, rating, getActivity());
			lv.setAdapter(ad);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					Integer pid=id.get(arg2);
					String nm=name.get(arg2);
					String pr=price.get(arg2);
					String image=img.get(arg2);
					String typ=type.get(arg2);
					Float rt=rating.get(arg2);
					
					Bundle arg=new Bundle();
					arg.putInt("p_id", pid);
					arg.putString("name", nm);
					arg.putString("price", pr);
					arg.putString("img", image);
					arg.putString("type", typ);
					arg.putFloat("rating", rt);
					
					FragmentTransaction ft=getActivity().getFragmentManager().beginTransaction();
					
					ItemClickFrag icf = new ItemClickFrag();
					icf.setArguments(arg);
				
					ft.addToBackStack(null);
					ft.replace(R.id.frame1, icf,"ic");
					
					ft.commit();
				}
			});
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

}
