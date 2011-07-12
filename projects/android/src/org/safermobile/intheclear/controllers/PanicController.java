package org.safermobile.intheclear.controllers;

import java.util.Timer;
import java.util.TimerTask;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.apps.Panic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class PanicController extends Service {
	NotificationManager nm;
	
	TimerTask tt;
	Timer t = new Timer();
	final Handler h = new Handler();
	int ticker = 0;
		
	ShoutController sc;
	Intent backToPanic;
	Bundle b;
	
	public class LocalBinder extends Binder {
		PanicController getService() {
			return PanicController.this;
		}
	}
	
	private final IBinder binder = new LocalBinder();
	
	@Override
	public void onCreate() {
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);		
		sc = new ShoutController(getBaseContext());
		backToPanic = new Intent(this,Panic.class);
		showNotification();
		tt = new TimerTask() {

			@Override
			public void run() {
				h.post(new Runnable() {

					@Override
					public void run() {
						shout();
						
					}
					
				});
			}
			
		};
		t.schedule(tt, 0, ITCConstants.Duriation.CONTINUED_PANIC);
	}
	
	public void shout() {
		Log.d(ITCConstants.Log.ITC,"SHOUTING!!!");
		sc.sendSMSShout(
				b.getString("configuredFriends"),
				b.getString("defaultPanicMsg"),
				sc.buildShoutData(b.getString("userDisplayName"))
		);
		ticker++;
	}
	
	@Override
	public int onStartCommand(Intent i, int flags, int startId) {
		Log.d(ITCConstants.Log.ITC,"service started hello!");
		b = i.getExtras();
		
		return START_STICKY;
	}
	
	private void showNotification() {
		backToPanic.putExtra("PanicCount", ticker);
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
		Log.d(ITCConstants.Log.ITC,"goodbye service, bye bye!");
		t.cancel();
		nm.cancel(R.string.remote_service_start_id);
	}

	@Override
	public IBinder onBind(Intent i) {
		return binder;
	}

}
