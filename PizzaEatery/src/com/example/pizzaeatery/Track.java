package com.example.pizzaeatery;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.security.auth.PrivateCredentialPermission;

import org.json.JSONArray;
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

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Track extends Activity implements LocationListener{
	GoogleMap mMap;
	Marker current_marker, destination_marker;
	LatLng mLatLng;
	Driving driving;
	Document doc = null;
	float cLat,cLng,dLat,dLng;
	String uname;
	Integer cnt=0;
	private Timer myTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext()); 
		uname =(mSharedPreference.getString("Name", null));
		getDBoyLoc();
		
		if(dLat==0&&dLng==0){
			Toast.makeText(this, "No order placed or not assigned to Delivery boy", Toast.LENGTH_SHORT).show();
			return;
		}
		getCustLoc();
		SetMap();
		drawline(mLatLng, new LatLng(cLat,cLng));
		
		myTimer=new Timer();
		myTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TimerMethod();
			}
		},0,7000);
	}

	public void TimerMethod(){
		getDBoyLoc();
		this.runOnUiThread(Timer_Tick);
	}
	
	private Runnable Timer_Tick=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			getLocation();
		
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_track, menu);
		return true;
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
		Location mLocation=new Location("");
		mLocation.setLatitude(dLat);
		mLocation.setLongitude(dLng);
		
		onLocationChanged(mLocation);	
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		
		if(current_marker!=null)
		{
			current_marker.remove();
		}
		
		current_marker = mMap.addMarker(new MarkerOptions().position(mLatLng)
				.title("Delivery Boy")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.navigatelocation)));
		if(cnt==0){
		mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
		cnt=1;
		}
		
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
	
	public void getDBoyLoc(){
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
		jobj.putOpt("uname",uname);
		
		String mName="GetDBoyLoc";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONObject re=jobj2.getJSONObject("GetDBoyLocResult");
		
		String s=re.getString("latitude");
		dLat=Float.parseFloat(s);
		s=re.getString("longitude");
		dLng=Float.parseFloat(s);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent in=new Intent(this,Navigation.class);
		startActivity(in);
		finish();
	}

}
