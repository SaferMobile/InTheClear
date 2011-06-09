package org.safermobile.intheclear;

import java.util.ArrayList;
import java.util.Vector;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class ITCPreferences extends PreferenceActivity implements OnPreferenceClickListener {
	Preference savePrefs;
	ArrayList<String> allDirs;
	
	SharedPreferences _sp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.itcprefs);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor ed = _sp.edit();
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		return false;
	}

}
