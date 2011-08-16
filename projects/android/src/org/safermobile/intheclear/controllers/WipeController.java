package org.safermobile.intheclear.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.data.PIMWiper;
import org.safermobile.utils.EndActivity;
import org.safermobile.utils.FolderIterator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class WipeController extends Service {
	boolean callbackAttached = false;
	String callbackClass;
	Context _c;
	Timer t;
	TimerTask tt;
	
	public class LocalBinder extends Binder {
		public WipeController getService() {
			return WipeController.this;
		}
	}
	
	private final IBinder binder = new LocalBinder();
	
	public void addCallbackTo(String callbackClass) {
		this.callbackClass = callbackClass;
		this.callbackAttached = true;
	}
	
	public void wipePIMData(Context c, boolean contacts, boolean photos, boolean callLog, boolean sms, boolean calendar, boolean sdcard) {
		new PIMWiper(getBaseContext(),contacts, photos, callLog, sms, calendar,sdcard).start();

		
		// kill the calling activity
		Intent toKill = new Intent();
		Log.d(ITCConstants.Log.ITC,"the kill filter is called: " + c.getClass().toString());
		toKill.setAction(c.getClass().toString());
		getBaseContext().sendBroadcast(toKill);
		
			
	}
	
	@Override
	public IBinder onBind(Intent i) {
		return binder;
	}
}
