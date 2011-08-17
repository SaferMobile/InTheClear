package org.safermobile.intheclear.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.PanicController;
import org.safermobile.intheclear.controllers.WipeController;
import org.safermobile.intheclear.controllers.WipeController.LocalBinder;
import org.safermobile.intheclear.screens.WipePreferences;
import org.safermobile.intheclear.ui.WipeDisplay;
import org.safermobile.intheclear.ui.WipeDisplayAdaptor;
import org.safermobile.utils.EndActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Wipe extends Activity implements OnClickListener {
	private SharedPreferences _sp;
	SharedPreferences.Editor _ed;
	
	Button wipeButton,overrideWipePreferences;
	ListView wipeDisplay;
	LinearLayout wipeDisplayHolder;
	
	Map<Integer,Boolean> wipePreferences = new HashMap<Integer,Boolean>();
	boolean shouldWipePhotos,shouldWipeContacts,shouldWipeCallLog,shouldWipeSMS,shouldWipeCalendar,shouldWipeFolders;
	
	private WipeController wc;
	BroadcastReceiver killReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
				Intent toKill = new Intent(Wipe.this,EndActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finish();
				startActivity(toKill);
			
		}
		
	};
	IntentFilter killFilter = new IntentFilter();
	boolean isBound = false;
	
	private ServiceConnection sc = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName cn, IBinder binder) {
			LocalBinder lb = (WipeController.LocalBinder) binder;
			wc = lb.getService();
			isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName cn) {
			wc = null;
			isBound = false;
			
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wipe);
				
		wipeButton = (Button) findViewById(R.id.wipeButton);
		wipeButton.setOnClickListener(this);
		
		overrideWipePreferences = (Button) findViewById(R.id.overrideWipePreferences);
		overrideWipePreferences.setOnClickListener(this);
		
		wipeDisplayHolder = (LinearLayout) findViewById(R.id.wipeDisplayHolder);
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		bindWipeService();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		alignPreferences();
		killFilter.addAction(this.getClass().toString());
		registerReceiver(killReceiver,killFilter);
	}
	
	@Override
	public void onStop() {
		updatePreferences();
		unbindWipeService();
		unregisterReceiver(killReceiver);
		super.onStop();
	}
	
	private void alignPreferences() {		
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

		redrawWipeList();
	}
	
	private void updatePreferences() {
		_ed = _sp.edit();
		_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_CONTACTS, shouldWipeContacts).commit();
		_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_PHOTOS, shouldWipePhotos).commit();
		_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALLLOG, shouldWipeCallLog).commit();
		_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_SMS, shouldWipeSMS).commit();
		_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALENDAR, shouldWipeCalendar).commit();
		_ed.putBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, shouldWipeFolders).commit();
	}
	
	private void redrawWipeList() {
		if(wipeDisplay != null)
			wipeDisplayHolder.removeView(wipeDisplay);
		
		wipeDisplay =  new ListView(this);
		wipeDisplay.setDividerHeight(0);
		ArrayList<WipeDisplay> wipeDisplayList = new ArrayList<WipeDisplay>();

		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_WIPECONTACTS),shouldWipeContacts,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_WIPEPHOTOS),shouldWipePhotos,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_CALLLOG),shouldWipeCallLog,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_SMS),shouldWipeSMS,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_CALENDAR),shouldWipeCalendar,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_SDCARD),shouldWipeFolders,this));
		wipeDisplay.setAdapter(new WipeDisplayAdaptor(this,wipeDisplayList));
		
		wipeDisplayHolder.addView(wipeDisplay);
	}
	
	private void doWipe() {
		
		// wipe baby!
		wc.wipePIMData(
			this,
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
				
				redrawWipeList();
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
	
	private void bindWipeService() {
		try {
			if(!isBound) {
				bindService(new Intent(Wipe.this,WipeController.class),sc,Context.BIND_AUTO_CREATE);
				isBound = true;
			}
		} catch(IllegalArgumentException e) {}
	}
	
	private void unbindWipeService() {
		try {
			if(isBound)
				unbindService(sc);
				isBound = false;
				Log.d(ITCConstants.Log.ITC,"service is now unbound.");
		} catch(IllegalArgumentException e) {}	
	}
}
