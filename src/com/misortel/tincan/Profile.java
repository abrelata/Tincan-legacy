package com.tincan.tincan;

import com.tincan.tincan.adapter.AlertManager;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;

public class Profile extends PreferenceActivity implements OnPreferenceClickListener{
	private Preference problems;
	private EditTextPreference uname, pass;
	private AlertManager pop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.profile);
		
		problems = findPreference("settings_forget");
		uname = (EditTextPreference) findPreference("settings_username");
		pass = (EditTextPreference) findPreference("settings_password");
		problems.setOnPreferenceClickListener(this);
		if (!uname.getText().toString().equals("") && !uname.getText().toString().equals("")){
			((PreferenceGroup) findPreference("settings_account")).removePreference(problems);
		}
		pop = new AlertManager();
	}

	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		if(preference == problems){
			pop.showToast(this, getString(R.string.alert_problem_account), 30);
		}
		return false;
	}
}