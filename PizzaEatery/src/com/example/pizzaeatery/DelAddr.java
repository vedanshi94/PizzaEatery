package com.example.pizzaeatery;

import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DelAddr extends Activity implements LocationListener,OnMapClickListener{
	Marker current_marker;
	GoogleMap mMap;
	float lat=0,lng=0;
	String addr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_del_addr);
		SetMap();
	}
	
	public void SetMap() 
	{
		if (mMap == null) 
		{
			mMap = ((MapFragment)getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			
			if (mMap != null) 
			{
				getLocation();
				mMap.setOnMapClickListener(this);
			}
		}
	}

	@Override
	public void onMapClick(LatLng arg0) 
	{
		if(current_marker!=null)
		{
			current_marker.remove();
		}
				
		current_marker = mMap.addMarker(new MarkerOptions().position(arg0).title("new Delivery location").icon(BitmapDescriptorFactory
				.fromResource(R.drawable.navigatelocation)));
		
		mMap.moveCamera(CameraUpdateFactory.newLatLng(arg0));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
		
		lat=(float)arg0.latitude;
		lng=(float)arg0.longitude;
		Toast.makeText(this, "Location updated...", Toast.LENGTH_SHORT).show();
	}
	
	public void getLocation() 
	{
		LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria mCriteria = new Criteria();
		
		String provider = mLocationManager.getBestProvider(mCriteria, true);
		Location mLocation = mLocationManager.getLastKnownLocation(provider);
		if (mLocation != null) 
		{
			onLocationChanged(mLocation);
		}
		
		mLocationManager.requestLocationUpdates(provider, 50000, 0, this);
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		SetMap();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_del_addr, menu);
		return true;
	}


	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		LatLng mLatLng = new LatLng(arg0.getLatitude(),arg0.getLongitude());
		
		if (current_marker != null) 
		{
			current_marker.remove();
		}

		current_marker = mMap.addMarker(new MarkerOptions()
				.position(mLatLng)
				.title("Current Location")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.navigatelocation)));

		lat=(float)mLatLng.latitude;
		lng=(float)mLatLng.longitude;
		
		mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
		
	}
	
	public void ConfirmAddr(View v)
	{
		EditText et=(EditText)findViewById(R.id.editText1);
		addr=et.getText().toString();
		if(addr.equals(""))
		{
			Toast.makeText(this, "Please Enter Address...", Toast.LENGTH_SHORT).show();
			return;
		}
		if(lat==0||lng==0)
		{
			Toast.makeText(this, "Please Select Location...", Toast.LENGTH_SHORT).show();
			return;
		}
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		String uname =(mSharedPreference.getString("Name", null));
		
		JSONObject jobj = new JSONObject();
		JSONObject jobj1 = new JSONObject();
		  HttpJson hj=new HttpJson();
		  try
		  {
			  jobj.putOpt("uname", uname);
			  jobj.putOpt("latitude", lat);
			  jobj.putOpt("longitude", lng);
			  jobj.putOpt("addr", addr);
			  jobj1.putOpt("ci", jobj);
			  
			  String mName="DelAddr";
			  JSONObject jobj2=hj.execJson(mName, jobj1);
				  
			  Integer i=jobj2.getInt("DelAddrResult");
			  Intent in=new Intent(this, NearOutlet.class);
			  in.putExtra("lat", lat);
			  in.putExtra("lng", lng);
			  startActivity(in);
		  }
		  catch(Exception e)
		  {
			  Toast.makeText(this, "exception occured", 10).show();
		  }
		
	}

}
