package com.example.pizzaeatery;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class Navigation extends Activity {

	String[] menu;
	ListView lv;
	DrawerLayout dLayout;
	ActionBarDrawerToggle dToggle;
	ActionBar ab;
	TabFrag tf;
	HistoryFrag hf;
	ProfileFrag pf;
	CartFrag cf;
	ItemClickFrag ic;
	
	FragmentManager fm;
	FragmentTransaction ft;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		
		menu = new String[] { "Menu", "History", "Profile", "Cart", "Track"};
		lv = (ListView) findViewById(R.id.left_drawer);

		ArrayAdapter<String> ad = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, menu);
		lv.setAdapter(ad);

		dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ab=getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);
		
		LayoutInflater li=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		View v=li.inflate(R.layout.custom_actionbar, null);
		ab.setCustomView(v);
		ab.setDisplayShowCustomEnabled(true);
		
		dToggle = new ActionBarDrawerToggle(
				this, dLayout, 
				R.drawable.ic_drawer, //set the image of HomeButton on whose click the drawer will open
				R.string.drawer_open, 
				R.string.drawer_close);

		dLayout.setDrawerListener(dToggle);
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		
		v.findViewById(R.id.imageButton1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				fm = getFragmentManager();
				ft = fm.beginTransaction();
				while(fm.popBackStackImmediate());
				ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				cf = new CartFrag();
				ft.replace(R.id.content_frame, cf,"c");
				ft.commit();
			}
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				dLayout.closeDrawers();

				fm = getFragmentManager();
				ft = fm.beginTransaction();

				if(arg2==0){
					while(fm.popBackStackImmediate());
					tf = new TabFrag(ab);
					ft.replace(R.id.content_frame, tf,"m");					
					ft.commit();
				}
				
				else if (arg2 == 1) {
					while(fm.popBackStackImmediate());
					ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					hf = new HistoryFrag();
					ft.replace(R.id.content_frame, hf,"h");					
					ft.commit();
				}

				else if (arg2 == 2) {
					while(fm.popBackStackImmediate());
					ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					pf = new ProfileFrag();
					ft.replace(R.id.content_frame, pf,"p");
					ft.commit();
				}

				else if (arg2 == 3) {
					while(fm.popBackStackImmediate());
					ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					cf = new CartFrag();
					ft.replace(R.id.content_frame, cf,"c");
					ft.commit();
				}

				else if (arg2 == 4) {
					ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
					Intent in=new Intent(getApplicationContext(),Track.class);
					startActivity(in);
					finish();
				}

				/*else if (arg2 == 3) {
					frg_ios = new fragment_ios();
					ft.replace(R.id.frame1, frg_ios,"i");
					ft.commit();
				}*/
			}
		});
		
		if(savedInstanceState==null)/*to set a default fragment*/
		{
			tf = new TabFrag(ab);
			ft.replace(R.id.content_frame, tf,"m");
			ft.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_navigation, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (dToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Create an override method named onPostCreate and specify the below mentioned statement
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
              dToggle.syncState();
    }

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		ic=(ItemClickFrag)getFragmentManager().findFragmentByTag("ic");
		pf=(ProfileFrag)getFragmentManager().findFragmentByTag("p");
		hf=(HistoryFrag)getFragmentManager().findFragmentByTag("h");
		cf=(CartFrag)getFragmentManager().findFragmentByTag("c");
		if(pf!=null){
				if(pf.isVisible())
				{
					fm=getFragmentManager();
					ft=fm.beginTransaction();
					while(fm.popBackStackImmediate());
					tf = new TabFrag(getActionBar());
					ft.replace(R.id.content_frame, tf);
					
					ft.commit();
				}
		else
			super.onBackPressed();
		}
		else if(hf!=null){
			if(hf.isVisible())
			{
				fm=getFragmentManager();
				ft=fm.beginTransaction();
				while(fm.popBackStackImmediate());
				tf = new TabFrag(getActionBar());
				ft.replace(R.id.content_frame, tf);
				
				ft.commit();
			}
			else
				super.onBackPressed();
			}
		else if(cf!=null){
			if(cf.isVisible())
			{
				fm=getFragmentManager();
				ft=fm.beginTransaction();
				while(fm.popBackStackImmediate());
				tf = new TabFrag(getActionBar());
				ft.replace(R.id.content_frame, tf);
				
				ft.commit();
			}
			else
				super.onBackPressed();
			}
		
		else if(ic!=null){
			if(ic.isVisible())
			{
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					super.onBackPressed();
					getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				}
				else{
					getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
					super.onBackPressed();
				}
			}
			else
				super.onBackPressed();
		}
		else
			super.onBackPressed();	
		
	}
}
