package org.safermobile.intheclear.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.utils.ShellUtils;
import org.safermobile.utils.StreamThread;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.MediaStore;
import android.util.Log;

public class PIMWiper  {
	private static ContentResolver cr;
		
	public PIMWiper(Context c) {
		PIMWiper.cr = c.getContentResolver();
	}
	
	private static void wipeAssets(Uri uriBase, String[] rewriteStrings, String[] rewriteFiles) {
		Cursor cursor = null;
		long _id = 0;
		
		try {
			cursor = cr.query(uriBase, null, null, null, null);
			if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				Log.d(ITCConstants.Log.ITC,"there are " + cursor.getCount() + " records in type: " + uriBase.toString());
				while(!cursor.isAfterLast()) {					
					_id = cursor.getLong(cursor.getColumnIndex("_id"));					
					ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

					for(String s : rewriteStrings) {
						if(cursor.getString(cursor.getColumnIndex(s)) != null && cursor.getString(cursor.getColumnIndex(s)).length() > 0) {
							
							StringBuffer sb = new StringBuffer();
							for(@SuppressWarnings("unused") char c : cursor.getString(cursor.getColumnIndex(s)).toCharArray()) {
								sb.append(0);
							}
							
							cpo.add(ContentProviderOperation
									.newUpdate(ContentUris.withAppendedId(uriBase, _id))
									.withSelection(Data._ID + "=?", new String[] {Integer.toString((int) _id)})
									.withValue(s,sb.toString())
									.build()
							);

							Log.d(ITCConstants.Log.ITC,"old " + s + " : " + cursor.getString(cursor.getColumnIndex(s)) + " | new " + s + " : " + sb.toString());
						}
					}
					
					if(rewriteFiles != null && rewriteFiles.length > 0) {
						for(String s : rewriteFiles) {
							if(cursor.getString(cursor.getColumnIndex(s)) != null && cursor.getString(cursor.getColumnIndex(s)).length() > 0) {
								
								// If the string contains a file path, zero-out/delete the referenced file as well
								File f = new File(cursor.getString(cursor.getColumnIndex(s)));
								if(f.isFile()) {
									try {
										FileInputStream fis = new FileInputStream(f);
										char[] newBytes = new char[(int) f.length()];
										int offset = 0;
										while(fis.read() != -1) {
											newBytes[offset] = 0;
											offset++;
										}
										fis.close();
										
										FileWriter fw = new FileWriter(f,false);
										fw.write(newBytes);
										fw.close();
										
										f.delete();
										
									} catch(IOException e) {}
									// Log.d(ITCConstants.Log.ITC,f.getPath() + " is a file to delete.");
								}
							}
						}
					}
					
					
					// rewrite content
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
					cpo.clear();
					
					// delete content
					cr.delete(ContentUris.withAppendedId(uriBase, _id), null, null);
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
		// String uriBase = "content://com.android.contacts/data";
		Uri uriBase = Data.CONTENT_URI;
		wipeAssets(
				uriBase,ITCConstants.ContentTargets.CONTACT.STRINGS,
				null
		);
	}
	
	public static void wipePhotos() {
		Uri[] uriBases = {
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					ITCConstants.ContentTargets.IMAGE.STRINGS,
					ITCConstants.ContentTargets.IMAGE.FILES
			);
		}
	}
	
	public static void wipeImageThumnbnails() {
		Uri[] uriBases = {
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					ITCConstants.ContentTargets.IMAGE_THUMBNAIL.STRINGS,
					ITCConstants.ContentTargets.IMAGE_THUMBNAIL.FILES
			);
		}
	}
	
	public static void wipeVideoThumbnails() {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					ITCConstants.ContentTargets.VIDEO_THUMBNAIL.STRINGS,
					ITCConstants.ContentTargets.VIDEO_THUMBNAIL.FILES
			);
		}
	}
	
	public static void wipeVideos() {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI
				
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					ITCConstants.ContentTargets.VIDEO.STRINGS,
					ITCConstants.ContentTargets.VIDEO.FILES
			);
		}
	}
	
	public static void wipeSMS() {
		Uri uriBase = Uri.parse("content://sms");
		wipeAssets(
				uriBase,
				ITCConstants.ContentTargets.SMS.STRINGS,
				null
		);
	}
	
	public static void wipeCallLog() {
		Uri uriBase = android.provider.CallLog.Calls.CONTENT_URI;
		wipeAssets(
				uriBase,
				ITCConstants.ContentTargets.CALL_LOG.STRINGS,
				null
		);
	}
	
	public static void wipeFolder(File folder) {
		Log.d(ITCConstants.Log.ITC,"WIPING FOLDER " + folder.getName());
		
		
	}
}
