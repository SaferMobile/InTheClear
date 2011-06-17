package org.safermobile.intheclear.data;

import java.io.File;
import java.util.Random;
import java.util.Vector;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class PIMWiper  {

	private final static String ZERO_STRING = "000000000000000"; //limit to 15 for name and phone number fields
	private final static int DEFAULT_ZERO_AMOUNT = 1000000000;
	
	private static Random RAND = new Random();
	
	private static Context c;
	private static ContentResolver cr;
	
	private final static String[] names = new String[] {
			"Tom","Jacob","Jake",
			"Ethan","Jonathan","Tyler","Samuel","Nicholas","Angel",
			"Jayden","Nathan","Elijah","Christian","Gabriel","Benjamin",
			"Emma","Aiden","Ryan","James","Abigail","Logan","John",
			"Daniel","Alexander","Isabella","Anthony","William","Christopher","Matthew","Emily","Madison",
			"Rob","Ava","Olivia","Andrew","Joseph","David","Sophia","Noah",
			"Justin",
			"Smith","Johnson","Williams","Jones","Brown","Davis","Miller","Wilson","Moore",
			"Taylor","Anderson","Thomas","Jackson","White","Harris","Martin","Thompson","Garcia",
			"Martinez","Robinson","Clark","Lewis","Lee","Walker","Hall","Allen","Young",
			"King","Wright","Hill","Scott","Green","Adams","Baker","Carter","Turner",
		};
	
	private final static String ITC = "[InTheClear:Wipe] ************************ ";
	
	public PIMWiper(Context c) {
		PIMWiper.c = c;
		PIMWiper.cr = c.getContentResolver();
	}
	
	public void test () {
		//first remove the contacts that are there
		
		//second fill up the contact list until an error is thrown
	}
	
	public static Vector<String> getContacts() {
		Vector<String> result = new Vector<String>();
		
		final String[] projection = new String[] {
				RawContacts.CONTACT_ID,
				RawContacts.SOURCE_ID
			};
		
		Cursor cursor = cr.query(RawContacts.CONTENT_URI, projection, null, null, null);
		if(cursor != null) {
			cursor.moveToFirst();
			while(!cursor.isAfterLast()) {
				result.addElement("contact ID: " + cursor.getString(0) + " | source ID: " + cursor.getString(1));
				cursor.moveToNext();
			}
		}
		
		return result;
	}
	
	public static void wipeContacts() {
		Log.d(ITC,"WIPING CONTACTS!");
	}
	
	public static void wipePhotos() {
		Log.d(ITC,"WIPING PHOTOS!");
	}
	
	public static void wipeFolder(File folder) {
		Log.d(ITC,"WIPING FOLDER " + folder.getName());
	}
	
	public static void wipeSMS() {
		Log.d(ITC,"WIPING SMSes!");

	}
	
	public static void wipeCallLog() {
		Log.d(ITC,"WIPING CALL LOG!");

	}
}
