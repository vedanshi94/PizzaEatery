package com.example.pizzaeatery_dboy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class HttpJson {

	JSONObject execJson(String mName,JSONObject jobj)
	{
	  StringEntity se;
	  HttpClient hc=new DefaultHttpClient();
	  HttpPost hp=new HttpPost("http://192.168.43.170/PizzaEatery/Service1.svc/"+mName);
	
	  JSONObject jobj2=null;
	  try{
		  se=new StringEntity(jobj.toString());
		  hp.setEntity(se);
		  hp.setHeader("Accept", "application/json");
		  hp.setHeader("Content-type", "application/json");
	  
		  HttpResponse hr=hc.execute(hp);
		  
		  HttpEntity he=hr.getEntity();
		  InputStream iStream=he.getContent();
		  BufferedReader br=new BufferedReader(new InputStreamReader(iStream));
		  StringBuilder sb=new StringBuilder();
		  String line="";
		  while((line=br.readLine())!=null)
		  {
			  sb.append(line);			
		  }
		  jobj2=new JSONObject(sb.toString());
	  } 
	  catch(Exception e)
	  {
		 
	  }
	  
	return jobj2;
	}
	
	
	JSONObject execJson(String mName)
	{
	  HttpClient hc=new DefaultHttpClient();
	  HttpPost hp=new HttpPost("http://192.168.43.170/PizzaEatery/Service1.svc/"+mName);
	
	  JSONObject jobj2=null;
	  hp.setHeader("Accept", "application/json");
	  hp.setHeader("Content-type", "application/json");
	  try{
		  
		  HttpResponse hr=hc.execute(hp);
		  
		  HttpEntity he=hr.getEntity();
		  InputStream iStream=he.getContent();
		  BufferedReader br=new BufferedReader(new InputStreamReader(iStream));
		  StringBuilder sb=new StringBuilder();
		  String line="";
		  while((line=br.readLine())!=null)
		  {
			  sb.append(line);			
		  }
		  jobj2=new JSONObject(sb.toString());
	  } 
	  catch(Exception e)
	  {
		 
	  }
	  
	return jobj2;
	}
}
