package com.example.pizzaeateryadmin;

import java.util.ArrayList;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AgnOrder_row extends BaseAdapter{
	ArrayList<String> status,total,date,dname;
	ArrayList<Integer> order_id;
	ArrayList<Integer> del_id;
	Activity context;
    ViewHolder holder;
    LayoutInflater inf;
	
	public AgnOrder_row(ArrayList<String> dname, ArrayList<String> status,ArrayList<String> total,ArrayList<String> date,ArrayList<Integer> order_id, Activity cont)
    {
    	this.status=status;
    	this.dname=dname;
    	this.total=total;
    	this.date=date;
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
	  Button b1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Toast.makeText(context,"Exception Occured", 10);
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.agn_order_rw, null);
			holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
			holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
			holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
			holder.tv4=(TextView)convertView.findViewById(R.id.textView4);
			holder.tv5=(TextView)convertView.findViewById(R.id.textView5);
			holder.tv6=(TextView)convertView.findViewById(R.id.textView6);
			holder.tv7=(TextView)convertView.findViewById(R.id.textView7);
			holder.tv8=(TextView)convertView.findViewById(R.id.textView8);
			holder.tv9=(TextView)convertView.findViewById(R.id.textView9);
			holder.b1=(Button)convertView.findViewById(R.id.button1);
			
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv2.setText(order_id.get(position).toString());
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpJson hj=new HttpJson();
				

				try {
					JSONObject jobj = new JSONObject();
					JSONObject jobj1=new JSONObject();
				    hj=new HttpJson();
					jobj.putOpt("order_id",order_id.get(position));
					jobj1.put("oi", jobj);
					int rs;
					JSONObject jobj3=hj.execJson("CngStatusOfOrder",jobj1);
				    rs=jobj3.getInt("CngStatusOfOrderResult");
				    if(rs==1){
				    	{
				    		Intent i = new Intent(context, AssignedOrderList.class);
				    		context.startActivity(i);
				    		context.finish();
					    }
				    }
					else
					{
						context.runOnUiThread(new Runnable() {
				    	    public void run() {
				    	        Toast.makeText(context, "Exception Occured", Toast.LENGTH_SHORT).show();
				    	    }
				    	});
					}
					
				}catch (Exception e) {
					// TODO Auto-generated catch block
					context.runOnUiThread(new Runnable() {
			    	    public void run() {
			    	        Toast.makeText(context, "Exception Occured", Toast.LENGTH_SHORT).show();
			    	    }
			    	});
				}
			}
		
		});
    	
		holder.tv4.setText(status.get(position));
		holder.tv6.setText(total.get(position));
		holder.tv7.setText(date.get(position));
		holder.tv9.setText(dname.get(position));

		return convertView;
	}

}
