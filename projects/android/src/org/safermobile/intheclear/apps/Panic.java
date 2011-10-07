package org.safermobile.intheclear.apps;

import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.ITCPreferences;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.PanicController;
import org.safermobile.intheclear.controllers.PanicController.LocalBinder;
import org.safermobile.intheclear.ui.WipeDisplay;
import org.safermobile.intheclear.ui.WipeDisplayAdaptor;
import org.safermobile.utils.EndActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Panic extends Activity implements OnClickListener, OnDismissListener {
	
	SharedPreferences _sp;
	boolean oneTouchPanic;
	
	TextView panicReadout,panicProgress,countdownReadout;
	ListView wipeDisplayList;
	Button controlPanic,cancelCountdown,panicControl;	
	
	Intent panic,toKill;
	int panicState = ITCConstants.PanicState.AT_REST;
	
	Dialog countdown;
	CountDownTimer cd;
	
	ProgressDialog panicStatus;
	String currentPanicStatus;

	private PanicController pc;
	boolean isBound = false;

	private ServiceConnection sc = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName cn, IBinder binder) {
			LocalBinder lb = (PanicController.LocalBinder) binder;
			pc = lb.getService();
			isBound = true;
			panicReadout.setText(pc.returnPanicData());
			wipeDisplayList.setAdapter(new WipeDisplayAdaptor(Panic.this,pc.returnWipeSettings()));
			
			Log.d(ITCConstants.Log.ITC,"i bound the service");
		}

		public void onServiceDisconnected(ComponentName cn) {
			pc = null;
		}
		
	};
	
	private BroadcastReceiver panicReceiver;
	private BroadcastReceiver killReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			killActivity();
		}
		
	};	
	IntentFilter killFilter = new IntentFilter();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panic);
				
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		panicReadout = (TextView) findViewById(R.id.panicReadout);
		panicControl = (Button) findViewById(R.id.panicControl);
		
		wipeDisplayList = (ListView) findViewById(R.id.wipeDisplayList);
				
		bindService(new Intent(Panic.this,PanicController.class),sc,Context.BIND_AUTO_CREATE);
		panicReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.hasExtra(ITCConstants.UPDATE_UI)) {
					String message = intent.getStringExtra(ITCConstants.UPDATE_UI);
					Log.d(ITCConstants.Log.ITC,message);
					updateProgressWindow(message);
				}
				
			}
			
		};
		panicStatus = new ProgressDialog(this);
		panicStatus.setButton(
				getResources().getString(R.string.KEY_PANIC_MENU_CANCEL), 
				new DialogInterface.OnClickListener() {
			
					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelPanic();
					}
				}
		);
		panicStatus.setMessage(currentPanicStatus);
		panicStatus.setTitle(getResources().getString(R.string.KEY_PANIC_BTN_PANIC));
		
	}
	
	@Override
	public void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Panic.class.getName());
		registerReceiver(panicReceiver,filter);
		
		killFilter.addAction(this.getClass().toString());
		registerReceiver(killReceiver,killFilter);
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
			panicControl.setText(getString(R.string.KEY_PANIC_MENU_CANCEL));
			panicControl.setOnClickListener(this);
			doPanic();
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void onNewIntent(Intent i) {
		super.onNewIntent(i);
		setIntent(i);
		
		if(i.hasExtra("ReturnFrom") && i.getIntExtra("ReturnFrom", 0) == ITCConstants.Panic.RETURN) {
			// the app is being launched from the notification tray.
			
			// update UI with the panic controller's status.
			updateProgressWindow(pc.getPanicProgress());
			
		}

		if(i.hasExtra("PanicCount"))
			Log.d(ITCConstants.Log.ITC,"Panic Count at: " + i.getIntExtra("PanicCount",0));
	}
	
	@Override
	public void onPause() {
		unregisterReceiver(panicReceiver);
		unregisterReceiver(killReceiver);
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		unbindPanicService();
		super.onDestroy();
	}
	
	private void alignPreferences() {
		oneTouchPanic = false;
		String recipients = _sp.getString(ITCConstants.Preference.CONFIGURED_FRIENDS,"");
		if(recipients.compareTo("") == 0) {
			AlertDialog.Builder d = new AlertDialog.Builder(this);
			d.setMessage(getResources().getString(R.string.KEY_SHOUT_PREFSFAIL))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.KEY_OK), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Panic.this.launchPreferences();
					}
				});
			AlertDialog a = d.create();
			a.show();
		} else {
			oneTouchPanic = _sp.getBoolean(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC, false);
		}
	}
	
	public void cancelPanic() {
		if(panicState == ITCConstants.PanicState.IN_COUNTDOWN) {
			// if panic hasn't started, then just kill the countdown
			cd.cancel();
		}
		
		unbindPanicService();
		
		toKill = new Intent(this,EndActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		startActivity(toKill);
		
	}
	
	private void unbindPanicService() {
		try {
			if(isBound)
				unbindService(sc);
		} catch(IllegalArgumentException e) {
			Log.d(ITCConstants.Log.ITC,"service is already unbound.  Finishing.");
		}	
		panicState = ITCConstants.PanicState.AT_REST;
		isBound = false;
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
		
	}
	
	public void updateProgressWindow(String message) {
		panicStatus.setMessage(message);
	}
	
	public void killActivity() {
		Intent toKill = new Intent(Panic.this,EndActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(toKill);
	}
	
	public void launchPreferences() {
		Intent toPrefs = new Intent(this,ITCPreferences.class);
		if(isBound)
			unbindPanicService();
		startActivity(toPrefs);
	}
	
	private void doPanic() {		
		panicState = ITCConstants.PanicState.IN_COUNTDOWN;
		panicControl.setText(getString(R.string.KEY_PANIC_MENU_CANCEL));
		cd = new CountDownTimer(ITCConstants.Duriation.COUNTDOWN,ITCConstants.Duriation.COUNTDOWNINTERVAL) {
			int t = 5;
			
			@SuppressWarnings("static-access")
			@Override
			public void onFinish() {
				// start the panic				
				pc.startPanic();
				
				// kill the activity
				killActivity();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				panicStatus.setMessage(
						getString(R.string.KEY_PANIC_COUNTDOWNMSG) + 
						" " + t + " " +	
						getString(R.string.KEY_SECONDS)
				);
				t--;
			}
			
		};
		
		panicStatus.show();
		cd.start();
		
	}
}
