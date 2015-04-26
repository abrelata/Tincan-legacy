package com.tincan.tincan;

import com.tincan.tincan.adapter.AlertManager;
import com.tincan.tincan.adapter.SessionManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Splash extends ActionBarActivity{
	private AlertManager pop;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		getSupportActionBar().hide();
		pop = new AlertManager();
		Thread timer = new Thread(){
			//onHOLD: SIP API CHECKER
			
			@Override
			public void run(){
				try{
					sleep(2000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					startActivity(new Intent(Splash.this, MainActivity.class));
				}
			}
		};
		timer.start();
	}
	
	@Override
	 
	 protected void onStart() {
		SessionManager session = new SessionManager(getBaseContext());
		
	  // TODO Auto-generated method stub
	  	super.onStart();
	  		if(!session.isAPISupported()){
	  			AlertDialog alert = pop.showAlert(Splash.this, getString(R.string.alert_error), getString(R.string.alert_unsupported_device));
	  			/*AlertDialog alert = new AlertDialog.Builder(this).create();
	  			alert.setTitle(getString(R.string.alert_error));
	  			alert.setMessage(getString(R.string.alert_unsupported_device));*/
	  			
	  			alert.setButton(getString(R.string.label_okay), new DialogInterface.OnClickListener() {
	 				@Override
	 				public void onClick(DialogInterface dialog, int which) {
	  					finish();
	  				}
	  			});
	  			alert.show();
	  		}
	 }
	 
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
