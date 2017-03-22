package com.tincan.tincan;

import java.util.List;

import com.tincan.tincan.adapter.DBCon;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class CallLogs extends ActionBarActivity{

	private TextView tv;
	private ListView lv;
	private String data="";
	//private ArrayList data;
	//private List<CallLogObject> data;
	private DBCon con;
	private ActionBar actionBar;

	private int[] viewLogsID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logs);
		
		lv = (ListView) findViewById(R.id.listCall);
		con = new DBCon(this);
		tv = (TextView) findViewById(R.id.tvCall);
		
		viewLogsID = new int[]{R.id.tvLogName, R.id.tvLogDate, R.id.tvLogDescription};
		
	    actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
		showCallLog();
	}

    private void showCallLog() {
		// TODO Auto-generated method stub
		con.open();
			data = con.getData();
		
		con.close();

		tv.setText(data);
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
		CallLogAdapter adapter = new CallLogAdapter(this, R.layout.log_row, data);
		lv.setAdapter(adapter);
		*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_deleteHistory:
        	con.open();
        	con.truncate();
        	con.close();
    		showCallLog();
        	break;
        }
        return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	
}
