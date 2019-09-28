package com.example.pizzaeatery;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFrag extends Fragment{
	
	ActionBar ab;
	ActionBar.Tab tab1,tab2,tab3;
	
	/*IngrFrag t1 = new IngrFrag();
	PizzaListFrag t2 = new PizzaListFrag();
	TopRatedFrag t3 = new TopRatedFrag();*/
	
	public TabFrag(ActionBar ab)
	{
		this.ab=ab;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		IngrFrag t1 = new IngrFrag();
		PizzaListFrag t2 = new PizzaListFrag();
		TopRatedFrag t3 = new TopRatedFrag();
		
		ab.removeAllTabs();
		View v = inflater.inflate(R.layout.tab, container, false);
		
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		tab1 = ab.newTab().setText("Standard Pizza");
		tab2 = ab.newTab().setText("Custom Pizza");
		tab3 = ab.newTab().setText("Top Rated Pizza");
		
		tab1.setTabListener(new TabListener(t2));
		tab2.setTabListener(new TabListener(t1));
		tab3.setTabListener(new TabListener(t3));
		
		ab.addTab(tab1);
		ab.addTab(tab2);
		ab.addTab(tab3);

		return v;
	}
}
