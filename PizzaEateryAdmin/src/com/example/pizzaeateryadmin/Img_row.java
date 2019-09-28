package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import com.example.pizzaeateryadmin.Pizza_row.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Img_row extends BaseAdapter{
	Activity context;
    ViewHolder holder;
    ArrayList<String> name;
    ArrayList<String> img;
    LayoutInflater inf;
    
    public Img_row(ArrayList<String> name,ArrayList<String> img, Activity cont)
    {
    	this.name=name;
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
	  TextView tv1;
	  ImageView iv;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.img_row, null);
			holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
			
			holder.iv=(ImageView)convertView.findViewById(R.id.imageView1);
			
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv1.setText(name.get(position));
		
		String nm="@drawable/"+img.get(position);
    	int resId=context.getApplicationContext().getResources().getIdentifier(nm, null, context.getApplicationContext().getPackageName());
    	holder.iv.setImageResource(resId);
		return convertView;
	}

}
