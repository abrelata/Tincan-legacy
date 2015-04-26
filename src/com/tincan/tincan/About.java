package com.tincan.tincan;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class About extends ActionBarActivity{
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.about);
	    
	    actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}                                                                                                                       
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}