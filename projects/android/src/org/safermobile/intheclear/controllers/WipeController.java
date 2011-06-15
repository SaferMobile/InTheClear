package org.safermobile.intheclear.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import org.safermobile.intheclear.data.PIMWiper;

public class WipeController {
	
	public WipeController() {
		
	}
	
	public Vector<String> getContacts() {
		return PIMWiper.getContacts();
	}
	
	public void wipePIMData(boolean contacts, boolean photos, boolean callLog, boolean sms, ArrayList<File> folders) {
		if(contacts)
			PIMWiper.wipeContacts();
		if(photos)
			PIMWiper.wipePhotos();
		if(callLog)
			PIMWiper.wipeCallLog();
		if(sms)
			PIMWiper.wipeSMS();
		if(folders.size() > 0) {
			for(File f : folders) {
				PIMWiper.wipeFolder(f);
			}
		}
			
	}
}
