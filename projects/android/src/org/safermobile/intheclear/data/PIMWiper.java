package org.safermobile.intheclear.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.utils.ShellUtils;
import org.safermobile.utils.StreamThread;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class PIMWiper  {
	
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
	
	private static void wipeAssets(Uri uriBase) {
		Cursor cursor = null;
		try {
			cursor = cr.query(uriBase, null, null, null, null);
			if(cursor != null) {
				Log.d(ITCConstants.Log.ITC,"there are " + cursor.getCount() + " records in type: " + uriBase.toString());
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					// ContentValues cv = new ContentValues();
					for(int x=0;x<cursor.getColumnCount();x++) {
						String value = cursor.getString(x);
						//Log.d(ITCConstants.Log.ITC,cursor.getColumnName(x) + " " + cursor.getString(x));
						
						
						// go through columns to see if the data contains a file path
						// if it does, then it needs to be overwritten.
						
						/*
						if(new File(value).exists()) {
							File del = new File(value);
							try {
								InputStream fis = new FileInputStream(del);
								byte[] newBytes = new byte[(int)del.length()];
								int offset = 0;
								BufferedWriter bw = new BufferedWriter(new FileWriter(del));
								while(fis.read() != -1) {
									newBytes[offset] = 0;
									offset++;
								}
								bw.write(newBytes.toString());
								bw.close();
								del.delete();
							} catch(IOException e) {}
						}
						*/
						
						// create a zero'ed out string as the same length as the value currently in content resolver
						char[] newValues = new char[value.length()];
						int count = 0;
						for(char c : newValues) {
							newValues[count] = 0;
							count++;
						}
						//Log.d(ITCConstants.Log.ITC,"NEW: " + newValues.toString());
						// cv.put(key, newValues.toString());
					}
					// update record with new values
					
					// delete the record
					// cr.delete(Uri.parse(uriBase), null, null);
					cursor.moveToNext();
				}
				cursor.close();
			}
		} catch(NullPointerException npe) {}
	}
	
	public static void wipeCalendar() {
		Uri uriBase = Uri.parse("content://calendar/calendars");
		wipeAssets(uriBase);
	}
	
	public static void wipeContacts() {
		// String uriBase = "content://com.android.contacts/raw_contacts";
		Uri uriBase = android.provider.ContactsContract.RawContacts.CONTENT_URI;
		wipeAssets(uriBase);
	}
	
	public static void wipePhotos() {
		Uri[] uriBases = {
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI
		};
		for(Uri s : uriBases) {
			wipeAssets(s);
		}
	}
	
	public static void wipeImageThumnbnails() {
		Uri[] uriBases = {
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri s : uriBases) {
			wipeAssets(s);
		}
	}
	
	public static void wipeVideoThumbnails() {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri s : uriBases) {
			wipeAssets(s);
		}
	}
	
	public static void wipeVideos() {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI
				
		};
		for(Uri s : uriBases) {
			wipeAssets(s);
		}
	}
	
	public static void wipeSMS() {
		Uri uriBase = Uri.parse("content://sms");
		wipeAssets(uriBase);
	}
	
	public static void wipeCallLog() {
		Uri uriBase = android.provider.CallLog.Calls.CONTENT_URI;
		wipeAssets(uriBase);
	}
	
	public static void wipeFolder(File folder) {
		Log.d(ITC,"WIPING FOLDER " + folder.getName());
		
		
	}
}
