package org.safermobile.intheclear.controllers;

import java.io.File;
import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.intheclear.data.PIMWiper;

import android.content.Context;
import android.content.Intent;

public class WipeController {
	boolean callbackAttached = false;
	String callbackClass;
	Context _c;
	
	public WipeController(Context c) {
		_c = c;
		new PIMWiper(_c);
	}
	
	public void addCallbackTo(String callbackClass) {
		this.callbackClass = callbackClass;
		this.callbackAttached = true;
	}
	
	private void updateCallingActivity(String message) {
		Intent i = new Intent();
		i.putExtra(ITCConstants.UPDATE_UI, message);
		i.setAction(callbackClass);
		_c.sendBroadcast(i);
	}
	
	public void wipePIMData(boolean contacts, boolean photos, boolean callLog, boolean sms, boolean calendar, ArrayList<File> folders) {
		if(contacts) {
			PIMWiper.wipeContacts();
			if(callbackAttached)
				updateCallingActivity(_c.getString(R.string.KEY_WIPE_CONFIRM_CONTACTS));
		}
		
		if(photos) {
			PIMWiper.wipePhotos();
			PIMWiper.wipeVideos();
			PIMWiper.wipeImageThumnbnails();
			PIMWiper.wipeVideoThumbnails();
			if(callbackAttached)
				updateCallingActivity(_c.getString(R.string.KEY_WIPE_CONFIRM_PHOTOS));
		}
		
		if(callLog) {
			PIMWiper.wipeCallLog();
			if(callbackAttached)
				updateCallingActivity(_c.getString(R.string.KEY_WIPE_CONFIRM_CALLLOG));
		}
		
		if(sms) {
			PIMWiper.wipeSMS();
			if(callbackAttached)
				updateCallingActivity(_c.getString(R.string.KEY_WIPE_CONFIRM_SMS));
		}
		
		if(calendar) {
			PIMWiper.wipeCalendar();
			if(callbackAttached)
				updateCallingActivity(_c.getString(R.string.KEY_WIPE_CONFIRM_CALENDAR));
		}
		
		if(folders != null && !folders.isEmpty()) {
			for(File f : folders) {
				PIMWiper.wipeFolder(f);
			}
			if(callbackAttached)
				updateCallingActivity(_c.getString(R.string.KEY_WIPE_CONFIRM_FOLDERS));
		}
			
	}
}
