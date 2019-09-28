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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IngrRow extends BaseAdapter{

	Activity context;
    ViewHolder holder;
    ArrayList<String> name;
    ArrayList<String> price;
    ArrayList<String> img;
    ArrayList<Integer> id;
    int k;
    ArrayList<String> e1,e2;
    LayoutInflater inf;
    public IngrRow(ArrayList<Integer> id,ArrayList<String> name,ArrayList<String> price,ArrayList<String> img, Activity cont,int k)
    {
    	this.id=id;
    	this.name=name;
    	this.price=price;
    	this.img=img;
    	context=cont;
    	this.k=k;
		inf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		e1=new ArrayList<String>();
		e2=new ArrayList<String>();
		for(int i=0;i<name.size();i++){
			e1.add("");
			e2.add("");
		}
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
	  TextView tv2;
	  EditText tv1,tv3;
	  ImageView iv;
	  Button b1,b2;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=inf.inflate(R.layout.ingr_row, null);
			holder.tv1=(EditText)convertView.findViewById(R.id.editText1);
			holder.tv2=(TextView)convertView.findViewById(R.id.textView2);
			holder.tv3=(EditText)convertView.findViewById(R.id.editText2);
			holder.iv=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.b1=(Button)convertView.findViewById(R.id.button1);
			holder.b2=(Button)convertView.findViewById(R.id.button2);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}

		    holder.tv1.setText(name.get(position));
		    holder.tv3.setText(price.get(position));
		    String nm="@drawable/"+img.get(position);
	    	int resId=context.getApplicationContext().getResources().getIdentifier(nm, null, context.getApplicationContext().getPackageName());
	    	holder.iv.setImageResource(resId);
		    
	      holder.tv1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				e1.set(position,s.toString());
			}
		});

		holder.tv3.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				e2.set(position,s.toString());
			}
		});
	      holder.b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HttpJson hj=new HttpJson();

				try {
					
					JSONObject jobj = new JSONObject();
					JSONObject jobj1 = new JSONObject();
				    hj=new HttpJson();
					jobj.putOpt("pizza_id",id.get(position));
					if(!e1.get(position).equals(""))
					{
						jobj.putOpt("name",e1.get(position));
						name.set(position, e1.get(position));
					}
					else
					{
						jobj.putOpt("name",name.get(position));
					}
					if(!e2.get(position).equals(""))
					{
						jobj.putOpt("price",e2.get(position));
						price.set(position,e2.get(position));
					}
					else
					{
						jobj.putOpt("price",price.get(position));
					}
					if(k==1)
					jobj.putOpt("type","Base");
					else if(k==2)
				    jobj.putOpt("type","Cheese");
					else if(k==3)
					jobj.putOpt("type","Veggies");
					else if(k==4)
					jobj.putOpt("type","Toppings");
					else 
				    jobj.putOpt("type","Sauce");
					jobj1.put("pz", jobj);
					int rs;
					JSONObject jobj3=hj.execJson("UpdateIngr",jobj1);
				    rs=jobj3.getInt("UpdateIngrResult");
				    if(rs==1){
				        context.runOnUiThread(new Runnable() {
				    	    public void run() {
				    	        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
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
				    hj=new HttpJson();
					jobj.putOpt("ingr_id",id.get(position));
					if(k==1)
					jobj.putOpt("type","Base");
					else if(k==2)
				    jobj.putOpt("type","Cheese");
					else if(k==3)
					jobj.putOpt("type","Veggies");
					else if(k==4)
					jobj.putOpt("type","Toppings");
					else 
				    jobj.putOpt("type","Sauce");
					int rs;
					JSONObject jobj3=hj.execJson("DeleteIngr",jobj);
				    rs=jobj3.getInt("DeleteIngrResult");
				    if(rs==1){
				    
				    	if(k==1)
				    		{
				    		Intent i = new Intent(context, Base_Update.class);
				    		context.startActivity(i);
				    		context.finish();
					    }
						else if(k==2)
						{
				    		Intent i = new Intent(context, Cheese_Update.class);
				    		context.startActivity(i);
				    		context.finish();
					    }
						else if(k==3)
						{
				    		Intent i = new Intent(context, Veggies_Update.class);
				    		context.startActivity(i);
				    		context.finish();
					    }
						else if(k==4)
						{
				    		Intent i = new Intent(context, Topping_Update.class);
				    		context.startActivity(i);
				    		context.finish();
					    }
						else 
						{
				    		Intent i = new Intent(context, Sauce_Update.class);
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

		return convertView;
	}

	
}
