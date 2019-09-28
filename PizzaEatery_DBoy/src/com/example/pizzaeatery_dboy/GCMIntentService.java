package com.example.pizzaeatery_dboy;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

@SuppressLint("NewApi")
public class GCMIntentService extends GCMBaseIntentService
{
	public  GCMIntentService()
	{
		super("354340905135");
	}


	@Override
	protected void onError(Context arg0, String arg1) 
	{
		Log.i("Error to regester ", ""+arg1);
	}

	@Override
	protected void onMessage(Context context, Intent arg1) 
	{
		Log.i("Selector is ", ""+arg1.getExtras());
		String message = arg1.getExtras().getString("message");
		generateNotification(context, message);
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) 
	{
		Log.i("Device Token is ", ""+arg1);
		SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
		String duname =(mSharedPreference.getString("dName", null));
		
		JSONObject jobj = new JSONObject();
		  HttpJson hj=new HttpJson();
		  try
		  {
			  jobj.putOpt("uname", duname);
			  jobj.putOpt("deviceId", arg1);
			  
			  String mName="RegDevice";
			  JSONObject jobj2=hj.execJson(mName, jobj);
				  
			  int i=jobj2.getInt("RegDeviceResult");
			  
		  }
		  catch(Exception e)
		  {
			  Toast.makeText(this, "exception occured", 10).show();
		  }
		
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) 
	{
		Log.i("Unregester successfully ", ""+arg1);
	}

	
	private void generateNotification(Context context, String message)
	{
		 Intent intent = new Intent(this, Password.class);

		 PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		    // Build notification
		    Notification noti = new NotificationCompat.Builder(this)  
		        .setContentTitle("Newly Assigned Order")
		        .setContentText(message).setSmallIcon(R.drawable.logo2)
		        .setContentIntent(pIntent).build();
		
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    
		    Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		    noti.sound = uri;
		    
		    // hide the notification after its selected
		    noti.flags = Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(0, noti);

	}

}
