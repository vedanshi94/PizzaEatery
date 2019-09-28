package com.example.pizzaeateryadmin;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OrderDetail extends Activity {

	ArrayList<String> name;
	ArrayList<String> price;
	ArrayList<String> quant;
	ArrayList<Integer> pId;
	ArrayList<String> img;
	ArrayList<String> type;
	ArrayList<String> total,del_name;
	ArrayList<String> c_ingr;
	int oId;
	Spinner sp1;
	String dname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		JSONObject jobj = new JSONObject();
		HttpJson hj=new HttpJson();
		try{
			Intent it = getIntent();
			Bundle b =  it.getExtras();
			oId=b.getInt("o_id");
			String tot=b.getString("total");
		    jobj.putOpt("order_id",oId);  
		
		    String mName="GetOrderDetail";
		    JSONObject jobj2=hj.execJson(mName, jobj);
		    JSONArray re=jobj2.getJSONArray("GetOrderDetailResult");
		
		    name = new ArrayList<String>();
		    price = new ArrayList<String>();
		    quant = new ArrayList<String>();
		    pId = new ArrayList<Integer>();
		    img = new ArrayList<String>();
		    total = new ArrayList<String>();
		    type = new ArrayList<String>();
		    c_ingr = new ArrayList<String>();
		
		for(int i=0;i<re.length();i++)
		{
			name.add(re.getJSONObject(i).getString("name"));
			price.add(re.getJSONObject(i).getString("price"));
			quant.add(re.getJSONObject(i).getString("quant"));
			pId.add(re.getJSONObject(i).getInt("pizza_id"));
			total.add(re.getJSONObject(i).getString("total"));
			img.add(re.getJSONObject(i).getString("image"));
			type.add(re.getJSONObject(i).getString("type"));
			
		}
		for(int i=0;i<pId.size();i++)
		{
			if(type.get(i).equals("Custom"))
			{
			jobj.put("pizza_id", pId.get(i));
			jobj2=hj.execJson("GetIngr", jobj);
			JSONObject jobj3 = new JSONObject();
			jobj3 = jobj2.getJSONObject("GetIngrResult");
			StringBuilder sb = new StringBuilder();
			sb.append("\nIngredient:\t");
			sb.append(jobj3.getString("b_name")+" ");
			if(!jobj3.getString("c_name").equals("null"))
			{
			   sb.append(jobj3.getString("c_name")+" ");	
			}
			if(!jobj3.getString("v_name").equals("null"))
			{
				sb.append(jobj3.getString("v_name")+" ");	
			}
			if(!jobj3.getString("s_name").equals("null"))
			{
				sb.append(jobj3.getString("s_name")+" ");	
			}
			if(!jobj3.getString("t_name").equals("null"))
			{
				sb.append(jobj3.getString("t_name"));	
			}
			c_ingr.add(sb.toString());
			}
			else
			{
				c_ingr.add("");
			}
		}
		jobj2=hj.execJson("GetDeliveryboyList");
	    re=jobj2.getJSONArray("GetDeliveryboyListResult");
		del_name=new ArrayList<String>();
		for(int i=0;i<re.length();i++)
		{
			del_name.add(re.getJSONObject(i).getString("uname"));
		}
		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, del_name);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner sp1 = (Spinner)findViewById(R.id.spinner1);
		sp1.setAdapter(dataAdapter);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {


			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				dname = dataAdapter.getItem(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub		
			}
        });
		TextView tv=(TextView)findViewById(R.id.textView4);
		String s= Integer.toString(oId);
		tv.setText(s);
		tv=(TextView)findViewById(R.id.textView2);
		tv.setText(tot);
		
		ListView lv=(ListView)findViewById(R.id.listView1);
		Order_detail_rw ad=new Order_detail_rw(pId, name, price, quant, total, img, c_ingr, this);
		lv.setAdapter(ad);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception Occured", Toast.LENGTH_LONG);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_order_detail, menu);
		return true;
	}

	 public void AgnOrder(View v)
     {
		HttpJson hj=new HttpJson();	

		try {
			JSONObject jobj = new JSONObject();
			jobj.putOpt("uname", dname);
			JSONObject jobj2=hj.execJson("GetDeliveryboyId",jobj);
			int did=jobj2.getInt("GetDeliveryboyIdResult");
		    jobj = new JSONObject();
			JSONObject jobj1=new JSONObject();
		    hj=new HttpJson();
			jobj.putOpt("deliveryboy_id", did);
			jobj.putOpt("order_id",oId);
			jobj1.put("oi", jobj);
			int rs;
			JSONObject jobj3=hj.execJson("AssignOrder",jobj1);
		    rs=jobj3.getInt("AssignOrderResult");
		    if(rs==1){
		    Intent i = new Intent(OrderDetail.this,OrderList.class);
		    finish();
		    startActivity(i);
		    
		    Toast.makeText(this, "Order Assigned Successfully", Toast.LENGTH_SHORT).show();
		    }
		    else
		    {
		    	 Toast.makeText(this, "Exception Occured", Toast.LENGTH_SHORT).show();
		    }
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
	    	        Toast.makeText(this, "Exception Occured", Toast.LENGTH_SHORT).show();
	    	    
		    }
     }
}
