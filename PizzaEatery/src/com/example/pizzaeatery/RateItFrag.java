package com.example.pizzaeatery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RateItFrag extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View view=inflater.inflate(R.layout.rate_it,container,false);
		
		Bundle b=getArguments();
		Integer p=b.getInt("pId");
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
			
		jobj.putOpt("pizza_id", p);
		
		String mName="GetOnePizzaInfo";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONObject re=jobj2.getJSONObject("GetOnePizzaInfoResult");
		
		TextView tv=(TextView)view.findViewById(R.id.textView1);
		ImageView iv=(ImageView)view.findViewById(R.id.imageView1);
		tv.setText(re.getString("name"));
		
		String nm="@drawable/"+re.getString("img");
    	int resId=getActivity().getApplicationContext().getResources().getIdentifier(nm, null, getActivity().getApplicationContext().getPackageName());
    	iv.setImageResource(resId);
		
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		view.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
				String uname =(mSharedPreference.getString("Name", null));
				
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dt=new Date();
				String date=df.format(dt);
				
				RatingBar rb=(RatingBar)view.findViewById(R.id.ratingBar1);
				int rate=(int)rb.getRating();
				String rs="";
				Bundle b=getArguments();
				Integer p1=b.getInt("pId");
				EditText et=(EditText)view.findViewById(R.id.editText1);
				String comment=et.getText().toString();
				
				JSONObject jobj = new JSONObject();
				JSONObject jobj1=new JSONObject();
				HttpJson hj=new HttpJson();
				try{
				jobj.putOpt("pizza_id", p1);
				jobj.putOpt("date", date);
				jobj.putOpt("rating", rate);
				jobj.putOpt("comment", comment);
				jobj.putOpt("uname", uname);
				jobj1.put("rt", jobj);
				
				String mName="SubCom";
				JSONObject jobj2=hj.execJson(mName, jobj1);
				rs=jobj2.getString("SubComResult");
				}
				catch (Exception e) {
					// TODO: handle exception
				}
				if(rs.equals("Updated"))
				{
					Toast.makeText(getActivity().getApplicationContext(),"Feedback Updated",Toast.LENGTH_LONG).show();
				}
				else if(rs.equals("Inserted"))
					Toast.makeText(getActivity().getApplicationContext(),"Feedback Submitted",Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getActivity().getApplicationContext(),"Feedback not Submitted",Toast.LENGTH_LONG).show();
			}
		});
		
		return view;
	}

}
