package org.safermobile.intheclear;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class ITCPreferences extends PreferenceActivity implements OnPreferenceClickListener {
	Preference savePrefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.itcprefs);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		return false;
	}

}
