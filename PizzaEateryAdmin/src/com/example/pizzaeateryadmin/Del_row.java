package com.example.pizzaeateryadmin;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Del_row extends BaseAdapter{

	ArrayList<String> uname,pwd,w1;
	Activity context;
	LayoutInflater inf;
	ViewHolder holder;
	 public Del_row(ArrayList<String> uname,ArrayList<String> pwd,Activity cont)
     {
		 this.uname=uname;
		 this.pwd=pwd;
		 this.context=cont;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		w1=new ArrayList<String>();
		for(int i = 0; i < uname.size(); i++)
		{
			w1.add("");
		}
	 }
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return uname.size();
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
	  EditText e1;
	  TextView tv1,tv2,tv3;
	  Button b1,b2;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.del_list, null);
			holder.tv1=(TextView)convertView.findViewById(R.id.textView1);
			holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
			holder.tv3=(TextView)convertView.findViewById(R.id.textView3);
			holder.e1=(EditText)convertView.findViewById(R.id.editText1);
			holder.b1=(Button)convertView.findViewById(R.id.button1);
			holder.b2=(Button)convertView.findViewById(R.id.button2);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		holder.tv2.setText(uname.get(position));
		holder.e1.setText(pwd.get(position));
	    holder.e1.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				w1.set(position,s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			} 
        });
		
		holder.b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpJson hj=new HttpJson();

				try {		
					JSONObject jobj = new JSONObject();
					jobj.put("uname",uname.get(position));
					if(!w1.get(position).equals(""))
					{
						jobj.put("password",w1.get(position));
						pwd.set(position, w1.get(position));
					}
					else
					{
						jobj.put("password",pwd.get(position));
					}
					JSONObject jobj1 = new JSONObject();
					jobj1.putOpt("di", jobj);
					JSONObject jobj2=hj.execJson("UpdateDel",jobj1);
					int rs=jobj2.getInt("UpdateDelResult");
				    if(rs==1){
				    	context.runOnUiThread(new Runnable() {
				    	    public void run() {
				    	        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();
				    	    }
				    	});
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
		holder.b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpJson hj=new HttpJson();

				try {
					
					JSONObject jobj = new JSONObject();
					JSONObject jobj1 = new JSONObject();
					jobj.put("uname",uname.get(position));
					jobj1.putOpt("di", jobj);
					int rs;
					JSONObject jobj3=hj.execJson("RemoveDel",jobj1);
				    rs=jobj3.getInt("RemoveDelResult");
				    if(rs==1){
				    Intent i = new Intent(context, RemoveDel.class);
				    context.startActivity(i);
				    context.finish();
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
		return convertView;
	}

}
