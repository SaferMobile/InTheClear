package org.safermobile.intheclear.screens;

import java.util.ArrayList;

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
				true
		));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_WIPECONTACTS), 
				ITCConstants.Wipe.CONTACTS, 
				false));
		wipeOptions.add(new WipeSelector(
						getResources().getString(R.string.KEY_WIPE_WIPEPHOTOS), 
						ITCConstants.Wipe.PHOTOS, 
						false));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_CALLLOG),
				ITCConstants.Wipe.CALLLOG,
				false));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_SMS),
				ITCConstants.Wipe.SMS,
				false));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_CALENDAR),
				ITCConstants.Wipe.CALENDAR,
				false));
		wipeOptions.add(new WipeSelector(
				getResources().getString(R.string.KEY_WIPE_SDCARD),
				ITCConstants.Wipe.SDCARD,
				false
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
			// put the selected wipe options
			
			// put the selected folders
			
			// return to calling activity
			setResult(RESULT_OK,i);
			finish();
		}
		
	}

}
