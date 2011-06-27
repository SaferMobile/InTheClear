package org.safermobile.intheclear.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Random;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.utils.ShellUtils;
import org.safermobile.utils.StreamThread;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class PIMWiper  {
	private static ContentResolver cr;
		
	public PIMWiper(Context c) {
		PIMWiper.cr = c.getContentResolver();
	}
	
	private static void wipeAssets(Uri uriBase, String[] rewriteStrings, String[] rewriteBlobs) {
		Cursor cursor = null;
		long _id = 0;
		
		try {
			cursor = cr.query(uriBase, null, null, null, null);
			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				Log.d(ITCConstants.Log.ITC,"there are " + cursor.getCount() + " records in type: " + uriBase.toString());
				while(!cursor.isAfterLast()) {					
					_id = cursor.getLong(cursor.getColumnIndex("_id"));
					String[] recordId = {Integer.toString((int) _id)};
					
					ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

					for(String s : rewriteStrings) {
						if(cursor.getString(cursor.getColumnIndex(s)) != null && cursor.getString(cursor.getColumnIndex(s)).length() > 0) {
							StringBuffer sb = new StringBuffer();
							for(char c : cursor.getString(cursor.getColumnIndex(s)).toCharArray()) {
								sb.append(0);
							}
							/*
							cpo.add(ContentProviderOperation
									.newUpdate(ContentUris.withAppendedId(uriBase, _id))
									.withSelection(Data._ID + "=?", recordId)
									.withValue(s,sb.toString())
									.build()
							);
							*/
							Log.d(ITCConstants.Log.ITC,"old " + s + " : " + cursor.getString(cursor.getColumnIndex(s)) + " | new " + s + " : " + sb.toString());
						}
					}
					
					for(String s : rewriteBlobs) {
						if(cursor.getBlob(cursor.getColumnIndex(s)) != null & cursor.getBlob(cursor.getColumnIndex(s)).length > 0) {
							byte[] newBlob = new byte[cursor.getBlob(cursor.getColumnIndex(s)).length];
							int count = 1;
							for(byte b : newBlob) {
								b = 0;
								newBlob[count] = b;
								count++;
							}
							
							/*
							cpo.add(ContentProviderOperation
							 		.newUpdate(ContentUris.withAppendedId(uriBase, _id))
							 		.withSelection(Data._ID + "=?", recordID)
							 		.withValue(s,newBlob)
							 		.build()
							);
							 */
						}
					}
					
					// rewrite content
					
					/*
					String authority = ContactsContract.AUTHORITY;
					try {
						cr.applyBatch(authority, cpo);
					} catch (RemoteException e) {
						Log.d(ITCConstants.Log.ITC,"FAIL : " + e);
					} catch (OperationApplicationException e) {
						Log.d(ITCConstants.Log.ITC,"FAIL : " + e);
					} catch (UnsupportedOperationException e) {
						Log.d(ITCConstants.Log.ITC,"FAIL : " + e);
					}
					*/
					cpo.clear();
					
					// delete content
					//cr.delete(ContentUris.withAppendedId(uriBase, recordId), null, null);
					cursor.moveToNext();
				}
			}
			cursor.close();
		} catch(NullPointerException npe) {}
	}
	
	private static void getAvailableColumns(Cursor cursor) {
		String[] availableColumns = cursor.getColumnNames();
		StringBuffer sbb = new StringBuffer();
		for(String s : availableColumns) {
			sbb.append(s + ", ");
		}
		Log.d(ITCConstants.Log.ITC,"Available Columns: " + sbb.toString());
	}
	
	public static void wipeCalendar() {
		Uri uriBase = Uri.parse("content://calendar/calendars");
		wipeAssets(uriBase,null,null);
	}
	
	public static void wipeContacts() {
		// String uriBase = "content://com.android.contacts/raw_contacts";
		Uri uriBase = Data.CONTENT_URI;
		wipeAssets(uriBase,ITCConstants.ContentTargets.CONTACT.STRINGS,ITCConstants.ContentTargets.CONTACT.BLOBS);
	}
	
	public static void wipePhotos() {
		Uri[] uriBases = {
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI
		};
		for(Uri s : uriBases) {
			wipeAssets(s,ITCConstants.ContentTargets.IMAGE,null);
		}
	}
	
	public static void wipeImageThumnbnails() {
		Uri[] uriBases = {
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri s : uriBases) {
			wipeAssets(s,ITCConstants.ContentTargets.IMAGE_THUMBNAIL,null);
		}
	}
	
	public static void wipeVideoThumbnails() {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri s : uriBases) {
			wipeAssets(s,ITCConstants.ContentTargets.VIDEO_THUMBNAIL,null);
		}
	}
	
	public static void wipeVideos() {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI
				
		};
		for(Uri s : uriBases) {
			wipeAssets(s,ITCConstants.ContentTargets.VIDEO,null);
		}
	}
	
	public static void wipeSMS() {
		Uri uriBase = Uri.parse("content://sms");
		wipeAssets(uriBase,null,null);
	}
	
	public static void wipeCallLog() {
		Uri uriBase = android.provider.CallLog.Calls.CONTENT_URI;
		wipeAssets(uriBase,null,null);
	}
	
	public static void wipeFolder(File folder) {
		Log.d(ITCConstants.Log.ITC,"WIPING FOLDER " + folder.getName());
		
		
	}
}
