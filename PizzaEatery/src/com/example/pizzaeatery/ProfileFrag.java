package com.example.pizzaeatery;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ProfileFrag extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view=inflater.inflate(R.layout.profile,container,false);
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
		String uname =(mSharedPreference.getString("Name", null));
		String eId,mNo;
		
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
		jobj.putOpt("uname",uname);
		
		String mName="GetProf";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONObject re=jobj2.getJSONObject("GetProfResult");
		
		eId=re.getString("emailid");
		mNo=re.getString("mobno");
		
		TextView tv=(TextView)view.findViewById(R.id.textView2);
		tv.setText(uname);
		tv=(TextView)view.findViewById(R.id.textView4);
		tv.setText(eId);
		tv=(TextView)view.findViewById(R.id.textView6);
		tv.setText(mNo);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		view.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				FragmentTransaction ft=getActivity().getFragmentManager().beginTransaction();
				EditPwdFrag ep=new EditPwdFrag();
				ft.replace(R.id.content_frame, ep);
				ft.addToBackStack(null);
				ft.commit();
				
			}
		});
		
		
		
		return view;
	}

}
