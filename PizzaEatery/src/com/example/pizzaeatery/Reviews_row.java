package com.example.pizzaeatery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class Reviews_row extends BaseAdapter {
	Activity context;
	ViewHolder holder;
	ArrayList<String> name;
	ArrayList<String> date;
	ArrayList<String> comment;
	ArrayList<Float> rating;
    LayoutInflater inf;
    
    public Reviews_row(ArrayList<String> name,ArrayList<String> date,ArrayList<String> comment,ArrayList<Float> rating, Activity cont)
    {
    	this.name=name;
    	this.date=date;
    	this.comment=comment;
    	this.rating=rating;
    	context=cont;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		TextView tv1,tv2,tv3;
		RatingBar rb;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.reviews_rw,null);
			holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
			holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
			holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
			holder.rb=(RatingBar)convertView.findViewById(R.id.ratingBar1);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv1.setText(name.get(position));
		holder.tv2.setText(date.get(position));
		holder.tv3.setText(comment.get(position));
		holder.rb.setRating(rating.get(position));
		return convertView;
	}

}
