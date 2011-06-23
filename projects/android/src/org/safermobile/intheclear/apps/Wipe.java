package org.safermobile.intheclear.apps;

import java.io.File;
import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.WipeController;
import org.safermobile.intheclear.screens.FolderSelector;
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

public class Wipe extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	
	Button wipeButton,viewSelectedFolders;
	ListView checkBoxHolder;
	ArrayList<WipeSelector> wipeSelector;
	ArrayList<File> checkedFolders;
	
	WipeController wc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
		
		wipeButton = (Button) findViewById(R.id.wipeButton);
		wipeButton.setOnClickListener(this);
		
		viewSelectedFolders = (Button) findViewById(R.id.viewSelectedFolders);
		viewSelectedFolders.setOnClickListener(this);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean shouldWipeContacts = _sp.getBoolean("DefaultWipeContacts",false);
		boolean shouldWipePhotos = _sp.getBoolean("DefaultWipePhotos", false);
		boolean shouldWipeCallLog = _sp.getBoolean("DefaultWipeCallLog", false);
		boolean shouldWipeSMS = _sp.getBoolean("DefaultWipeSMS", false);
		boolean shouldWipeCalendar = _sp.getBoolean("DefaultWipeCalendar", false);
		
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

		checkBoxHolder = (ListView) findViewById(R.id.checkBoxHolder);
		checkBoxHolder.setAdapter(new WipeArrayAdaptor(this, wipeSelector));
	}
	
	private void doWipe() {
		// iterate through options to see what's checked
		checkedFolders = new ArrayList<File>();

		
		// create a wipe controller instance
		wc = new WipeController(getBaseContext());
		
		// wipe baby!
		wc.wipePIMData(
				wipeSelector.get(0).getSelected(),
				wipeSelector.get(1).getSelected(),
				wipeSelector.get(2).getSelected(),
				wipeSelector.get(3).getSelected(),
				checkedFolders);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ITCConstants.Results.SELECTED_FOLDERS) {
			if(data.hasExtra("selectedFolders")) {
				checkedFolders = (ArrayList<File>) data.getSerializableExtra("selectedFolders");
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(v == wipeButton) {
			doWipe();
		} else if(v == viewSelectedFolders) {
			Intent i = new Intent(this,FolderSelector.class);
			if(checkedFolders != null && checkedFolders.size() > 0) {
				i.putExtra("selectedFolders", checkedFolders);
			}
			startActivityForResult(i,ITCConstants.Results.SELECTED_FOLDERS);
		}
	}
}
