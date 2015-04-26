package com.tincan.tincan;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tincan.tincan.adapter.AlertManager;
import com.tincan.tincan.adapter.ContactsAdapter;
import com.tincan.tincan.adapter.SessionManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnItemClickListener{
	public SharedPreferences settings;
	public String username, password, contactNumber="", contactName="";
	public final String domain = "10.10.30.50";

	private Cursor phone;
	private List<Contacts> list;
	private ListView contactList;
	private TextView txtStatus;
	private ActionBar actionBar;
	
    public SipManager manager = null;
    public SipProfile me = null;
    public SipAudioCall call = null;
    public IncomingCallReceiver callReceiver;
    public int sipstatus = 0;
    
    private AlertManager pop;
    private SessionManager session;
    private static final int UPDATE_SETTINGS_DIALOG = 1;
    private View viewContactNumber, viewAB;
	private LayoutInflater factory;
    
	private MenuItem profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = new ArrayList<Contacts>();
		pop  = new AlertManager();
		session = new SessionManager(getApplicationContext());
		contactList = (ListView) findViewById(R.id.list);
		txtStatus = (TextView) findViewById(R.id.textView1);
        actionBar = getSupportActionBar();
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //viewAB = View.inflate(getApplicationContext(), R.layout.action_progress, null);
        //actionBar.setCustomView(viewAB);
        callReceiver = new IncomingCallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.SipDemo.INCOMING_CALL");
        this.registerReceiver(callReceiver, filter);
        
        phone = getContentResolver().query(
        		ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        		null, null, null, null);

        contactList.setOnItemClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		showContacts();
		initializeManager();
	}
	
    @Override
    public void onStart() {
        super.onStart();
        initializeManager();
    }
    
    private void initializeManager() {
		// TODO Auto-generated method stub
        if(manager == null) {
            manager = SipManager.newInstance(this);
          }

        initializeLocalProfile();
	}

	private void initializeLocalProfile() {
		// TODO Auto-generated method stub
        if (manager == null) {
            return;
        }

        if (me != null) {
            closeLocalProfile();
        }

        settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        username = settings.getString("settings_username", "");
        password = settings.getString("settings_password", "");

        if (username.length() == 0 || password.length() == 0) {
            showDialog(UPDATE_SETTINGS_DIALOG);
            return;
        }
        
        try {
            SipProfile.Builder builder = new SipProfile.Builder(username, domain);
            builder.setPassword(password);
            me = builder.build();

            Intent i = new Intent();
            i.setAction("android.SipDemo.INCOMING_CALL");
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, Intent.FILL_IN_DATA);
            manager.open(me, pi, null);
            final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);


            manager.setRegistrationListener(me.getUriString(), new SipRegistrationListener() {
                @Override
				public void onRegistering(String localProfileUri) {
                    updateStatus(getString(R.string.label_registering_to_server));
     				//profile.setIcon(getResources().getDrawable(R.drawable.ic_action_person_busy));
                    sipstatus = 1;
                    pb.setVisibility(View.VISIBLE);
                }

                @Override
				public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    updateStatus(getString(R.string.label_ready));
                    //profile.setIcon(getResources().getDrawable(R.drawable.ic_action_person_ready));
                    sipstatus = 2;
                    pb.setVisibility(View.INVISIBLE);
                }

                @Override
				public void onRegistrationFailed(String localProfileUri, int errorCode,
                        String errorMessage) {
                    //updateStatus(getString(R.string.alert_register_failed));
                    //pb.setVisibility(View.INVISIBLE);
                    pop.showToast(MainActivity.this, getString(R.string.alert_register_failed), 20);
                    updateStatus("");
                    sipstatus = 0;
                }
            });
        } catch (ParseException pe) {
            updateStatus("Connection Error.");
        } catch (SipException se) {
            updateStatus("Connection error.");
        }

	}

	public void updateStatus(final String status) {
		// TODO Auto-generated method stub
		this.runOnUiThread(new Runnable() {
            @Override
			public void run() {
                //TextView labelView = (TextView) findViewById(R.id.sipLabel);
               // pop.showToast(getApplicationContext(), status, 30);
                txtStatus.setText(status);
            }
	    });
	}

	private void closeLocalProfile() {
		// TODO Auto-generated method stub
	   if (manager == null)
	            return;
	   try {
		    if (me != null)
		    	manager.close(me.getUriString());
		} catch (Exception ee) {}	
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.close();
        }

        closeLocalProfile();

        if (callReceiver != null) {
            this.unregisterReceiver(callReceiver);
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//profile = menu.getItem(0);
		profile = menu.findItem(R.id.action_profile);
		return true;
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
    	if(id == R.id.action_call){
    		factory = LayoutInflater.from(this);
    		viewContactNumber = factory.inflate(R.layout.dialog_dial, null);
			return new AlertDialog.Builder(this)
			.setView(viewContactNumber)
			.setPositiveButton(R.string.label_call, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					// TODO Auto-generated method stub
					EditText txtContactNumber = (EditText) (viewContactNumber.findViewById(R.id.txtContact));
					if(!txtContactNumber.getText().toString().equals("")){
						//if(session.isOnline() && sipstatus == 2){
							contactNumber = txtContactNumber.getText().toString().trim();
							initiateCall();
						//}
						//else
							//pop.showToast(getApplicationContext(), getString(R.string.alert_alert_offline), 30);
					}
					else
						pop.showToast(getApplicationContext(), getString(R.string.alert_alert_offline), 30);
				}
			})					
			.setNegativeButton(R.string.label_cancel,  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			})
			.create();
    	}
    	else if(id == UPDATE_SETTINGS_DIALOG){
            return new AlertDialog.Builder(this)
            .setTitle(getString(R.string.label_welcome))
            .setMessage(getString(R.string.label_getStarted))
            .setPositiveButton(R.string.label_login, new DialogInterface.OnClickListener() {
            	@Override
				public void onClick(DialogInterface dialog, int whichButton) {
            		updateSettings();
            		}
            	})
        	.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
        		@Override
				public void onClick(DialogInterface dialog, int whichButton) {
        			finish();
    				}
    			})
			.create();
		}
    	else
    	   return null;
    }
    
    protected void initiateCall() {
		// TODO Auto-generated method stub
    	//updateStatus(contactNumber);
    	if(session.isOnline()){
	    	Intent i = new Intent(MainActivity.this, Call.class);
	    	i.putExtra("contactName", contactName);
	    	i.putExtra("contactNumber", contactNumber);
			startActivity(i);
			
			try {
	            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
	                @Override
	                public void onCallEstablished(SipAudioCall call) {
	                    call.startAudio();
	                    call.setSpeakerMode(true);
	                    call.toggleMute();
	                    updateStatus(call);
	                }

	                @Override
	                public void onCallEnded(SipAudioCall call) {
	                    updateStatus("Ready.");
	                    sipstatus=2;
	                }
	            };

	            call = manager.makeAudioCall(me.getUriString(), contactNumber+"@"+domain, listener, 30);
			}catch (Exception e) {
	            if (me != null) {
	                try {
	                    manager.close(me.getUriString());
	                } catch (Exception ee) {
	                    ee.printStackTrace();
	                }
	            }
	            if (call != null) {
	                call.close();
	            }
	        }
		}
    	else{
    		pop.showToast(this, getString(R.string.alert_alert_offline), 30);
    	}
	}

	protected void updateStatus(SipAudioCall call) {
		// TODO Auto-generated method stub
        String useName = call.getPeerProfile().getDisplayName();
        if(useName == null) {
          useName = call.getPeerProfile().getUserName();
        }
        updateStatus(useName + "@" + call.getPeerProfile().getSipDomain());
	}

	private void updateSettings(){
    	Intent settings = new Intent(getBaseContext(), Profile.class);
    	startActivity(settings);
    }
    
    //CONTACTS
    private void showContacts() {
		// TODO Auto-generated method stub
    	while(phone.moveToNext()){
        	String phoneName   = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        	String phoneNo = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        	Contacts phoneObject = new Contacts();
        	phoneObject.setName(phoneName);
        	phoneObject.setPhoneNo(phoneNo);
        	list.add(phoneObject);
        }
        phone.close();
        ContactsAdapter contactsAdapter = new ContactsAdapter(
        		this, R.layout.contacts_row, list);
        
        contactList.setAdapter(contactsAdapter);
        if(list != null && list.size() != 0){
        	Collections.sort(list, new Comparator<Contacts>() {
				@Override
				public int compare(Contacts lhs, Contacts rhs) {
					// TODO Auto-generated method stub
					return lhs.getName().compareTo(rhs.getName());
				}        		
			});
        }
        contactList.setFastScrollEnabled(true);
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View arg1, int position, long id) {
		// TODO Auto-generated method stub
		Contacts contact = (Contacts) listView.getItemAtPosition(position);
		showCallDialog(contact.getName(), contact.getPhoneNo());
		arg1.setBackgroundResource(R.drawable.bg_row);
	}
	
	private void setCountryCode(){
		Spinner spin = (Spinner) findViewById(R.id.spin_country);
	}
	private void showCallDialog(String name, final String phoneNo) {
		// TODO Auto-generated method stub
		AlertDialog alert = pop.showAlert(MainActivity.this, "Wish to Contact", getString(R.string.label_are_you_sure)+ " " + name + "?");
		//AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
		contactName = name;
		contactNumber = phoneNo;

		//alert.setMessage(getString(R.string.label_are_you_sure)+ " " + name + "?");
			
		alert.setButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {
		    @Override
			public void onClick(DialogInterface dialog, int which) {
				initiateCall();
			}
		});
		alert.setButton2(getString(R.string.label_no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.dismiss();
			}
		});
		alert.show();
		setCountryCode();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_profile:
			startActivity(new Intent(this, Profile.class));
			break;
			
		case R.id.action_call:
			showDialog(R.id.action_call);
			break;
			
       case R.id.action_settings:
        	startActivity(new Intent(this, Settings.class));
        	break;
        	
        case R.id.action_rates:
        	startActivity(new Intent(this, CallRates.class));
        	break;	
        
        case R.id.action_logs:
        	startActivity(new Intent(this, CallLogs.class));
        	break;
       	
		case R.id.action_logout:
			//http://stackoverflow.com/questions/13762248/reset-default-values-of-preference
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = preferences.edit();
			editor.clear();
			editor.commit();
	    	//stopSession();
	    	finish();
	    	break;
	    }return true;        	
    }
}