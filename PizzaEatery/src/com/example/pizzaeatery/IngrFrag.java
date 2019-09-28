package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class IngrFrag extends Fragment{
	ArrayList<String> bname,bprice,bimg,cname,cprice,cimg,vname,vprice,vimg,tname,tprice,timg,sname,sprice,simg;
	ArrayList<Integer> bid,cid,vid,tid,sid;
	
	Spinner sp1,sp2,sp3,sp4,sp5;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		final View view=inflater.inflate(R.layout.ingr,container,false);
		
		HttpJson hj=new HttpJson();
		
		try {
			JSONObject jobj2=hj.execJson("GetBaseInfo");
			JSONArray re=jobj2.getJSONArray("GetBaseInfoResult");
			
			bid=new ArrayList<Integer>();
			bname = new ArrayList<String>();
			bprice = new ArrayList<String>();
			bimg = new ArrayList<String>();
			
			for(int i=0;i<re.length();i++)
			{
				bid.add(re.getJSONObject(i).getInt("base_id"));
				bname.add(re.getJSONObject(i).getString("name"));
				bprice.add(re.getJSONObject(i).getString("price"));
				bimg.add(re.getJSONObject(i).getString("img"));
			}
			
			jobj2=null;
			re=null;
			jobj2=hj.execJson("GetCheeseInfo");
			re=jobj2.getJSONArray("GetCheeseInfoResult");
			
			cid=new ArrayList<Integer>();
			cname = new ArrayList<String>();
			cprice = new ArrayList<String>();
			cimg = new ArrayList<String>();
			/*cid.add(0);
			cname.add(null);
			cprice.add(null);
			cimg.add(null);*/
			
			for(int i=0;i<re.length();i++)
			{
				cid.add(re.getJSONObject(i).getInt("cheese_id"));
				cname.add(re.getJSONObject(i).getString("name"));
				cprice.add(re.getJSONObject(i).getString("price"));
				cimg.add(re.getJSONObject(i).getString("img"));
			}
			
			jobj2=null;
			re=null;
			jobj2=hj.execJson("GetVeggiesInfo");
			re=jobj2.getJSONArray("GetVeggiesInfoResult");
			
			vid=new ArrayList<Integer>();
			vname = new ArrayList<String>();
			vprice = new ArrayList<String>();
			vimg = new ArrayList<String>();
			/*vid.add(0);
			vname.add(null);
			vprice.add(null);
			vimg.add(null);*/
			
			for(int i=0;i<re.length();i++)
			{
				vid.add(re.getJSONObject(i).getInt("veggies_id"));
				vname.add(re.getJSONObject(i).getString("name"));
				vprice.add(re.getJSONObject(i).getString("price"));
				vimg.add(re.getJSONObject(i).getString("img"));
			}
			
			jobj2=null;
			re=null;
			jobj2=hj.execJson("GetToppingsInfo");
			re=jobj2.getJSONArray("GetToppingsInfoResult");
			
			tid=new ArrayList<Integer>();
			tname = new ArrayList<String>();
			tprice = new ArrayList<String>();
			timg = new ArrayList<String>();
			/*tid.add(0);
			tname.add(null);
			tprice.add(null);
			timg.add(null);*/
			
			for(int i=0;i<re.length();i++)
			{
				tid.add(re.getJSONObject(i).getInt("toppings_id"));
				tname.add(re.getJSONObject(i).getString("name"));
				tprice.add(re.getJSONObject(i).getString("price"));
				timg.add(re.getJSONObject(i).getString("img"));
			}
			
			jobj2=null;
			re=null;
			jobj2=hj.execJson("GetSauceInfo");
			re=jobj2.getJSONArray("GetSauceInfoResult");
			
			sid=new ArrayList<Integer>();
			sname = new ArrayList<String>();
			sprice = new ArrayList<String>();
			simg = new ArrayList<String>();
			/*sid.add(0);
			sname.add(null);
			sprice.add(null);
			simg.add(null);*/
			
			for(int i=0;i<re.length();i++)
			{
				sid.add(re.getJSONObject(i).getInt("sauce_id"));
				sname.add(re.getJSONObject(i).getString("name"));
				sprice.add(re.getJSONObject(i).getString("price"));
				simg.add(re.getJSONObject(i).getString("img"));
			}
			
			sp1=(Spinner)view.findViewById(R.id.spinner1);
			sp2=(Spinner)view.findViewById(R.id.spinner2);
			sp3=(Spinner)view.findViewById(R.id.spinner3);
			sp4=(Spinner)view.findViewById(R.id.spinner4);
			sp5=(Spinner)view.findViewById(R.id.spinner5);
			
			Ingr_row ad=new Ingr_row(bname, bprice, bimg, getActivity(),1);
			sp1.setAdapter(ad);
			ad=new Ingr_row(cname, cprice, cimg, getActivity(),1);
			sp2.setAdapter(ad);
			ad=new Ingr_row(vname, vprice, vimg, getActivity(),0);
			sp3.setAdapter(ad);
			ad=new Ingr_row(tname, tprice, timg, getActivity(),0);
			sp4.setAdapter(ad);
			ad=new Ingr_row(sname, sprice, simg, getActivity(),1);
			sp5.setAdapter(ad);
			
			view.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					EditText et=(EditText)view.findViewById(R.id.editText1);
					String pName=et.getText().toString();
					if(pName.equals("")){
						Toast.makeText(getActivity().getApplicationContext(),"Enter Name",Toast.LENGTH_LONG).show();
						return;
					}
					Integer b=bid.get(sp1.getSelectedItemPosition());
					Integer c=cid.get(sp2.getSelectedItemPosition());
					
					Integer vg;
					if(sp3.getSelectedItemPosition()==0)
					{
						vg=0;
					}
					else
					{
					   vg=vid.get(sp3.getSelectedItemPosition()-1);
					}
					Integer t;
					if(sp4.getSelectedItemPosition()==0)
					{
						t=0;
					}
					else
					{
					   t=tid.get(sp4.getSelectedItemPosition()-1);
					}
					Integer s=sid.get(sp5.getSelectedItemPosition());
					
					Integer rs;
					try{
					JSONObject jobj=new JSONObject();
					JSONObject jobj1=new JSONObject();
					
					
					HttpJson hj=new HttpJson();
					
					jobj.putOpt("name", pName);
					jobj.putOpt("base_id", b);
					jobj.putOpt("cheese_id", c);
					jobj.putOpt("veggies_id", vg);
					jobj.putOpt("toppings_id", t);
					jobj.putOpt("sauce_id", s);
					jobj1.putOpt("cp", jobj);
				
					
					String mName="CustomPizza";
					JSONObject jobj2=hj.execJson(mName, jobj1);
					rs=jobj2.getInt("CustomPizzaResult");
					
					
					if(rs==0)
					{
						Toast.makeText(getActivity().getApplicationContext(),"Pizza Name already exist...",Toast.LENGTH_LONG).show();
						return;
					}
					
					jobj=new JSONObject();
					jobj1=new JSONObject();
					jobj2=null;
					SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
					String uname =(mSharedPreference.getString("Name", null));
					jobj.putOpt("uname", uname);
					jobj.putOpt("pizza_id", rs);
					jobj.putOpt("quantity", 1);
					jobj1.putOpt("ct", jobj);
					
						jobj2=hj.execJson("AddtoCart",jobj1);
					    String a=jobj2.getString("AddtoCartResult");
					    
					    if(a.equals("Added"))
					    	Toast.makeText(getActivity().getApplicationContext(),"Successfully added in cart",Toast.LENGTH_LONG).show();
					    else if(a.equals("Already added"))
					    	Toast.makeText(getActivity().getApplicationContext(),"Already added in cart",Toast.LENGTH_LONG).show();
					    else if(a.equals("error"))
					    	Toast.makeText(getActivity().getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
					    	
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			});
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return view;
	}

}