package com.example.pizzaeatery;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class NearOutlet extends Activity {
	String otId,uname;
	ArrayList<String> ot_id;
	Float cartTot;
	ListView lv;
	ArrayList<String> name,price,img,total;
	
	ArrayList<Integer> quant;
	ArrayList<Integer> pId;
	
	private static final String TAG = "paymentExample";
   
    //private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    private static final String CONFIG_CLIENT_ID = "AWIEKVlsC6ZFQsPBFo0iRNjwDcRbnj5lzaOrVfSEnAcq7iWjFQ-9jjfUgm44F1wLU2tSqYTAbOuqasgK";    												

    private static final int REQUEST_CODE_PAYMENT = 1;   

    private static PayPalConfiguration config; 
    
    PayPalPayment thingToBuy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_near_outlet);
		Intent in=getIntent();
		Bundle b=in.getExtras();
		Float lat=b.getFloat("lat");
		Float lng=b.getFloat("lng");

		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
		jobj.putOpt("lat",lat);
		jobj.putOpt("lng",lng);
		
		String mName="GetOutlets";
		JSONObject jobj2=hj.execJson(mName, jobj);
		JSONArray re=jobj2.getJSONArray("GetOutletsResult");
		
		ot_id = new ArrayList<String>();
		ArrayList<String> addr = new ArrayList<String>();
		ArrayList<String> dist=new ArrayList<String>();
		for(int i=0;i<re.length();i++)
		{
			ot_id.add(re.getJSONObject(i).getString("outlet_id"));
			addr.add(re.getJSONObject(i).getString("adr"));
			dist.add(re.getJSONObject(i).getString("dist"));
		}
		
		lv=(ListView)findViewById(R.id.listView1);
		ArrayAdapter<String> ad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,addr);
		lv.setAdapter(ad);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		config = new PayPalConfiguration()
        .environment(CONFIG_ENVIRONMENT)
        .clientId(CONFIG_CLIENT_ID);
		 
		Intent intent = new Intent(this, PayPalService.class);
       intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
       startService(intent);
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				otId=ot_id.get(arg2);
				Toast.makeText(getApplicationContext(), "Redirecting to Paypal...", Toast.LENGTH_SHORT).show();
				
				SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); 
				uname =(mSharedPreference.getString("Name", null));
				
				JSONObject jobj = new JSONObject();
				HttpJson hj=new HttpJson();
				try{
					
					jobj.putOpt("uname",uname);  
					
					String mName="GetCart";
					JSONObject jobj2=hj.execJson(mName, jobj);
					JSONArray re=jobj2.getJSONArray("GetCartResult");
					
					name = new ArrayList<String>();
					price = new ArrayList<String>();
					quant = new ArrayList<Integer>();
					pId = new ArrayList<Integer>();
					img = new ArrayList<String>();
					total = new ArrayList<String>();
					
					for(int i=0;i<re.length();i++)
					{
						name.add(re.getJSONObject(i).getString("name"));
						price.add(re.getJSONObject(i).getString("price"));
						quant.add(re.getJSONObject(i).getInt("quantity"));
						pId.add(re.getJSONObject(i).getInt("pizza_id"));
						total.add(re.getJSONObject(i).getString("total"));
						img.add(re.getJSONObject(i).getString("img"));	
					}
					
					cartTot=0f;

					for(int i=0;i<total.size();i++){
						cartTot=cartTot+Float.parseFloat(total.get(i));
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				cartTot=cartTot*0.016f;
				thingToBuy = new PayPalPayment(new BigDecimal(cartTot), "USD", "Total Amount", PayPalPayment.PAYMENT_INTENT_SALE);
				
				Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
				 intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);		 	  
		        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
		}});
		
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE_PAYMENT) 
        {
            if (resultCode == Activity.RESULT_OK) 
            {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) 
                {
                    try 
                    {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));

                        JSONObject jobj = new JSONObject();
                        JSONObject jobj1=new JSONObject();
        				HttpJson hj=new HttpJson();
        				
        					DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        					Date dt=new Date();
        					String date=df.format(dt);
        					jobj.putOpt("uname",uname); 
        					
        					jobj1.putOpt("total", cartTot/0.016f);
        					jobj1.putOpt("date", date);
        					jobj1.putOpt("outlet_id", otId);
        					jobj.putOpt("oi", jobj1);
        					
        					JSONObject job=new JSONObject();
        					JSONArray jr=new JSONArray();
        					for(int i=0;i<pId.size();i++){
        						job.put("pizza_id", pId.get(i));
        						job.put("quant", quant.get(i));
        						jr.put(i, job);
        						job=new JSONObject();
        					}
        					jobj.putOpt("list", jr);
        					String mName="PlaceOrder";
        					JSONObject jobj2=hj.execJson(mName, jobj);
        					int i=jobj2.getInt("PlaceOrderResult");
        					
        					jobj = new JSONObject();
            				hj=new HttpJson();
            				jobj2=null;
            					jobj.putOpt("uname",uname); 
            					
            					mName="ClearCart";
            					jobj2=hj.execJson(mName, jobj);
            					i=jobj2.getInt("ClearCartResult");
        					
        					Toast.makeText(getApplicationContext(),"Payment Done...", Toast.LENGTH_LONG).show();
        					
        					Intent in=new Intent(this,Navigation.class);
        					in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        					startActivity(in);

                    } catch (JSONException e) 
                    {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } 
            else if (resultCode == Activity.RESULT_CANCELED) 
            {
                Log.i(TAG, "The user canceled.");
            }
            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) 
            {
                Log.i(TAG,"An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
      
    }
	
	@Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_near_outlet, menu);
		return true;
	}

}
