package org.safermobile.intheclear.apps;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.PanicController;
import org.safermobile.intheclear.controllers.ShoutController;
import org.safermobile.intheclear.controllers.WipeController;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Panic extends Activity implements OnClickListener, OnDismissListener {
	SharedPreferences _sp;
	TextView panicReadout,panicProgress,countdownReadout;
	Button controlPanic,cancelCountdown,panicControl;
	
	Dialog countdown;
	
	boolean oneTouchPanic,shouldWipePhotos,shouldWipeContacts,shouldWipeCallLog,shouldWipeSMS,shouldWipeCalendar,shouldWipeFolders;
	boolean canContinuePanicing;
	
	String userDisplayName,defaultPanicMsg,configuredFriends;
	
	ShoutController sc;
	WipeController wc;
	
	ArrayList<File> selectedFolders;
	
	int panicState = ITCConstants.PanicState.AT_REST;
	int t;
	CountDownTimer cd;
	
	Intent panic;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panic);
		
		canContinuePanicing = false;
		
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		sc = new ShoutController(this);
		wc = new WipeController(getBaseContext());
		
		panicReadout = (TextView) findViewById(R.id.panicReadout);
		panicControl = (Button) findViewById(R.id.panicControl);
		
		panic = new Intent(this,PanicController.class);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		alignPreferences();
		if(!oneTouchPanic) {
			panicControl.setText(this.getResources().getString(R.string.KEY_PANIC_BTN_PANIC));
			panicControl.setOnClickListener(this);
		} else {
			doPanic();
		}
	}
	
	@Override
	public void onDestroy() {
		Log.d(ITCConstants.Log.ITC,"um... destroyed?");
		super.onDestroy();
	}
	
	private void alignPreferences() {
		selectedFolders = new ArrayList<File>();
		
		oneTouchPanic = _sp.getBoolean(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC, false);
		
		defaultPanicMsg = _sp.getString(ITCConstants.Preference.DEFAULT_PANIC_MSG, "");
		userDisplayName = _sp.getString(ITCConstants.Preference.USER_DISPLAY_NAME, "");
				
		panicReadout.setText(
				this.getResources().getString(R.string.KEY_PANIC_MSG_TITLE) +
				"\n\n" + defaultPanicMsg +
				"\n\n" + sc.buildShoutData(userDisplayName)
		);

		shouldWipeContacts = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CONTACTS, false);
		shouldWipePhotos = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_PHOTOS, false);
		shouldWipeCallLog = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALLLOG, false);
		shouldWipeSMS = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_SMS, false);
		shouldWipeCalendar = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALENDAR, false);
		
		shouldWipeFolders = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, false);
		if(shouldWipeFolders) {
			String cf = _sp.getString(ITCConstants.Preference.DEFAULT_WIPE_FOLDER_LIST, "");
			StringTokenizer st = new StringTokenizer(cf, ";");
			while(st.hasMoreTokens())
				selectedFolders.add(new File(st.nextToken()));
		}
		
		configuredFriends = _sp.getString(ITCConstants.Preference.CONFIGURED_FRIENDS, "");
		
	}
	
	public void cancelPanic() {
		switch(panicState) {
		case ITCConstants.PanicState.IN_COUNTDOWN:
			cd.cancel();
			canContinuePanicing = false;
			panicState = ITCConstants.PanicState.AT_REST;
			break;
		case ITCConstants.PanicState.IN_CONTINUED_PANIC:
			stopService(new Intent(panic));
			canContinuePanicing = false;
			panicState = ITCConstants.PanicState.AT_REST;
			break;
		default:
			canContinuePanicing = false;
			panicState = ITCConstants.PanicState.AT_REST;
			break;
		}
		countdown.dismiss();
	}
	
	private void doPanic() {
		canContinuePanicing = true;
		panicState++;
		
		panicControl.setText(getString(R.string.KEY_PANIC_MENU_CANCEL));
		
		countdown = new Dialog(this);
		countdown.setContentView(R.layout.countdown);
		countdown.setCancelable(false);
		countdown.setOnDismissListener(this);
		
		countdownReadout = (TextView) countdown.findViewById(R.id.countdownReadout);
		
		cancelCountdown = (Button) countdown.findViewById(R.id.cancelCountdown);
		cancelCountdown.setText(R.string.KEY_PANIC_MENU_CANCEL);
		cancelCountdown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v == cancelCountdown) {
					cancelPanic();
				}
			}
		});
		countdown.show();

		t = 0;
		cd = new CountDownTimer(ITCConstants.Duriation.COUNTDOWN, ITCConstants.Duriation.COUNTDOWNINTERVAL) {
			@Override
			public void onFinish() {
				// Send the first shout
				if(canContinuePanicing) {
					panicState++;
					Log.d(ITCConstants.Log.ITC,"panic state: " + panicState);
					sc.sendSMSShout(configuredFriends, defaultPanicMsg, sc.buildShoutData(userDisplayName));
				}
				
				// Perform wipe
				if(canContinuePanicing) {
					panicState++;
					Log.d(ITCConstants.Log.ITC,"panic state: " + panicState);

					countdownReadout.setText(getString(R.string.KEY_PANIC_PROGRESS_2));
					Log.d(ITCConstants.Log.ITC,"Wiping... " + _sp.getString(ITCConstants.Preference.DEFAULT_WIPE_FOLDER_LIST, ""));
					wc.wipePIMData(
							shouldWipeContacts,
							shouldWipePhotos,
							shouldWipeCallLog,
							shouldWipeSMS,
							shouldWipeCalendar,
							selectedFolders
					);
				}
				
				// Start Service for continued panic
				if(canContinuePanicing) {
					panicState++;
					Log.d(ITCConstants.Log.ITC,"panic state: " + panicState);
					countdownReadout.setText(getString(R.string.KEY_PANIC_PROGRESS_3));
					startService(new Intent(panic));
				}
			}

			@Override
			public void onTick(long countDown) {
				String secondString = 
					getString(R.string.KEY_PANIC_COUNTDOWNMSG) + " " + (5 - t) +
					" " + getString(R.string.KEY_SECONDS);
				Log.d(ITCConstants.Log.ITC,secondString);
				countdownReadout.setText(getString(R.string.KEY_PANIC_PROGRESS_1) + "\n" + secondString);
				t++;
			}
		};
		cd.start();
	}
	

	@Override
	public void onClick(View v) {
		if(v == panicControl && panicState == ITCConstants.PanicState.AT_REST) {
			doPanic();
		} else if (v == panicControl && panicState != ITCConstants.PanicState.AT_REST) {
			cancelPanic();
		}
		
	}

	@Override
	public void onDismiss(DialogInterface d) {
		cancelPanic();
	}
}
