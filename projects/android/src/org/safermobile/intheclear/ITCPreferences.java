package org.safermobile.intheclear;

import java.io.File;
import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.itcprefs);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		_ed = _sp.edit();
		
		pc = (PreferenceCategory) findPreference(ITCConstants.Preference.WIPERCAT);
		pc.removePreference((CheckBoxPreference) findPreference("IsVirginUser"));
		
		checkedFolders = new ArrayList<File>();
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