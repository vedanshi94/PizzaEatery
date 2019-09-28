
package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Cart_row extends BaseAdapter {
	Activity context;
	ViewHolder holder;
	LayoutInflater inf;
	ArrayList<String> name;
	ArrayList<String> price;
	ArrayList<Integer> quant;
	ArrayList<String> total;
	ArrayList<Integer> pId;
	ArrayList<String> img;
	ArrayList<Boolean> flag=new ArrayList<Boolean>();
	ArrayList<Integer> spQuant=new ArrayList<Integer>();
	ArrayAdapter<Integer> adp;
	Integer i=0;
	
    public Cart_row(ArrayList<Integer> pId,ArrayList<String> name,ArrayList<String> price,ArrayList<Integer> quant,ArrayList<String> total,ArrayList<String> img, Activity cont)
    {
    	this.name=name;
    	this.pId=pId;
    	this.price=price;
    	this.quant=quant;
    	this.total=total;
    	this.img=img;
    	context=cont;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		for(int i=0;i<name.size();i++)
			flag.add(false);
		
		for(int i=0;i<99;i++)
			spQuant.add(i+1);
		adp = new ArrayAdapter<Integer>(context, android.R.layout.simple_dropdown_item_1line, spQuant);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	public static class ViewHolder
	{
		TextView tv1,tv2,tv3,tv4,tv5,tv6;
		Spinner sp1;
		Button b1;
		ImageView iv;
	}

	@Override
	public View getView(final int position, View ConvertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(ConvertView==null)
		{
			holder = new ViewHolder();
			ConvertView=inf.inflate(R.layout.cart_rw,null);
			holder.tv1=(TextView)ConvertView.findViewById(R.id.textView1);
			holder.tv2=(TextView)ConvertView.findViewById(R.id.textView2);
			holder.tv3=(TextView)ConvertView.findViewById(R.id.textView3);
			holder.tv4=(TextView)ConvertView.findViewById(R.id.textView4);
			holder.tv5=(TextView)ConvertView.findViewById(R.id.textView5);
			holder.tv6=(TextView)ConvertView.findViewById(R.id.textView6);
			holder.iv=(ImageView)ConvertView.findViewById(R.id.imageView1);
			holder.b1=(Button)ConvertView.findViewById(R.id.button1);

			holder.sp1=(Spinner)ConvertView.findViewById(R.id.spinner1);
			holder.sp1.setAdapter(adp);
			
			//flag.set(position, false);
	
			ConvertView.setTag(holder); 
		}
		else
		{
			holder=(ViewHolder)ConvertView.getTag();
		}
		holder.tv1.setText(name.get(position));
		holder.tv3.setText(price.get(position));
		holder.sp1.setSelection(quant.get(position)-1);
		holder.tv6.setText(total.get(position));
		String nm="@drawable/"+img.get(position);
    	int resId=context.getApplicationContext().getResources().getIdentifier(nm, null, context.getApplicationContext().getPackageName());
    	holder.iv.setImageResource(resId);
		
		holder.b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	
				SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()); 
				String uname =(mSharedPreference.getString("Name", null));
				
				JSONObject jobj = new JSONObject();
				JSONObject jobj1 = new JSONObject();
				HttpJson hj=new HttpJson();
				try{
					jobj.putOpt("uname",uname);
					jobj.putOpt("pizza_id",pId.get(position));
				
					jobj1.putOpt("ct",jobj);
					
					String mName="RemoveFromCart";
					JSONObject jobj2=hj.execJson(mName, jobj1);
					String s=jobj2.getString("RemoveFromCartResult");
					FragmentTransaction ft=context.getFragmentManager().beginTransaction();
					CartFrag cf1=new CartFrag();
					ft.replace(R.id.content_frame, cf1,"c");
					ft.commit();
					
				}
				catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		holder.sp1.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{
				if(flag.get(position)){
					int q=spQuant.get(arg2);
					
					SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()); 
					String uname =(mSharedPreference.getString("Name", null));
					
					JSONObject jobj = new JSONObject();
					JSONObject jobj1 = new JSONObject();
					HttpJson hj=new HttpJson();
					try{
						jobj.putOpt("uname",uname);
						jobj.putOpt("pizza_id",pId.get(position));
						jobj.putOpt("quantity",q);
						jobj1.putOpt("ct",jobj);
						
						String mName="UpdateQuant";
						JSONObject jobj2=hj.execJson(mName, jobj1);
						int i=jobj2.getInt("UpdateQuantResult");
						FragmentTransaction ft=context.getFragmentManager().beginTransaction();
						CartFrag cf1=new CartFrag();
						ft.replace(R.id.content_frame, cf1,"c");
						ft.commit();
						
					}
					catch (Exception e) {
						// TODO: handle exception
					}
				}
				else
					flag.set(position, true);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return ConvertView;
	}

	
}