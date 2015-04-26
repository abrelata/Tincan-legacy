package com.misortel.tincan;

import com.misortel.tincan.adapter.AlertManager;
import com.misortel.tincan.adapter.SessionManager;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class Settings extends PreferenceActivity implements OnPreferenceClickListener{
	private Preference internet, cellular, about;
	private SessionManager session;
	private Bundle bundle;
	private AlertManager pop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
		session = new SessionManager(getApplicationContext());
		internet = findPreference("settings_internet");
		cellular = findPreference("settings_cellular");
		about = findPreference("settings_about");
		bundle = getIntent().getExtras();
		
		//customer.setOnPreferenceClickListener(this);
	    //about.setOnPreferenceClickListener(this);
		if(session.isOnline())
	    	internet.setSummary(R.string.label_online);
	    else
	    	internet.setSummary(R.string.label_offline);	
		
		cellular.setOnPreferenceClickListener(this);
		about.setOnPreferenceClickListener(this);
	}

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		// TODO Auto-generated method stub
		if(arg0 == cellular){
			//only works on JB
			/*Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
			startActivity(intent);*/
			
			//2.1
			Intent intent = new Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
			final ComponentName cName = new ComponentName("com.android.phone", "com.android.phone.Settings");
			intent.setComponent(cName);
			startActivity(intent);
		}
		if(arg0 == about){
			startActivity(new Intent(this, About.class));
		}
		return false;
	}

	/*@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		if(preference == settings){
			PreferenceScreen a = (PreferenceScreen) preference;
            a.getDialog().getWindow().setBackgroundDrawableResource(R.color.white);
		}
		return false;
	}*/
}