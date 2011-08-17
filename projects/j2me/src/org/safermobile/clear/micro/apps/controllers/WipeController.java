package org.safermobile.clear.micro.apps.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.pim.PIMException;

import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.data.PIMWiper;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

public class WipeController {

	/*
	private final static String UP_DIRECTORY = "..";
	  private final static String MEGA_ROOT = "/";
	  private final static String SEP_STR = "/";
	  private final static char   SEP = '/';
	*/
	
	public final static String TYPE_PHOTOS = "photos";
	public final static String TYPE_VIDEOS = "videos";
	public final static String TYPE_RECORDINGS = "recordings";
	public final static String TYPE_MEMORYCARD = "memorycard";
	
	private static byte[] zeroFile; //this is what we use to zero files out
	
	private final static String[] POSSIBLE_WIPE_PATHS = 
		{
			"file:///store/home/user/",			
			"file:///SDCard/Blackberry/",
			"file:///SDCard/Blackberry/system/",
			"file:///SDCard/Blackberry/system/appdata/",
			"file:///SDCard/Blackberry/system/media/",
			"file:///SDCard/",
			"file:///C:/",
			"file:///E:/"
		};
	
	public boolean checkPermissions (boolean checkContacts, boolean checkEvents, boolean checkPhotos, boolean checkSDCard)
	{
		boolean result = false;
		
		if (checkContacts)
			result = PIMWiper.checkContacts();
		
		if (checkEvents)
			result = PIMWiper.checkEventsAndToDo();
		
		try
		{
			if (result = isFileAPIAvailable())
			{
				if (checkPhotos)
				{
					String photosPath = System.getProperty("fileconn.dir.photos");
					if (photosPath != null)
						Connector.open(photosPath);
		
					String videosPath = System.getProperty("fileconn.dir.videos");
					if (videosPath != null)
						Connector.open(videosPath);
				}
				
				if (checkSDCard)
				{
					String memcardPath = System.getProperty("fileconn.dir.memorycard");
					if (memcardPath != null)
						Connector.open(memcardPath);
				}
			}
			
			result = true;
		}
		catch (Exception e)
		{
			//bad things afoot
			
			result = false;
		}
		
		return result;

	}
	
	public boolean checkAllPermissions ()
	{
		boolean result = false;
		
		result = PIMWiper.checkContacts();
    	result = PIMWiper.checkEventsAndToDo();
		
		try
		{
			if (result = isFileAPIAvailable())
			{
				Enumeration drives = FileSystemRegistry.listRoots();
				String photosPath = System.getProperty("fileconn.dir.photos");
				if (photosPath != null)
					Connector.open(photosPath);
	
				String videosPath = System.getProperty("fileconn.dir.videos");
				if (videosPath != null)
					Connector.open(videosPath);
				
				String memcardPath = System.getProperty("fileconn.dir.memorycard");
				if (memcardPath != null)
					Connector.open(memcardPath);
				
			}
		}
		catch (Exception e)
		{
			//bad things afoot
			
			result = false;
		}
		
		return result;

	}
	
	public void wipePIMData (boolean contacts, boolean events, boolean toDos) throws PIMException
	{
		if (contacts)
			PIMWiper.wipeContacts();
		
		if (events)
			PIMWiper.wipeEvents();
		
		if (toDos)
			PIMWiper.wipeToDos();
	}
	
	
	public void fillContactsZero (int fillItemCount) throws Exception
	{
		PIMWiper.zeroContacts(fillItemCount);

	}
	
	
	public boolean wipeAllRootPaths (WipeListener wl) throws IOException
	{
		Enumeration drives = FileSystemRegistry.listRoots();
		boolean success = false;
		
		while (drives.hasMoreElements())
		{
			String rootPath =  "file:///" + drives.nextElement().toString();
			
		    FileConnection fc = (FileConnection) Connector.open(rootPath, Connector.READ);
		    
		    if (fc.exists())
		    {
				try
				{			
					wipeFilePath(fc.getURL(), wl);
					success = true;
				}
				catch (Exception e)
				{
					wl.wipingFileError(fc.getName(),e.getMessage());
					
				}
		    }
		}
		
		int i = 0;
		
		try
		{
			for (i = 0; i < POSSIBLE_WIPE_PATHS.length; i++)
			{
			    FileConnection fc = (FileConnection) Connector.open(POSSIBLE_WIPE_PATHS[i], Connector.READ);
			    
			    try
				{
			    	if (fc.exists())
			    	{
			    		success = wipeFilePath(fc.getURL(), wl);
			    	}
				}
				catch (Exception e)
				{
					wl.wipingFileError(fc.getName(),e.getMessage());
					success = false;
				}
			}
		}
		catch (Exception e)
		{
			//will catch these assuming it means that the file path does not exist
			Logger.error("wipeController", "error wiping files: " + POSSIBLE_WIPE_PATHS[i], e);

		}
		
		
		return success;
	}
	
	public boolean wipeFilePath (String path, WipeListener wl) throws Exception
	{

		   FileConnection fc = (FileConnection) Connector.open(path, Connector.READ);
		   
	      if (fc.exists()) 
	      {
	    	
	    	  if (fc.isDirectory())
	    	  {

	   		   	 wl.wipeStatus("Opening folder:\n" + fc.getName());
	   			 doSecPause(100);
	   		   
	    		  //first iterate through the files in the directory
	    		  String dirPath = fc.getURL();
	    		  Enumeration enumFiles = fc.list("*", true);
	    		  
	    		  while (enumFiles.hasMoreElements())
	    		  {
	    			  String fcNext = enumFiles.nextElement().toString();
	    			  
	    			  try
	    			  {
	    				  //recursively call this method on each file, there may be nested directories
	    				  wipeFilePath(dirPath + fcNext, wl);
	    			  }
	    			  catch (Exception e)
	    			  {
	    				  //the user doesn't need to this
	    				  //wl.wipingFileError(fc.getName(), e.getMessage());
	    				 // doSecPause(300);
	    			  }
	    		  }
	    		  
	    		  if (!fc.list("*",true).hasMoreElements())
	    		  {
		    		  try
	    			  {
		    			  wl.wipeStatus("Deleting folder:\n" + fc.getName());
		    	   		  doSecPause(100);
		    	   		   
			    		  //if the directory itself is writeable, then delete it
			    		  
		    			  fc.close();
		    			  fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);	    			  
		    			  
		    			  fc.delete();
		    			  fc.close();
	
		    			  wl.wipingFileSuccess(fc.getName());
			    		 
	    			  }
	    			  catch (Exception e)
	    			  {
	    				  //the user doesn't need to this
	    				  //wl.wipingFileError(fc.getName(), e.getMessage());
	    				 // doSecPause(300);
	    				  //may not be able to delete all directories if they are permanent internal locations
	    			  }
	    		  }
	    	  }
	    	  else if (fc.canWrite()) //it is a file!
	    	  {
    			  try
    			  {
        			  fc.close();
        			
	    			  fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
	    			  String fileName = fc.getName();
	    		
	    			  wl.wipeStatus("Deleting file:\n" + fileName);
	    			  doSecPause(100);

	    			  fc.delete();
	    			  fc.close();
	
	    			  wl.wipingFileSuccess(fileName);
	    			  
	    			  return true;
    			  }
    			  catch (Exception e)
    			  {
    				  //the user doesn't need to this
    				  //wl.wipingFileError(fc.getName(), e.getMessage());
    				 // doSecPause(300);
    				  //may not be able to delete all directories if they are permanent internal locations
    				  return false;
    			  }
	    		  
	    	  }
	    	  
	      }
		
		return true;
	
	}
	
	public static void zeroFile (FileConnection fc, long fileSize, WipeListener wl) throws Exception
	{

		  OutputStream outputStream= fc.openOutputStream();
		  
		  byte[] zeroValue = getZeroFile();
		  long percent = 0;
		  
		  for (long i = 0; i < fileSize; i+=zeroValue.length)
		  {
			  outputStream.write(zeroValue);
			 
			  if (i > 0)
			  {				 
				  percent = 100 / (fileSize / i);			  			 
				  wl.wipeStatus("Creating Zero File:\n" + percent + "% complete\n(please be patient)");
			  }
		  }
          
          outputStream.close();
		  
	}
	
	private static byte[] getZeroFile ()
	{
		if (zeroFile == null)
		{
			zeroFile = new byte[1000];
		  
			for (int i = 0; i < zeroFile.length; i++)
				zeroFile[i] = 0;
		}
		  
		return zeroFile;
	}
	
	//
	public static boolean zeroFillStorage (Enumeration paths, WipeListener wl)
	{
		boolean success = false;
		FileConnection fc;
		
		while (paths.hasMoreElements())
		{
			try
			{
				String root = "file:///" + paths.nextElement().toString();
				
				fc = (FileConnection) Connector.open(root, Connector.READ);

				if (!fc.canWrite())
				{
					zeroFillStorage (fc.list(),wl);
				}
				else
				{
					//make a timestamped name file that looks like an image
					String zeroFileName = new Date().getTime() + ".jpg";
					
					String filePath = fc.getURL() + zeroFileName;
					
					fc = (FileConnection) Connector.open(filePath, Connector.READ_WRITE);
					
					if (!fc.canWrite())
					{
						zeroFillStorage (fc.list("*",true),wl);
					}
					else
					{
						fc.create();
						long zeroFileSize = fc.availableSize(); //reduce by 1/4
						zeroFile (fc, zeroFileSize, wl);
						fc.close();
					}
				}
				
			}
			catch (Exception e)
			{
				//we should catch exceptions for each path, so we can try multiple times
				Logger.error(TYPE_MEMORYCARD, "error zero filling storage: " + e.getMessage(), e);
			//	e.printStackTrace();
			}
		}
		
		String[] mediaPaths = {TYPE_PHOTOS, TYPE_VIDEOS, TYPE_MEMORYCARD, "memorycard." + TYPE_PHOTOS, "memorycard." + TYPE_VIDEOS};
		
		for (int i = 0; i < mediaPaths.length; i++)
		{
			try
			{
				String pathKey = "fileconn.dir." + mediaPaths[i];
				String path = System.getProperty(pathKey);

				if (path != null)
				{
					fc = (FileConnection) Connector.open(path, Connector.READ);
					String zeroFileName = new Date().getTime() + ".jpg";				
					String zeroPath = fc.getURL() + zeroFileName;
					fc.close();
					
					fc = (FileConnection) Connector.open(zeroPath, Connector.READ_WRITE);
					fc.create();
					long zeroFileSize = fc.availableSize(); //reduce by 1/4
					zeroFile (fc, zeroFileSize, wl);					
					fc.close();
				}
			}
			catch (Exception e)
			{
				Logger.error(mediaPaths[i], "error zero filling storage: " + e.getMessage(), e);
				e.printStackTrace();
			}
			
			
		}
		
		return success;
	}
	
	public boolean wipeMedia (String type, boolean external, WipeListener wl) throws Exception
	{
		String pathKey = "fileconn.dir." + (external ? "memorycard." : "") + type;
		
		String path = System.getProperty(pathKey);
		
		if (path != null)
		{
			boolean success = wipeFilePath(path, wl);
	
			return success;
			
		}
		else
			return false;
		
	}
	
	 

	
	public boolean isFileAPIAvailable ()
	{
		  boolean isAPIAvailable = false;
		    if (System.getProperty(
		      "microedition.io.file.FileConnection.version") != null)
		    {
		      isAPIAvailable = true;
		    }
		    
		    return isAPIAvailable;
	}
	
	
	public static boolean doWipe (boolean wipeContacts, boolean wipeEventsTodos, boolean wipePhotos, boolean wipeMemoryCard, LargeStringCanvas canvas, WipeListener wipeListener)
	{
		
		L10nResources l10n = LocaleManager.getResources(); 

		canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_PREPARING_WIPE));
		WipeController wc = new WipeController();
		
		
		doSecPause (1);
		
		if (wipeContacts || wipeEventsTodos)
		{
			try
			{
				canvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_PERSONAL));
				doSecPause (1);
				wc.wipePIMData(wipeContacts, wipeEventsTodos, wipeEventsTodos);
				canvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_PERSONAL_COMPLETE));
			}
			catch (Exception e)
			{
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_ERROR));
				e.printStackTrace();
			}
			
			doSecPause (3);
		}
		
		if (wipePhotos)
		{
			canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_PHOTOS));
			try {
				wc.wipeMedia(WipeController.TYPE_PHOTOS,false,wipeListener);
				wc.wipeMedia(WipeController.TYPE_PHOTOS,true,wipeListener);
				
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_PHOTOS_COMPLETE));
			} catch (Exception e) {
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_PHOTOS_ERROR)+'\n'+e.getMessage());
				e.printStackTrace();
			}
			
			doSecPause (3);
		
			canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_VIDEOS));
			try {
				wc.wipeMedia(WipeController.TYPE_VIDEOS,false,wipeListener);
				wc.wipeMedia(WipeController.TYPE_VIDEOS,true,wipeListener);
				
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_VIDEOS_COMPLETE));
			} catch (Exception e) {
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_VIDEOS_ERROR)+'\n'+e.getMessage());
				e.printStackTrace();
			}
			
			doSecPause (3);
			
			canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_RECORDINGS));
			try {
				wc.wipeMedia(WipeController.TYPE_RECORDINGS,false,wipeListener);
				wc.wipeMedia(WipeController.TYPE_RECORDINGS,true,wipeListener);
				
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_RECORDINGS_COMPLETE));
			} catch (Exception e) {
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_RECORDINGS_ERROR)+'\n'+e.getMessage());
				e.printStackTrace();
			}
			
			doSecPause (3);

		}

		if (wipeMemoryCard)
		{
			canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_FILES));
			try {
				wc.wipeMedia(WipeController.TYPE_MEMORYCARD,false,wipeListener);
				
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_ROOTS));
				
				wc.wipeAllRootPaths(wipeListener);

				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_FILES_COMPLETE));
				
			} catch (Exception e) {
				canvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_WIPE_FILES_ERROR) + '\n' + e.getMessage());
				Logger.error("wipeController", "error wiping files", e);

			}
			
			doSecPause (3);

		}
		
		
		return true;
		
		
	}
	
	private static void doSecPause (int secs)
	{
		try { Thread.sleep(secs);}
		catch(Exception e){}
	}
}

/*
System.getProperty("fileconn.dir.photos"); 
System.getProperty("fileconn.dir.photos.name"); 
System.getProperty("fileconn.dir.memorycard.photos"); 
System.getProperty("fileconn.dir.memorycard.photos.name"); 
System.getProperty("fileconn.dir.videos"); 
System.getProperty("fileconn.dir.videos.name"); 
System.getProperty("fileconn.dir.memorycard.videos"); 
System.getProperty("fileconn.dir.memorycard.videos.name"); 
System.getProperty("fileconn.dir.tones"); 
System.getProperty("fileconn.dir.tones.name"); 
System.getProperty("fileconn.dir.memorycard.tones"); 
System.getProperty("fileconn.dir.memorycard.tones.name"); 
System.getProperty("fileconn.dir.graphics"); 
System.getProperty("fileconn.dir.graphics.name"); 
System.getProperty("fileconn.dir.memorycard.graphics"); 
System.getProperty("fileconn.dir.memorycard.graphics.name"); 
System.getProperty("fileconn.dir.music"); 
System.getProperty("fileconn.dir.music.name"); 
System.getProperty("fileconn.dir.memorycard.music"); 
System.getProperty("fileconn.dir.memorycard.music.name"); 
System.getProperty("fileconn.dir.recordings"); 
System.getProperty("fileconn.dir.recordings.name"); 
System.getProperty("fileconn.dir.memorycard.recordings"); 
System.getProperty("fileconn.dir.memorycard.recordings.name"); 
*/