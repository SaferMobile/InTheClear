package org.safermobile.intheclear.apps;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.WipeController;
import org.safermobile.intheclear.ui.WipeArrayAdaptor;
import org.safermobile.intheclear.ui.WipeSelector;

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
	
	Button wipeButton,viewSelectedFolders;
	ListView checkBoxHolder;
	TextView checkedFolderDialog;
	ArrayList<WipeSelector> wipeSelector;
	ArrayList<File> checkedFolders;
	
	boolean shouldWipePhotos,shouldWipeContacts,shouldWipeCallLog,shouldWipeSMS,shouldWipeCalendar,shouldWipeFolders;
	
	WipeController wc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
		
		wipeButton = (Button) findViewById(R.id.wipeButton);
		wipeButton.setOnClickListener(this);
		
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
		shouldWipePhotos = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_PHOTOS, false);
		shouldWipeCallLog = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALLLOG, false);
		shouldWipeSMS = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_SMS, false);
		shouldWipeCalendar = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALENDAR, false);
		shouldWipeFolders = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, false);
		shouldWipeFolders = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, false);
		
		wipeSelector = new ArrayList<WipeSelector>();
		
		wipeSelector.add(new WipeSelector(
				getString(R.string.KEY_WIPE_WIPECONTACTS), 
				ITCConstants.Wipe.CONTACTS, 
				shouldWipeContacts));
		wipeSelector.add(
				new WipeSelector(
						getString(R.string.KEY_WIPE_WIPEPHOTOS), 
						ITCConstants.Wipe.PHOTOS, 
						shouldWipePhotos));
		wipeSelector.add(new WipeSelector(
				getString(R.string.KEY_WIPE_CALLLOG),
				ITCConstants.Wipe.CALLLOG,
				shouldWipeCallLog));
		wipeSelector.add(new WipeSelector(
				getString(R.string.KEY_WIPE_SMS),
				ITCConstants.Wipe.SMS,
				shouldWipeSMS));
		wipeSelector.add(new WipeSelector(
				getString(R.string.KEY_WIPE_CALENDAR),
				ITCConstants.Wipe.CALENDAR,
				shouldWipeCalendar));
		wipeSelector.add(new WipeSelector(
				getString(R.string.KEY_WIPE_SDCARD),
				ITCConstants.Wipe.SDCARD,
				shouldWipeFolders));
		
		checkBoxHolder.setAdapter(new WipeArrayAdaptor(this, wipeSelector));		
	}
	
	private void doWipe() {
		// create a wipe controller instance
		wc = new WipeController(getBaseContext());
		
		// wipe baby!
		wc.wipePIMData(
				wipeSelector.get(0).getSelected(),
				wipeSelector.get(1).getSelected(),
				wipeSelector.get(2).getSelected(),
				wipeSelector.get(3).getSelected(),
				wipeSelector.get(4).getSelected(),
				wipeSelector.get(5).getSelected()
				);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ITCConstants.Results.OVERRIDE_WIPE_PREFERENCES) {
			if(data.hasExtra("newWipeSelection")) {	
				// TODO: iterate through new wipe selection
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == wipeButton) {
			doWipe();
		} else if(v == viewSelectedFolders) {
			Intent i = new Intent(this,WipeSelector.class);
			i.putExtra("wipeSelection", checkedFolders);
			startActivityForResult(i,ITCConstants.Results.OVERRIDE_WIPE_PREFERENCES);
		}
	}
}
