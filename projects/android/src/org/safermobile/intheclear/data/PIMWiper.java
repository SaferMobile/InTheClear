package org.safermobile.intheclear.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.Contacts.Photos;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
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
	
	private final static String ITC = "[InTheClear:PIMWiper] ************************ ";
	
	public PIMWiper(Context c) {
		PIMWiper.c = c;
		PIMWiper.cr = c.getContentResolver();
	}
	
	public void test () {
		//first remove the contacts that are there
		
		//second fill up the contact list until an error is thrown
	}
	
	public static ArrayList<String> getContacts() {
		ArrayList<String> result = new ArrayList<String>();
		
		final String[] projection = new String[] {
				RawContacts.CONTACT_ID,
				RawContacts.SOURCE_ID
			};
		
		Cursor cursor = null;
		try {
			cursor = cr.query(RawContacts.CONTENT_URI, projection, null, null, null);
			if(cursor != null) {
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					result.add("contact ID: " + cursor.getString(0) + " | source ID: " + cursor.getString(1));
					cursor.moveToNext();
				}
			}
			cursor.close();
		} catch (NullPointerException npe) {}

		
		return result;
	}
	
	private static void wipeAssets(String uriBase, String[] projection) {
		Cursor cursor = null;
		try {
			cursor = cr.query(Uri.parse(uriBase), projection, null, null, null);
			if(cursor != null) {
				cursor.moveToFirst();
				for(int x=0;x<cursor.getCount();x++) {
					long assetId = Integer.parseInt(cursor.getString(0));
					Log.d(ITC,"DELETING: " + ContentUris.withAppendedId(Uri.parse(uriBase), assetId).toString());
					//cr.delete(ContentUris.withAppendedId(Uri.parse(uriBase), assetId), null, null);
					cursor.moveToNext();
				}
				cursor.close();
			}
		} catch(NullPointerException npe) {}
	}
	
	public static void wipeCalendar() {
		Log.d(ITC,"WIPING THE CALENDAR!");
	}
	
	public static void wipeContacts() {
		String[] projection = {
				RawContacts.CONTACT_ID
		};
		String uriBase = "content://com.android.contacts/raw_contacts";
		wipeAssets(uriBase,projection);
	}
	
	public static void wipePhotos() {
		String[] projection = {
				Media._ID
		};
		String uriBase_external = "content://media/external/images/media";
		String uriBase_internal = "content://media/internal/images/media";
		wipeAssets(uriBase_external,projection);
		wipeAssets(uriBase_internal,projection);
	}
	
	public static void wipeSMS() {
		String uriBase = "content://sms";
		// TODO: this needs to be tested.
		wipeAssets(uriBase,null);
	}
	
	public static void wipeCallLog() {
		String uriBase = "content://call_log/calls";
		String[] projection = {
				Calls._ID
		};
		wipeAssets(uriBase,projection);
	}
	
	public static void wipeFolder(File folder) {
		Log.d(ITC,"WIPING FOLDER " + folder.getName());
	}
}
