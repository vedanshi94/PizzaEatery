package com.example.pizzaeatery;

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
import android.widget.Toast;

public class EditPwdFrag extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		final View view=inflater.inflate(R.layout.edit_pwd,container,false);
		
		view.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText et1,et2,et3;
				String oldp,newp,cnewp;
				et1=(EditText)view.findViewById(R.id.editText1);
				et2=(EditText)view.findViewById(R.id.editText2);
				et3=(EditText)view.findViewById(R.id.editText3);
				
				oldp=et1.getText().toString();
				newp=et2.getText().toString();
				cnewp=et3.getText().toString();
				
				if(newp.equals(cnewp))
				{
					int i=0;
					SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); 
					String uname =(mSharedPreference.getString("Name", null));
					
					JSONObject jobj = new JSONObject();
					HttpJson hj=new HttpJson();
					try{
					jobj.putOpt("uname", uname);
					jobj.putOpt("password", newp);
					jobj.putOpt("oldpwd", oldp);
					
					String mName="UpdatePwd";
					JSONObject jobj2=hj.execJson(mName, jobj);
					i=(Integer)jobj2.get("UpdatePwdResult");
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					
					if(i==0)
					{
						Toast.makeText(getActivity().getApplicationContext(),"Wrong old password",Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(getActivity().getApplicationContext(),"Password Updated",Toast.LENGTH_LONG).show();
				}
				
				else
					Toast.makeText(getActivity().getApplicationContext(),"Confirmed password is different",Toast.LENGTH_LONG).show();
			}
		});
		return view;
	}
}
