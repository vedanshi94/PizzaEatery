package com.example.pizzaeatery;

import java.util.ArrayList;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class History2_row extends BaseAdapter{
	Activity context;
	ViewHolder holder;
	ArrayList<String> name;
	ArrayList<String> price;
	ArrayList<String> quant;
	ArrayList<String> total;
	ArrayList<Integer> pId;
	ArrayList<String> img;
	LayoutInflater inf;
	
	public History2_row(ArrayList<Integer> pId,ArrayList<String> name,ArrayList<String> price,ArrayList<String> quant,ArrayList<String> total,ArrayList<String> img, Activity cont)
	{
		this.name=name;
		this.price=price;
		this.quant=quant;
		this.total=total;
		this.pId=pId;
		this.img=img;
		context=cont;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return name.size();
	}

	@Override
	public Object getItem(int position) {
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
		TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
		ImageView iv;
		ImageButton ib;
		Button b1;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.history2_rw, null);
			holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
			holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
			holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
			holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
			holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
			holder.tv6=(TextView)convertView.findViewById(R.id.textView6);
			holder.tv7=(TextView)convertView.findViewById(R.id.textView7);
			holder.iv=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.b1=(Button)convertView.findViewById(R.id.button1);
			
			holder.ib=(ImageButton)convertView.findViewById(R.id.imageButton1);
			convertView.setTag(holder);
			
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv7.setText(name.get(position));
		holder.tv2.setText(price.get(position));
		holder.tv4.setText(quant.get(position));
		holder.tv6.setText(total.get(position));
		String nm="@drawable/"+img.get(position);
    	int resId=context.getApplicationContext().getResources().getIdentifier(nm, null, context.getApplicationContext().getPackageName());
    	holder.iv.setImageResource(resId);
		
		holder.b1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
		
					Bundle arg=new Bundle();
					arg.putInt("pId", pId.get(position));
					
					FragmentTransaction ft=context.getFragmentManager().beginTransaction();
					RateItFrag ri=new RateItFrag();
					ri.setArguments(arg);
					
					ft.addToBackStack(null);
					ft.replace(R.id.content_frame, ri);
					
					ft.commit();
				}
			});
		
		
		holder.ib.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
		
					try 
					{
						Intent sharingIntent = new Intent(Intent.ACTION_SEND);
					    sharingIntent.setType("text/plain");
					    String s="http://192.168.43.170/PizzaEatery/service1.svc";
					    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, s);
				        sharingIntent.setPackage("com.facebook.katana");
				        
				        context.startActivity(sharingIntent);
					}
					catch (Exception e) 
					{
						Log.i("Exception is ", "" + e);
					}
				}
			});
		
		return convertView;
	}
}
