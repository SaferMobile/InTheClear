package org.safermobile.clear.micro.apps.controllers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.pim.PIMException;

import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.data.PIMWiper;
import org.safermobile.micro.utils.Logger;

public class WipeController {

	/*
	private final static String UP_DIRECTORY = "..";
	  private final static String MEGA_ROOT = "/";
	  private final static String SEP_STR = "/";
	  private final static char   SEP = '/';
	*/
	
	  private boolean keepRunning = true;
	
	public final static String TYPE_PHOTOS = "photos";
	public final static String TYPE_VIDEOS = "videos";
	public final static String TYPE_RECORDINGS = "recordings";
	public final static String TYPE_MEMORYCARD = "memorycard";
	
	
	  
	  
	public Vector getContacts () throws PIMException
	{
		return PIMWiper.getContacts();
		
	}
	
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
	
	public void cancel ()
	{
		keepRunning = false;
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
	
	public void fillContactsRandom (int fillItemCount) throws Exception
	{
		PIMWiper.fillContacts(fillItemCount);

	}
	
	public void fillContactsZero (int fillItemCount) throws Exception
	{
		PIMWiper.zeroContacts(fillItemCount);

	}
	
	public void fillSMS (int fillItemCount)
	{
		
	}
	
	public boolean wipeAllRootPaths (WipeListener wl) throws IOException
	{
		Enumeration drives = FileSystemRegistry.listRoots();
		boolean success = false;
		
		while (drives.hasMoreElements() && keepRunning)
		{
			String root =  drives.nextElement().toString();
			String path = "file:///" + root;
			
			try
			{
			
				wipeFilePath(path, wl);
				success = true;
			}
			catch (Exception e)
			{
				wl.wipingFileError(path,e.getMessage());
				
			}
		}
		
		return success;
	}
	
	public boolean wipeFilePath (String path, WipeListener wl) throws Exception
	{

		if (!keepRunning) //this should stop most everything
			return false;
		
		Logger.debug(ITCConstants.TAG, "wipeFilePath called: " + path);

		   
		   FileConnection fc = (FileConnection) Connector.open(path, Connector.READ);

		   wl.wipingFileSuccess(path);		   
		   
	      if (!fc.exists()) 
	      {
	    	  
	        throw new IOException("File does not exists");
	      }
	      else
	      {
	    	  
	    	  if (fc.isDirectory())
	    	  {
	    		  Enumeration enumFiles = fc.list();
	    		  while (enumFiles.hasMoreElements())
	    		  {
	    			  String fcNext = enumFiles.nextElement().toString();
	    			  
	    			  try
	    			  {
	    				  wipeFilePath(fc.getURL() + fcNext, wl);
	    			  }
	    			  catch (Exception e)
	    			  {
	    				  wl.wipingFileError(fc.getURL(),"ERROR: unable to delete: " + e.getMessage());
	    			  }
	    		  }
	    		  
	    		  if (fc.canWrite())
	    		  {
	    			  fc.close();
	    			  fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
	    			  fc.delete();
	    			  fc.close();
	    			  
	    		  }
	    	  }
	    	  else
	    	  {
	    		  if (fc.canWrite())
	    		  {
	    			  fc.close();
	    			  fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
	    			  fc.delete();
	    			  fc.close();
	    			  
	    		  }
	    		  else
	    		  {
	    			  wl.wipingFileError(path,"cannot wipe file (read only)");
	    			  
	    		  }
	    	  }
	    	  
	      }
		
		return true;
	
	}
	
	public boolean wipeMedia (String type, boolean external, WipeListener wl) throws Exception
	{
		String pathKey = "fileconn.dir." + (external ? "memorycard." : "") + type;
		
		String path = System.getProperty(pathKey);
		
		if (path != null)
		{
			return wipeFilePath(path, wl);
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