package org.safermobile.intheclear;

import java.io.File;
import java.util.ArrayList;

import org.safermobile.intheclear.screens.FolderSelector;

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
		
		cbp = (CheckBoxPreference) findPreference("DefaultWipeFolders");
		cbp.setOnPreferenceClickListener(this);
		
		pc = (PreferenceCategory) findPreference(ITCConstants.Preference.WIPERCAT);
		
		pc.removePreference((EditTextPreference) findPreference("DefaultWipeFoldersList"));
		pc.removePreference((CheckBoxPreference) findPreference("IsVirginUser"));
		
		checkedFolders = new ArrayList<File>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ITCConstants.Results.SELECTED_FOLDERS) {
			if(data.hasExtra("selectedFolders")) {
				checkedFolders.clear();
				checkedFolders = (ArrayList<File>) data.getSerializableExtra("selectedFolders");
				
				StringBuffer sb = new StringBuffer();
				for(File f : checkedFolders) {
					sb.append(f.getPath() + ";");
				}
				_ed.putString(ITCConstants.Preference.DEFAULT_WIPE_FOLDER_LIST, sb.toString());
				_ed.commit();
			}
		}
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		if(preference == cbp) {
			if(cbp.isChecked()) {
				Intent i = new Intent(this,FolderSelector.class);
				if(checkedFolders != null && checkedFolders.size() > 0) {
					i.putExtra("selectedFolders",checkedFolders);
				}
				startActivityForResult(i,ITCConstants.Results.SELECTED_FOLDERS);
			}
		}
		return false;
	}
	
}