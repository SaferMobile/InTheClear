package org.safermobile.intheclear.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.apps.Panic;
import org.safermobile.intheclear.ui.WipeDisplay;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class PanicController extends Service {
	private NotificationManager nm;
	SharedPreferences _sp;
	
	TimerTask tt,ui;
	Timer t = new Timer();
	Timer u = new Timer();
	final Handler h = new Handler();
	boolean isPanicing = false;
		
	Intent backToPanic;
	int panicCount = 0;
	
	WipeController wc;
	ShoutController sc;
	
	ArrayList<File> selectedFolders;
	ArrayList<WipeDisplay> wipeDisplayList;
	String userDisplayName,defaultPanicMsg,configuredFriends,panicData;
	boolean shouldWipePhotos,shouldWipeContacts,shouldWipeCallLog,shouldWipeSMS,shouldWipeCalendar,shouldWipeFolders;
		
	public class LocalBinder extends Binder {
		public PanicController getService() {
			return PanicController.this;
		}
	}
	
	private final IBinder binder = new LocalBinder();
	
	@Override
	public void onCreate() {
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		
		sc = new ShoutController(getBaseContext());
		wc = new WipeController(getBaseContext());
		wc.addCallbackTo(Panic.class.getName());
		backToPanic = new Intent(this,Panic.class);
		alignPreferences();
		showNotification();
	}
	
	private void alignPreferences() {
		_sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

		selectedFolders = new ArrayList<File>();
		defaultPanicMsg = _sp.getString(ITCConstants.Preference.DEFAULT_PANIC_MSG, "");
		userDisplayName = _sp.getString(ITCConstants.Preference.USER_DISPLAY_NAME, "");
		
		panicData = "\n\n" + defaultPanicMsg +
					"\n\n" + sc.buildShoutData();

		shouldWipeContacts = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CONTACTS, false);
		shouldWipePhotos = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_PHOTOS, false);
		shouldWipeCallLog = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALLLOG, false);
		shouldWipeSMS = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_SMS, false);
		shouldWipeCalendar = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_CALENDAR, false);
		shouldWipeFolders = _sp.getBoolean(ITCConstants.Preference.DEFAULT_WIPE_FOLDERS, false);
		
		configuredFriends = _sp.getString(ITCConstants.Preference.CONFIGURED_FRIENDS, "");
		
		wipeDisplayList = new ArrayList<WipeDisplay>();

		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_WIPECONTACTS),shouldWipeContacts,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_WIPEPHOTOS),shouldWipePhotos,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_CALLLOG),shouldWipeCallLog,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_SMS),shouldWipeSMS,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_CALENDAR),shouldWipeCalendar,this));
		wipeDisplayList.add(new WipeDisplay(getResources().getString(R.string.KEY_WIPE_SDCARD),shouldWipeFolders,this));
	}
	
	public String returnPanicData() {
		return panicData;
	}
	
	public ArrayList<WipeDisplay> returnWipeSettings() {
		return wipeDisplayList;
	}
	
	public int returnPanicProgress() {
		return 0;
	}
	
	public void updatePanicUi(String message) {
		final Intent i = new Intent();
		i.putExtra(ITCConstants.UPDATE_UI, message);
		i.setAction(Panic.class.getName());
		
		ui = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendBroadcast(i);
			}
			
		};
		u.schedule(ui,ITCConstants.Duriation.SPLASH);
	}
	
	private int shout() {
		int result = ITCConstants.Results.FAIL;
		updatePanicUi(getString(R.string.KEY_PANIC_PROGRESS_1));
		
		tt = new TimerTask() {
			
			@Override
			public void run() {
				h.post(new Runnable() {
					
					@Override
					public void run() {
						if(isPanicing) {
							sc.sendSMSShout(
									configuredFriends,
									defaultPanicMsg,
									sc.buildShoutData()
							);
							Log.d(ITCConstants.Log.ITC,"this is a shout going out...");
							panicCount++;
						}
					}
				});
			}
			
		};
		
		t.schedule(tt, 0, ITCConstants.Duriation.CONTINUED_PANIC);
		result = ITCConstants.Results.A_OK;
		return result;
	}
	
	private int wipe() {
		int result = ITCConstants.Results.FAIL;
		updatePanicUi(getString(R.string.KEY_PANIC_PROGRESS_2));
		new Thread(new Runnable() {
			@Override
			public void run() {
				wc.wipePIMData(
						shouldWipeContacts,
						shouldWipePhotos,
						shouldWipeCallLog,
						shouldWipeSMS,
						shouldWipeCalendar,
						shouldWipeFolders
				);
			}
		}).start();
		result = ITCConstants.Results.A_OK;
		return result;
	}
	
	private void stopRunnables() {
		if(isPanicing) {
			tt.cancel();
			isPanicing = false;
		}
	}
	
	@Override
	public int onStartCommand(Intent i, int flags, int startId) {
		Log.d(ITCConstants.Log.ITC,"service started hello!");		
		return START_STICKY;
	}
	
	public void startPanic() {
		isPanicing = true;
		if(shout() == ITCConstants.Results.A_OK)			
			if(wipe() == ITCConstants.Results.A_OK){
				updatePanicUi(getString(R.string.KEY_PANIC_PROGRESS_3));
			} else {
				Log.d(ITCConstants.Log.ITC,"SOMETHING WAS WRONG WITH WIPE");
			}
		else {
			Log.d(ITCConstants.Log.ITC,"SOMETHING WAS WRONG WITH SHOUT");	
		}
	}
	
	private void showNotification() {
		backToPanic.putExtra("PanicCount", panicCount);
		backToPanic.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		Notification n = new Notification(
				R.drawable.panic,
				getString(R.string.KEY_PANIC_TITLE_MAIN),
				System.currentTimeMillis()
		);
		
		PendingIntent pi = PendingIntent.getActivity(
				this,
				ITCConstants.Results.RETURN_FROM_PANIC,
				backToPanic,
				PendingIntent.FLAG_UPDATE_CURRENT
		);
		
		n.setLatestEventInfo(
				this, 
				getString(R.string.KEY_PANIC_TITLE_MAIN), 
				getString(R.string.KEY_PANIC_RETURN), 
				pi
		);
		
		nm.notify(R.string.remote_service_start_id,n);
	}
	
	@Override
	public void onDestroy() {
		stopRunnables();
		Log.d(ITCConstants.Log.ITC,"goodbye service, bye bye!");
		nm.cancel(R.string.remote_service_start_id);
	}

	@Override
	public IBinder onBind(Intent i) {
		return binder;
	}

}
