package com.tincan.tincan;

import com.tincan.tincan.adapter.DBCon;

import android.net.sip.SipException;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Call extends ActionBarActivity implements OnClickListener, View.OnTouchListener {
	private Button btnEnd, btnMute;
	private TextView txtStatus, txtContact;
	private Bundle bundle;
	private long timeMilliseconds = 0L, timeStart = 0L;
    private String contactName = "", contactNumber = "", domain="10.10.30.50";
	Handler timerHandler = new Handler();
	private MainActivity main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call);
		
		btnEnd = (Button) findViewById(R.id.btnEnd);
		btnMute = (Button) findViewById(R.id.btnMute);
		btnMute.setOnTouchListener(this);
		txtContact = (TextView) findViewById(R.id.txtContact);
		txtStatus = (TextView) findViewById(R.id.txtStatus);
		timeStart = SystemClock.uptimeMillis();
		bundle = getIntent().getExtras();
		main = new MainActivity();
		
		contactName = bundle.getString("contactName");
		contactNumber = bundle.getString("contactNumber");
		
		getSupportActionBar().hide();

		btnEnd.setOnClickListener(this);
		if(contactName.equals("")){
			txtContact.setText(contactNumber );
		}
		else txtContact.setText(contactName);
		
		timerHandler.postDelayed(timerRunnable, 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 == btnEnd){
			String time = txtStatus.getText().toString();
			boolean success = true;

            if(main.call != null) {
                try {
                	main.call.endCall();
                } catch (SipException se) {
                }
                main.call.close();
            }
			try{
				DBCon con = new DBCon(Call.this);
				con.open();
				con.createEntry(txtContact.getText().toString(), time);
				con.close();
				finish();
				}catch(Exception e){
					success = false;
			}
            //break;
		}
	}
	
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
        	timeMilliseconds = SystemClock.uptimeMillis() - timeStart;
        	int seconds  = (int) ((timeMilliseconds / 1000) %60);
        	int minutes = (int) ((timeMilliseconds / (1000*60))%60);
        	int hours = (int) ((timeMilliseconds) / (1000*60*60))%24;
        	
            txtStatus.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}