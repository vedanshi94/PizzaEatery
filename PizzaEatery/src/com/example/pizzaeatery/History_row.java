package com.example.pizzaeatery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class History_row extends BaseAdapter {
	Activity context;
    ViewHolder holder;
	ArrayList<String> order_id;
	ArrayList<String> status;
	ArrayList<String> date;
	LayoutInflater inf;

	public History_row(ArrayList<String> order_id,ArrayList<String> status,ArrayList<String> date, Activity cont)
	{
		this.order_id=order_id;
		this.status=status;
		this.date=date;
		context=cont;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return order_id.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static class ViewHolder
	{
		TextView tv1,tv2,tv3,tv4,tv5;
	}
	
	@Override
	public View getView(int position, View ConvertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(ConvertView==null)
		{
			 holder = new ViewHolder();
			 ConvertView=inf.inflate(R.layout.history_rw,null);
			 holder.tv1=(TextView)ConvertView.findViewById(R.id.textView1);
			 holder.tv2=(TextView)ConvertView.findViewById(R.id.textView2);
			 holder.tv3=(TextView)ConvertView.findViewById(R.id.textView3);
			 holder.tv4=(TextView)ConvertView.findViewById(R.id.textView4);
			 holder.tv5=(TextView)ConvertView.findViewById(R.id.textView5);
			 ConvertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)ConvertView.getTag();
		}
		holder.tv2.setText(order_id.get(position));
		holder.tv4.setText(status.get(position));
		holder.tv5.setText(date.get(position));
		return ConvertView;
	}
		
}
