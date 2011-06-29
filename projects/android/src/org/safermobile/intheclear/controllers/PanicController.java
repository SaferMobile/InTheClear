package org.safermobile.intheclear.controllers;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.apps.Panic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class PanicController extends Service {
	NotificationManager nm;
	int ticker = 0;
	
	@Override
	public void onCreate() {
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		showNotification();
		handler.sendEmptyMessage(ITCConstants.SERVICE_START);
	}
	
	@Override
	public void onStart(Intent i, int startId) {
		
	}
	
	private void showNotification() {
		Notification n = new Notification(
				R.drawable.panic,
				getString(R.string.KEY_PANIC_TITLE_MAIN),
				System.currentTimeMillis()
		);
		
		PendingIntent pi = PendingIntent.getActivity(
				this,
				ITCConstants.Results.RETURN_FROM_PANIC,
				new Intent(this,Panic.class),
				0
		);
		
		n.setLatestEventInfo(
				this, 
				getString(R.string.KEY_PANIC_TITLE_MAIN), 
				getString(R.string.KEY_PANIC_RETURN), 
				pi
		);
		
		nm.notify(R.string.remote_service_start_id,n);
	}
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case ITCConstants.SERVICE_START:
				int val = ++ticker;
				
				
				
				break;
			default:
				super.handleMessage(msg);
					
			}
		}
	};
	
	@Override
	public void onDestroy() {
		
	}

	@Override
	public IBinder onBind(Intent i) {
		return null;
	}

}
