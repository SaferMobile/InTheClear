package org.safermobile.intheclear.screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.ui.WipeArrayAdaptor;
import org.safermobile.intheclear.ui.WipeSelector;
import org.safermobile.intheclear.ui.WipeSelector.Color;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class WipePreferences extends Activity implements OnClickListener {
	Button confirmSelection;
	ArrayList<WipeSelector> wipeOptions;
	ListView list;
	
	boolean defaultContacts, defaultPhotos, defaultCallLog, defaultSMS, defaultCalendar, defaultSDCard = false;
	boolean defaultNone = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe_selector);
		
		confirmSelection = (Button) findViewById(R.id.confirmSelection);
		confirmSelection.setOnClickListener(this);
		
		wipeOptions = new ArrayList<WipeSelector>();
		
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_NONE),
				ITCConstants.Wipe.NONE,
				defaultNone
		));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_WIPECONTACTS), 
				ITCConstants.Wipe.CONTACTS, 
				defaultContacts));
		wipeOptions.add(new WipeSelector(
						getResources().getString(R.string.KEY_WIPE_WIPEPHOTOS), 
						ITCConstants.Wipe.PHOTOS, 
						defaultPhotos));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_CALLLOG),
				ITCConstants.Wipe.CALLLOG,
				defaultCallLog));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_SMS),
				ITCConstants.Wipe.SMS,
				defaultSMS));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_CALENDAR),
				ITCConstants.Wipe.CALENDAR,
				defaultCalendar));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_SDCARD),
				ITCConstants.Wipe.SDCARD,
				defaultSDCard
		));
		
		for(WipeSelector w : wipeOptions) {
			if(w.getWipeType() == ITCConstants.Wipe.NONE) {
				w.setColor(Color.SELECTABLE);
				w.setIsToggleControl(true);
			}
		}
		
		list = (ListView) findViewById(R.id.wipeSelectionHolder);
		list.setAdapter(new WipeArrayAdaptor(this,wipeOptions));
	}
	
	@Override
	public void onClick(View v) {
		if(v == confirmSelection) {
			Intent i = new Intent();
			ArrayList<Map<Integer,Boolean>> wipePreferencesHolder = new ArrayList<Map<Integer,Boolean>>();
			Map<Integer,Boolean> wipePreferences = new HashMap<Integer,Boolean>();
			for(WipeSelector ws : wipeOptions) {
				wipePreferences.put(ws.getWipeType(), ws.getSelected());
			}
			wipePreferencesHolder.add(wipePreferences);
			i.putExtra(ITCConstants.Preference.WIPE_SELECTOR, wipePreferencesHolder);
			setResult(RESULT_OK,i);
			finish();
		}
		
	}

}
