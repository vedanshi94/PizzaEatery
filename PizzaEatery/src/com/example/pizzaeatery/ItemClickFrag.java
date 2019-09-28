package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ItemClickFrag extends Fragment{
	Integer pId;
	String name,price,img,type;
	Float rating;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.item_click,container,false);
		
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
			Bundle b=getArguments();
			 pId=b.getInt("p_id");
			 name=b.getString("name");
			 price=b.getString("price");
			 img=b.getString("img");
			 type=b.getString("type");
			 rating=b.getFloat("rating");
		
			 TextView tv=(TextView)view.findViewById(R.id.textView1);
			 ImageView iv=(ImageView)view.findViewById(R.id.imageView1);
			 tv.setText(name);
			 String nm="@drawable/"+img;
		    	int resId=getActivity().getApplicationContext().getResources().getIdentifier(nm, null, getActivity().getApplicationContext().getPackageName());
		    	iv.setImageResource(resId);
			 
			 tv=(TextView)view.findViewById(R.id.textView2);
			 if(type.equals("standard"))
				 tv.setText("Standard Pizza");
			 else{
				 JSONObject jo=new JSONObject();
				 HttpJson h=new HttpJson();
				 jo.putOpt("pizza_id", pId);
				 JSONObject jo2=h.execJson("GetIngr", jo);
				 JSONObject jo3=jo2.getJSONObject("GetIngrResult");
				 String bs,c,v,t,s;
				 bs=jo3.getString("b_name");
				 c=jo3.getString("c_name");
				 v=jo3.getString("v_name");
				 t=jo3.getString("t_name");
				 s=jo3.getString("s_name");
				 
				 tv.setText("Custom Pizza\nBase:"+bs+"   Cheese:"+c+"\nVeggies:"+v+"   Sauce:"+s+"\nToppings:"+t);
			 }
			 tv=(TextView)view.findViewById(R.id.textView3);
			 tv.setText(price);
			 
			 RatingBar rb=(RatingBar)view.findViewById(R.id.ratingBar1);
			 rb.setRating(rating);
		
			 jobj.putOpt("pizza_id", pId);
			String mName="GetCom";//...method
			JSONObject jobj2=hj.execJson(mName, jobj);
			JSONArray re=jobj2.getJSONArray("GetComResult");
			
			ArrayList<String> uname = new ArrayList<String>();
			ArrayList<String> date = new ArrayList<String>();
			ArrayList<Float> rating = new ArrayList<Float>();
			ArrayList<String> comment = new ArrayList<String>();
			
			String a;
			float r;
			for(int i=0;i<re.length();i++)
			{
				uname.add(re.getJSONObject(i).getString("uname"));
				date.add(re.getJSONObject(i).getString("date"));
				a=re.getJSONObject(i).getString("rating");
				r=Float.parseFloat(a);
				rating.add(r);
				comment.add(re.getJSONObject(i).getString("comment"));
				
			}
			
			ListView lv=(ListView)view.findViewById(R.id.listView1);
			Reviews_row ad=new Reviews_row(uname, date, comment, rating, getActivity());
			lv.setAdapter(ad); 
			
			view.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Bundle b=getArguments();
					pId=b.getInt("p_id");
					 
					SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
					String usrname =(mSharedPreference.getString("Name", null));
					
					JSONObject jobj=new JSONObject();
					JSONObject jobj1=new JSONObject();
					
					try{
					jobj.putOpt("uname", usrname);
					jobj.putOpt("pizza_id", pId);
					jobj.putOpt("quantity", 1);
					jobj1.putOpt("ct", jobj);
					
					HttpJson hj=new HttpJson();

					JSONObject jobj2=hj.execJson("AddtoCart",jobj1);
				    String a=jobj2.getString("AddtoCartResult");
					    
				    if(a.equals("Added"))
				    	Toast.makeText(getActivity().getApplicationContext(),"Successfully added in cart",Toast.LENGTH_LONG).show();
				    else if(a.equals("Already added"))
					   	Toast.makeText(getActivity().getApplicationContext(),"Already added in cart",Toast.LENGTH_LONG).show();
					else if(a.equals("error"))
				    	Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
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
