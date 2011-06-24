package org.safermobile.intheclear.controllers;

import java.io.File;
import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.data.PIMWiper;

import android.content.Context;
import android.util.Log;

public class WipeController {
	
	public WipeController(Context c) {
		new PIMWiper(c);
	}
	
	public ArrayList<String> getContacts() {
		return PIMWiper.getContacts();
	}
	
	public void wipePIMData(boolean contacts, boolean photos, boolean callLog, boolean sms, boolean calendar, ArrayList<File> folders) {
		if(contacts)
			PIMWiper.wipeContacts();
		if(photos) {
			PIMWiper.wipePhotos();
			PIMWiper.wipeVideos();
			PIMWiper.wipeImageThumnbnails();
			PIMWiper.wipeVideoThumbnails();
		}
		if(callLog)
			PIMWiper.wipeCallLog();
		if(sms)
			PIMWiper.wipeSMS();
		if(calendar)
			PIMWiper.wipeCalendar();
		if(folders != null && folders.size() > 0) {
			for(File f : folders) {
				PIMWiper.wipeFolder(f);
			}
		}
			
	}
}
