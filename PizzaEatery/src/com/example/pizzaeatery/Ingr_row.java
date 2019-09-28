package com.example.pizzaeatery;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Ingr_row  extends BaseAdapter{
	
	Activity context;
    ViewHolder holder;
    ArrayList<String> name;
    ArrayList<String> price;
    ArrayList<String> img;
    LayoutInflater inf;
    int k ;
    public Ingr_row(ArrayList<String> name,ArrayList<String> price,ArrayList<String> img, Activity cont, int k)
    {
    	this.name=name;
    	this.price=price;
    	this.img=img;
    	context=cont;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.k=k;
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(k==0)
		{
			return name.size()+1;
		}
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
	  TextView tv1,tv2,tv3;
	  ImageView iv;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

			if(convertView==null)
			{
				holder=new ViewHolder();
				convertView=inf.inflate(R.layout.ingr_rw, null);
				holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
				holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
				holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
				holder.iv=(ImageView)convertView.findViewById(R.id.imageView1);
				convertView.setTag(holder);
			}
			else
			{
				holder=(ViewHolder)convertView.getTag();
			}
			if(k==1){
			holder.tv1.setText(name.get(position));
			holder.tv3.setText(price.get(position));
			
	    	String nm="@drawable/"+img.get(position);
	    	int resId=context.getApplicationContext().getResources().getIdentifier(nm, null, context.getApplicationContext().getPackageName());
	    	holder.iv.setImageResource(resId);
			}
			
			else
			{
				if(position == 0)
				{
					holder.tv1.setText("Nothing Selected");
					holder.tv3.setText(null);
					holder.iv.setImageDrawable(null);
					holder.tv2.setText(null);
				}
				else
				{
					String nm="@drawable/"+img.get(position-1);
			    	int resId=context.getApplicationContext().getResources().getIdentifier(nm, null, context.getApplicationContext().getPackageName());
					holder.iv.setImageResource(resId);
				    holder.tv1.setText(name.get(position-1));
				    holder.tv3.setText(price.get(position-1));
				    holder.tv2.setText("Price: ");
				}
			}

		return convertView;
	}

}