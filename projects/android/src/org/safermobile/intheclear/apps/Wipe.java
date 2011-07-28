package org.safermobile.intheclear.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.WipeController;
import org.safermobile.intheclear.screens.WipePreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Wipe extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	
	Button wipeButton,overrideWipePreferences;
	ListView checkBoxHolder;
	TextView checkedFolderDialog;
	
	Map<Integer,Boolean> wipePreferences = new HashMap<Integer,Boolean>();
	boolean shouldWipePhotos,shouldWipeContacts,shouldWipeCallLog,shouldWipeSMS,shouldWipeCalendar,shouldWipeFolders;
	
	WipeController wc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
		
		wipeButton = (Button) findViewById(R.id.wipeButton);
		wipeButton.setOnClickListener(this);
		
		overrideWipePreferences = (Button) findViewById(R.id.overrideWipePreferences);
		overrideWipePreferences.setOnClickListener(this);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		alignPreferences();
	}
	
	private void alignPreferences() {
		Log.d(ITCConstants.Log.ITC,"WE ARE REALIGNING PREFs");
		shouldWipeContacts = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CONTACTS,false);
		wipePreferences.put(ITCConstants.Wipe.CONTACTS, shouldWipeContacts);
		
		shouldWipePhotos = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_PHOTOS, false);
		wipePreferences.put(ITCConstants.Wipe.PHOTOS, shouldWipePhotos);
		
		shouldWipeCallLog = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALLLOG, false);
		wipePreferences.put(ITCConstants.Wipe.CALLLOG, shouldWipeCallLog);
		
		shouldWipeSMS = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_SMS, false);
		wipePreferences.put(ITCConstants.Wipe.SMS, shouldWipeSMS);
		
		shouldWipeCalendar = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALENDAR, false);
		wipePreferences.put(ITCConstants.Wipe.CALENDAR, shouldWipeCalendar);
		
		shouldWipeFolders = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, false);
		wipePreferences.put(ITCConstants.Wipe.SDCARD, shouldWipeFolders);
	}
	
	private void doWipe() {
		// create a wipe controller instance
		wc = new WipeController(getBaseContext());
		
		// wipe baby!
		wc.wipePIMData(
			shouldWipeContacts,
			shouldWipePhotos,
			shouldWipeCallLog,
			shouldWipeSMS,
			shouldWipeCalendar,
			shouldWipeFolders);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ITCConstants.Results.OVERRIDE_WIPE_PREFERENCES) {
			if(data.hasExtra(ITCConstants.Preference.WIPE_SELECTOR)) {	
				wipePreferences = 
					((ArrayList<Map<Integer,Boolean>>)data.getSerializableExtra(ITCConstants.Preference.WIPE_SELECTOR)).get(0);
				
				shouldWipeContacts = wipePreferences.get(ITCConstants.Wipe.CONTACTS);
				shouldWipePhotos = wipePreferences.get(ITCConstants.Wipe.PHOTOS);
				shouldWipeCallLog = wipePreferences.get(ITCConstants.Wipe.CALLLOG);
				shouldWipeSMS = wipePreferences.get(ITCConstants.Wipe.SMS);
				shouldWipeCalendar = wipePreferences.get(ITCConstants.Wipe.CALENDAR);
				shouldWipeFolders = wipePreferences.get(ITCConstants.Wipe.SDCARD);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == wipeButton) {
			doWipe();
		} else if(v == overrideWipePreferences) {
			ArrayList<Map<Integer,Boolean>> wp = new ArrayList<Map<Integer,Boolean>>();
			wp.add(wipePreferences);
			
			Intent i = new Intent(this,WipePreferences.class);
			i.putExtra(ITCConstants.Preference.WIPE_SELECTOR, wp);
			startActivityForResult(i,ITCConstants.Results.OVERRIDE_WIPE_PREFERENCES);
		}
	}
}
