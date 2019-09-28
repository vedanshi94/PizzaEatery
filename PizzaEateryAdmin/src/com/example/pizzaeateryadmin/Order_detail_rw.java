package com.example.pizzaeateryadmin;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Order_detail_rw extends BaseAdapter{
	Activity context;
	ViewHolder holder;
	ArrayList<String> name;
	ArrayList<String> price;
	ArrayList<String> quant;
	ArrayList<String> total;
	ArrayList<Integer> pId;
	ArrayList<String> img;
	ArrayList<String> c_ingr;
	LayoutInflater inf;
	
	public Order_detail_rw(ArrayList<Integer> pId,ArrayList<String> name,ArrayList<String> price,ArrayList<String> quant,ArrayList<String> total,ArrayList<String> img,ArrayList<String> c_ingr, Activity cont) 
	{
		// TODO Auto-generated constructor stub
		this.name=name;
		this.price=price;
		this.quant=quant;
		this.total=total;
		this.pId=pId;
		this.img=img;
		this.c_ingr=c_ingr;
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
	
	@Override
	public boolean isEnabled(int position) {
	    return false;
	}

	public static class ViewHolder
	{
		TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
		ImageView iv;
		ImageButton ib;
		Button b1;

	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.order_detail_rw, null);
			holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
			holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
			holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
			holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
			holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
			holder.tv6=(TextView)convertView.findViewById(R.id.textView6);
			holder.tv7=(TextView)convertView.findViewById(R.id.textView7);
			holder.iv=(ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
			
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv7.setText(name.get(position)+" "+c_ingr.get(position));
		holder.tv2.setText(price.get(position));
		holder.tv4.setText(quant.get(position));
		holder.tv6.setText(total.get(position));
		String nm="@drawable/"+img.get(position);
    	int resId=context.getApplicationContext().getResources().getIdentifier(nm, null, context.getApplicationContext().getPackageName());
    	holder.iv.setImageResource(resId);
		
		return convertView;
	}

}
