package org.safermobile.intheclear;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceActivity;
import android.util.Log;

public class ITCPreferences extends PreferenceActivity implements OnPreferenceClickListener {
	ArrayList<File> checkedFolders;
	
	SharedPreferences _sp;
	SharedPreferences.Editor _ed;
	CheckBoxPreference cbp;
	PreferenceCategory pc;
	ListPreference lang;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.itcprefs);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		_ed = _sp.edit();
		
		pc = (PreferenceCategory) findPreference(ITCConstants.Preference.WIPERCAT);
		pc.removePreference((CheckBoxPreference) findPreference("IsVirginUser"));
		
		lang = (ListPreference) findPreference(ITCConstants.Preference.DEFAULT_LANGUAGE);
		lang.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference pref, Object obj) {
				restartWithNewLocale(((ListPreference) pref).getValue());
				return true;
			}
			
		});
		
		checkedFolders = new ArrayList<File>();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	public void restartWithNewLocale(String localeCode) {
		Locale loc = new Locale(localeCode);
		Locale.setDefault(loc);
		Configuration config = new Configuration();
		config.locale = loc;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		Log.d(ITCConstants.Log.ITC,"current configuration = " + getBaseContext().getResources().getConfiguration().locale);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ITCConstants.Results.PREFERENCES_UPDATED) {
			if(data.hasExtra("newWipeSelection")) {
				// TODO: iterate through new wipe selection and update preferences accordingly
			}
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		return false;
	}
	
}