package com.example.pizzaeatery_dboy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderAssgnd_row extends BaseAdapter{
	Activity context;
    ViewHolder holder;
	ArrayList<String> order_id;
	ArrayList<String> addr,uname;
	ArrayList<String> date;
	LayoutInflater inf;

	public OrderAssgnd_row(ArrayList<String> order_id,ArrayList<String> uname, ArrayList<String> addr,ArrayList<String> date, Activity cont)
	{
		this.order_id=order_id;
		this.uname=uname;
		this.addr=addr;
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
		TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;
	}
	
	@Override
	public View getView(int position, View ConvertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(ConvertView==null)
		{
			 holder = new ViewHolder();
			 ConvertView=inf.inflate(R.layout.order_assgnd_rw,null);
			 holder.tv1=(TextView)ConvertView.findViewById(R.id.textView1);
			 holder.tv2=(TextView)ConvertView.findViewById(R.id.textView2);
			 holder.tv3=(TextView)ConvertView.findViewById(R.id.textView3);
			 holder.tv4=(TextView)ConvertView.findViewById(R.id.textView4);
			 holder.tv5=(TextView)ConvertView.findViewById(R.id.textView5);
			 holder.tv6=(TextView)ConvertView.findViewById(R.id.textView6);
			 holder.tv7=(TextView)ConvertView.findViewById(R.id.textView7);
			 ConvertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)ConvertView.getTag();
		}
		holder.tv2.setText(order_id.get(position));
		holder.tv4.setText(addr.get(position));
		holder.tv5.setText(date.get(position));
		holder.tv7.setText(uname.get(position));
		return ConvertView;
	}

}
