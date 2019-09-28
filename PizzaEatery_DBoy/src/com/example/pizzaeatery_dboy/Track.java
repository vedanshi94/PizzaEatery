package com.example.pizzaeatery_dboy;

import java.util.ArrayList;

import org.json.JSONObject;
import org.w3c.dom.Document;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;

public class Track extends Activity implements LocationListener{

	String duname,uname;
	float cLat,cLng,dLat,dLng;
	Integer cnt=0;
	GoogleMap mMap;
	Marker current_marker, destination_marker;
	LatLng mLatLng;
	Driving driving;
	Document doc = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		duname =(mSharedPreference.getString("dName", null));
		Intent in=getIntent();
		Bundle b=in.getExtras();
		uname=b.getString("uname");
		getCustLoc();
		SetMap();
		
		drawline(mLatLng, new LatLng(cLat, cLng));
	}

	public void drawline(final LatLng startingLocation,LatLng endingLocation) 
	{
		ArrayList<LatLng> directionPoint;
		final PolylineOptions rectLine;
	
		driving = new Driving();
		try 
		{
			doc = driving.getDocument(startingLocation, endingLocation,Driving.MODE_DRIVING);
		}

		catch (Exception e) 
		{
			Log.i("Exception is ", "" + e);
		}
		
		if (doc!= null)
		{
			directionPoint = driving.getDirection(doc);
			
			rectLine = new PolylineOptions().width(10).color(Color.RED);

			for (int i = 0; i < directionPoint.size(); i++)
			{
				rectLine.add(directionPoint.get(i));
			}

			mMap.addPolyline(rectLine);				 
		}	
	}
	
	public void SetMap()
	{
		if(mMap==null)
		{
			mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			if(mMap!=null)
			{
				getLocation();
				destination_marker =  mMap.addMarker(new MarkerOptions().position(new LatLng(cLat, cLng))
						.title("Delivery Location")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.navigatelocation)));
			}
		}
	}
	
	public void getLocation()
	{
		LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria mCriteria = new Criteria();
		
		String provider = mLocationManager.getBestProvider(mCriteria, true);
		Location mLocation = mLocationManager.getLastKnownLocation(provider);
		
		if(mLocation!=null)
		{
			onLocationChanged(mLocation);
		}
		
		mLocationManager.requestLocationUpdates(provider, 7000, 0, this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_track, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		dLat=(float)location.getLatitude();
		dLng=(float)location.getLongitude();
		mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		
		if(current_marker!=null)
		{
			current_marker.remove();
		}
		
		current_marker = mMap.addMarker(new MarkerOptions().position(mLatLng)
				.title("Current Location")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.navigatelocation)));
		
		if(cnt==0){
			mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
			cnt=1;
		}
		
		updateDBoyLoc();
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
	
	public void getCustLoc(){
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
		jobj.putOpt("uname",uname);
		
		String mName="GetCustLoc";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONObject re=jobj2.getJSONObject("GetCustLocResult");
		
		String s=re.getString("latitude");
		cLat=Float.parseFloat(s);
		s=re.getString("longitude");
		cLng=Float.parseFloat(s);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void updateDBoyLoc(){
		JSONObject jobj = new JSONObject();
		JSONObject jobj1 = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
		jobj.putOpt("uname",duname);
		jobj.putOpt("latitude",dLat);
		jobj.putOpt("longitude",dLng);
		jobj1.putOpt("di",jobj);
		
		String mName="UpdateDBoyLoc";
		JSONObject jobj2=hj.execJson(mName, jobj1);
		Integer i=jobj2.getInt("UpdateDBoyLocResult");
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	}

}
