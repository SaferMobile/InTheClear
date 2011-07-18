package org.safermobile.intheclear.apps;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.controllers.PanicController;
import org.safermobile.intheclear.controllers.PanicController.LocalBinder;
import org.safermobile.utils.EndActivity;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;

public class Panic extends Activity implements OnClickListener, OnDismissListener {
	
	SharedPreferences _sp;
	boolean oneTouchPanic;
	
	TextView panicReadout,panicProgress,countdownReadout;
	Button controlPanic,cancelCountdown,panicControl;	
	
	
	Intent panic,toKill;
	int panicState = ITCConstants.PanicState.AT_REST;
	
	Dialog countdown;
	CountDownTimer cd;

	private PanicController pc;
	boolean isBound = false;

	private ServiceConnection sc = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName cn, IBinder binder) {
			LocalBinder lb = (PanicController.LocalBinder) binder;
			pc = lb.getService();
			isBound = true;
			panicReadout.setText(pc.returnPanicData());
			Log.d(ITCConstants.Log.ITC,"i bound the service");
		}

		public void onServiceDisconnected(ComponentName cn) {
			pc = null;
		}
		
	};
	
	private BroadcastReceiver panicReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panic);
				
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		panicReadout = (TextView) findViewById(R.id.panicReadout);
		panicControl = (Button) findViewById(R.id.panicControl);
		
		bindService(new Intent(Panic.this,PanicController.class),sc,Context.BIND_AUTO_CREATE);
		panicReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.hasExtra(ITCConstants.UPDATE_UI)) {
					String message = intent.getStringExtra(ITCConstants.UPDATE_UI);
					Log.d(ITCConstants.Log.ITC,message);
					countdownReadout.setText(message);
				}
				
			}
			
		};
	}
	
	@Override
	public void onResume() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Panic.class.getName());
		registerReceiver(panicReceiver,filter);
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
	
	@Override
	public void onNewIntent(Intent i) {
		super.onNewIntent(i);
		setIntent(i);

		if(i.hasExtra("PanicCount"))
			Log.d(ITCConstants.Log.ITC,"Panic Count at: " + i.getIntExtra("PanicCount",0));
	}
	
	@Override
	public void onPause() {
		unregisterReceiver(panicReceiver);
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		unbindPanicService();
		super.onDestroy();
	}
	
	private void alignPreferences() {		
		oneTouchPanic = _sp.getBoolean(ITCConstants.Preference.DEFAULT_ONE_TOUCH_PANIC, false);
	}
	
	public void cancelPanic() {
		if(panicState == ITCConstants.PanicState.IN_COUNTDOWN) {
			// if panic hasn't started, then just kill the countdown
			cd.cancel();
			countdown.dismiss();
		}
		
		unbindPanicService();
		
		toKill = new Intent(this,EndActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
		startActivity(toKill);
		
	}
	
	private void doPanic() {
		// setup broadcast receiver to service
		
		
		// countdown to panic (5,4,3,2,1...)
		launchPanic();
		
		// tell service to start panicing
		
			
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
		countdownReadout.setText(message);
	}
	
	private void launchPanic() {		
		panicState = ITCConstants.PanicState.IN_COUNTDOWN;
		panicControl.setText(getString(R.string.KEY_PANIC_MENU_CANCEL));
		cd = new CountDownTimer(ITCConstants.Duriation.COUNTDOWN,ITCConstants.Duriation.COUNTDOWNINTERVAL) {
			int t = 5;
			
			@Override
			public void onFinish() {
				// start the panic
				pc.startPanic();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				Panic.this.updateProgressWindow(
						getString(R.string.KEY_PANIC_COUNTDOWNMSG) + 
						" " + t + " " +	
						getString(R.string.KEY_SECONDS)
				);
				t--;
			}
			
		};
		
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
		cd.start();
	}
}
