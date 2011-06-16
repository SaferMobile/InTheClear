package org.safermobile.intheclear;

import java.io.File;
import java.util.ArrayList;

import org.safermobile.intheclear.ui.FolderIterator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class ITCPreferences extends PreferenceActivity implements OnPreferenceClickListener {
	Preference savePrefs;
	ArrayList<File> allDirs;
	
	SharedPreferences _sp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.itcprefs);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		new FolderIterator();
		allDirs = FolderIterator.updateListPreference();
		
		for(File f : allDirs) {
			// TODO: adding the selected folders to shared preferences.
		}
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		return false;
	}

}
