package com.example.pizzaeateryadmin;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Order_row extends BaseAdapter{

	ArrayList<String> status,total,date,uname;
	ArrayList<Integer> order_id;
	Activity context;
    ViewHolder holder;
    LayoutInflater inf;
    String name;
	
	public Order_row(ArrayList<String> uname, ArrayList<String> status,ArrayList<String> total,ArrayList<String> date,ArrayList<Integer> order_id, Activity cont)
    {
    	this.status=status;
    	this.total=total;
    	this.date=date;
    	this.uname=uname;
    	this.order_id=order_id;
    	context=cont;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return status.size();
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
	  TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.order_rw, null);
			holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
			holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
			holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
			holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
			holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
			holder.tv6=(TextView)convertView.findViewById(R.id.textView6);
			holder.tv7=(TextView)convertView.findViewById(R.id.textView7);
			holder.tv8=(TextView)convertView.findViewById(R.id.textView8);
			holder.tv9=(TextView)convertView.findViewById(R.id.textView9);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv2.setText(order_id.get(position).toString());
		holder.tv4.setText(status.get(position));
		holder.tv6.setText(total.get(position));
		holder.tv7.setText(date.get(position));
		holder.tv9.setText(uname.get(position));
		return convertView;
	}

}
