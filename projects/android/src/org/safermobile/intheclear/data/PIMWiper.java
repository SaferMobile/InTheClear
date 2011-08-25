package org.safermobile.intheclear.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.safermobile.intheclear.ITCConstants;
import org.safermobile.intheclear.R;
import org.safermobile.utils.FolderIterator;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.MediaStore;
import android.util.Log;

public class PIMWiper extends Thread {
	private static ContentResolver cr;
	private static AccountManager am;
	private static Context c;
	
	private boolean contacts,photos,callLog,sms,calendar,sdcard;
	
	public static Bitmap gracie;
	
	public PIMWiper(Context c,boolean contacts,boolean photos,boolean callLog,boolean sms,boolean calendar,boolean sdcard) {
		this.contacts = contacts;
		this.photos = photos;
		this.callLog = callLog;
		this.sms = sms;
		this.calendar = calendar;
		this.sdcard = sdcard;
		
		try {
			gracie = BitmapFactory.decodeResource(c.getResources(), R.drawable.gracie_the_mixed_breed);
		} catch(Exception e) {
			Log.d(ITCConstants.Log.ITC,"gracie not found: " + e);
		}
		
		PIMWiper.c = c;
		PIMWiper.cr = c.getContentResolver();
		PIMWiper.am = AccountManager.get(c);
		
		
	}
	
	@Override
	public void run() {
		try {
			// FIRST, you must turn off sync or else you get the "Deleted Contacts" error...
			preventSync();

			if(contacts) {
				PIMWiper.wipeContacts();
				PIMWiper.wipePhoneNumbers();
				PIMWiper.wipeEmail();
			}
			
			if(photos) {
				PIMWiper.wipePhotos();
				PIMWiper.wipeVideos();
				PIMWiper.wipeImageThumnbnails();
				PIMWiper.wipeVideoThumbnails();
			}
			
			if(callLog) {
				PIMWiper.wipeCallLog();
			}
			
			if(sms) {
				PIMWiper.wipeSMS();
			}
			
			if(calendar) {
				PIMWiper.wipeCalendar();
			}
			
			if(sdcard) {
				new FolderIterator();
				ArrayList<File> folders = FolderIterator.getFoldersOnSDCard();
				Log.d(ITCConstants.Log.ITC,"Preparing to wipe " + folders.size() + " folders from the SD Card.");
				for(File f : folders) {
					PIMWiper.wipeFolder(f);
				}
				
			}
		} catch(IOException e){
			Log.d(ITCConstants.Log.ITC,"Wipe has failed: " + e);
		}
	}
	
	private static void getAvailableColumns(Cursor cursor) {
		String[] availableColumns = cursor.getColumnNames();
		StringBuffer sbb = new StringBuffer();
		for(String s : availableColumns) {
			sbb.append(s + ", ");
		}
		Log.d(ITCConstants.Log.ITC,"Available Columns: " + sbb.toString());
	}
	
	private static void preventSync() {
		Log.d(ITCConstants.Log.ITC,"accts: " + am.getAccounts());
		Account[] accounts = am.getAccounts();
		for(Account a : accounts) {
			ContentResolver.setIsSyncable(a, ContactsContract.AUTHORITY, 0);
		}
	}
		
	private static ArrayList<Integer> wipeAssets(Uri uriBase, String authority, String[] rewriteStrings, String[] rewriteFiles) throws FileNotFoundException, IOException {
		/*
		 * primarily for the sake of the calendar wipe,
		 * return the list of asset IDs, should you need to drill down further
		 * into the content resolver
		 */
		ArrayList<Integer> assetIds = new ArrayList<Integer>();
		
		Cursor cursor = null;
		long _id = 0;
		
		try {
			cursor = cr.query(uriBase, null, null, null, null);
			if(cursor != null && cursor.getCount() > 0) {
				getAvailableColumns(cursor);
				
				cursor.moveToFirst();
				Log.d(ITCConstants.Log.ITC,"there are " + cursor.getCount() + " records in type: " + uriBase.toString());
				
				while(!cursor.isAfterLast()) {
					_id = cursor.getLong(cursor.getColumnIndex("_id"));	
					assetIds.add((int) _id);
					
					ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

					if(rewriteStrings != null) {
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
					}

					if(rewriteFiles != null && rewriteFiles.length > 0) {
						for(String s : rewriteFiles) {
							if(cursor.getString(cursor.getColumnIndex(s)) != null && cursor.getString(cursor.getColumnIndex(s)).length() > 0) {
								
								// If the string contains a file path, zero-out/delete the referenced file as well
								File f = new File(cursor.getString(cursor.getColumnIndex(s)));
								if(f.isFile())
									rewriteAndDelete(f);
							}
						}
					}
										
					// rewrite content
					if(!cpo.isEmpty()) {
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
					}
					
					// delete content
					//Log.d(ITCConstants.Log.ITC,"DELETE " + ContentUris.withAppendedId(uriBase, _id).toString() + " ?");
					try {
						cr.delete(ContentUris.withAppendedId(uriBase, _id), null, null);
					} catch(UnsupportedOperationException e) {
						Log.d(ITCConstants.Log.ITC,"CAN\'T DELETE BECAUSE: " + e);
						
						// there's an exception, so try building the delete query from scratch and forcing it through?
						try {
							cr.delete(uriBase, Data._ID + "=?", new String[] {Integer.toString((int) _id)});
						} catch(UnsupportedOperationException e2){}
					}
					cursor.moveToNext();
				}
			} else {
				Log.d(ITCConstants.Log.ITC,"there are no records to manipulate here: " + uriBase.toString());
			}
			cursor.close();
		} catch(NullPointerException npe) {}
		return assetIds;
	}
	
	private static void rewriteAndDelete(final File f) throws FileNotFoundException, IOException {
		// first, determine if file is an image
		Log.d(ITCConstants.Log.ITC,"filename " + f.getName());
		boolean isImageFile = false;
		try {
			String fileType = f.getName().substring(f.getName().length() - 4);
			if(
					fileType.compareTo(".jpg") == 0 ||
					fileType.compareTo(".png") == 0 ||
					fileType.compareTo(".gif") == 0) {
				isImageFile = true;
			}
		} catch(StringIndexOutOfBoundsException e) {}

		if(!isImageFile) {			
			// if not an image...
			FileInputStream fis = new FileInputStream(f);
			FileWriter fw = new FileWriter(f,false);
			
			try {
				char[] newBytes = new char[(int) f.length()];
				int offset = 0;
				while(fis.read() != -1) {
					newBytes[offset] = 0;
					offset++;
				}
				fis.close();
				
				fw.write(newBytes);
				fw.close();
				
				// delete the file
				f.delete();
			} catch(OutOfMemoryError e) {
				Log.d(ITCConstants.Log.ITC,"VM limit reached in the zero-out process.  Garbage collection prompted.");
				
				fis.close();
				fw.close();
				f.delete();
				System.gc();
			}
		} else {
			// rewrite as dummy bitmap
			
			FileOutputStream fos = new FileOutputStream(f);
			gracie.compress(Bitmap.CompressFormat.JPEG,30,fos);
			fos.flush();
			fos.close();
			
			// ...and force media scanner to rescan the file
			MediaScannerConnection.scanFile(
					PIMWiper.c,
					new String[] {f.getPath()},
					new String[] {"JPEG"},
					new MediaScannerConnection.OnScanCompletedListener() {
						
						@Override
						public void onScanCompleted(String path, Uri uri) {
							Log.d(ITCConstants.Log.ITC,"we have scanned in " + path + " with the media scanner!");
							// delete the file
							f.delete();
						}
					});
			}
	}
	
	public static void wipeCalendar() throws FileNotFoundException, IOException {		
		/*
		 *  TODO: this needs help.  
		 *  getting error "IllegalArgumentException:
		 *  WHERE based updates not supported"
		 *  
		 *  however, we have finally pinpointed the correct
		 *  content authority and URI base
		 */
		Uri uriBase = Uri.parse("content://com.android.calendar/calendars");
		//wipeAssets(uriBase,"com.android.calendar",ITCConstants.ContentTargets.CALENDAR.STRINGS,null);
		ArrayList<Integer> calIds = wipeAssets(uriBase,"com.android.calendar",null,null);
		for(long cal : calIds) {
			Log.d(ITCConstants.Log.ITC,"cal: " + cal);
		}
	}
	
	public static void wipeContacts() throws FileNotFoundException, IOException {
		Uri uriBase = ContactsContract.Contacts.CONTENT_URI;
		wipeAssets(
				uriBase,ContactsContract.AUTHORITY,ITCConstants.ContentTargets.CONTACT.STRINGS,
				null
		);
	}
	
	public static void wipePhoneNumbers() throws FileNotFoundException, IOException {
		Uri uriBase = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		wipeAssets(
				uriBase,ContactsContract.AUTHORITY,ITCConstants.ContentTargets.PHONE_NUMBER.STRINGS,
				null
		);
	}
	
	public static void wipeEmail() throws FileNotFoundException, IOException {
		Uri uriBase = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		wipeAssets(
				uriBase,ContactsContract.AUTHORITY,ITCConstants.ContentTargets.EMAIL.STRINGS,
				null
		);
	}
	
	public static void wipePhotos() throws FileNotFoundException, IOException {
		Uri[] uriBases = {
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Media.INTERNAL_CONTENT_URI
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					MediaStore.AUTHORITY,
					ITCConstants.ContentTargets.IMAGE.STRINGS,
					ITCConstants.ContentTargets.IMAGE.FILES
			);
		}
	}
	
	public static void wipeImageThumnbnails() throws FileNotFoundException, IOException {
		Uri[] uriBases = {
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
				MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					MediaStore.AUTHORITY,
					ITCConstants.ContentTargets.IMAGE_THUMBNAIL.STRINGS,
					ITCConstants.ContentTargets.IMAGE_THUMBNAIL.FILES
			);
		}
	}
	
	public static void wipeVideoThumbnails() throws FileNotFoundException, IOException {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					MediaStore.AUTHORITY,
					ITCConstants.ContentTargets.VIDEO_THUMBNAIL.STRINGS,
					ITCConstants.ContentTargets.VIDEO_THUMBNAIL.FILES
			);
		}
	}
	
	public static void wipeVideos() throws FileNotFoundException, IOException {
		Uri[] uriBases = {
				android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI
				
		};
		for(Uri uriBase : uriBases) {
			wipeAssets(
					uriBase,
					MediaStore.AUTHORITY,
					ITCConstants.ContentTargets.VIDEO.STRINGS,
					ITCConstants.ContentTargets.VIDEO.FILES
			);
		}
	}
	
	public static void wipeSMS() throws FileNotFoundException, IOException {
		Uri uriBase = Uri.parse("content://sms");
		wipeAssets(
				uriBase,
				"sms",
				ITCConstants.ContentTargets.SMS.STRINGS,
				null
		);
	}
	
	public static void wipeCallLog() throws FileNotFoundException, IOException {
		Uri uriBase = android.provider.CallLog.Calls.CONTENT_URI;
		wipeAssets(
				uriBase,
				CallLog.AUTHORITY,
				ITCConstants.ContentTargets.CALL_LOG.STRINGS,
				null
		);
	}
	
	private static ArrayList<File> getInnerFiles(File f) {
		ArrayList<File> innerFiles = new ArrayList<File>();
		for(File file : f.listFiles()) {
			if(file.isFile() && file.canWrite())
				innerFiles.add(file);
			else if(file.isDirectory() && file.canRead())
				innerFiles.addAll(getInnerFiles(file));
		}
		return innerFiles;
	}
	
	public static void wipeFolder(File folder) throws FileNotFoundException, IOException {
		ArrayList<File> del = new ArrayList<File>();
		StringBuffer sb = new StringBuffer();
		for(File file : folder.listFiles()) {
			if(file.isFile() && file.canWrite())
				del.add(file);
			else if(file.isDirectory() && file.canRead())
				del.addAll(getInnerFiles(file));
		}
		
		int counter = 1;
		for(File f : del) {
			sb.append(counter++ + ". " + f.getPath() + "\n");
			rewriteAndDelete(f);
		}
		Log.d(ITCConstants.Log.ITC,"FOLDER " + folder.getName() + " contained:\n" + sb.toString());
		
		// remove this folder (if it's not the SDCard, naturally)
		// if, for some reason, the folder was not fully emptied,
		// this will fail.  so do it again.
		/*
		if(folder.getPath().compareTo(FolderIterator.pathToSDCard.toString()) != 0)
			if(!folder.delete())
				wipeFolder(folder);
		*/
	}
}