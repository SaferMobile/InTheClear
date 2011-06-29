package org.safermobile.intheclear.controllers;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.apps.Panic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class PanicController extends Service {
	NotificationManager nm;
	int ticker = 0;
		
	ShoutController sc;
	Intent backToPanic;
	
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
	}
	
	@Override
	public int onStartCommand(Intent i, int flags, int startId) {
		Log.d(ITCConstants.Log.ITC,"service started hello!");
		return START_STICKY;
	}
	
	private void showNotification() {
		backToPanic.putExtra("PanicCount", "hi i am back from panic!");
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
		nm.cancel(R.string.remote_service_start_id);
	}

	@Override
	public IBinder onBind(Intent i) {
		return binder;
	}

}
