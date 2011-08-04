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

	private final static String UP_DIRECTORY = "..";
	  private final static String MEGA_ROOT = "/";
	  private final static String SEP_STR = "/";
	  private final static char   SEP = '/';
	
	  private boolean keepRunning = true;
	  
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
	
	public void wipeMemoryCard (WipeListener wl) throws Exception
	{
		
		String memcardPath = System.getProperty("fileconn.dir.memorycard");
		
		if (memcardPath != null)
			wipeFilePath(memcardPath, wl);
		else
			throw new IOException("Cannot find memory card folder");

	}
	
	public void wipePhotos (WipeListener wl) throws Exception
	{
		String photosPath = System.getProperty("fileconn.dir.photos");
		
		if (photosPath != null)
			wipeFilePath(photosPath, wl);
		else
			throw new IOException("Cannot find photos folder");
	}
	
	public void wipeVideos (WipeListener wl) throws Exception
	{
		String videosPath = System.getProperty("fileconn.dir.videos");
		
		if (videosPath != null)
			wipeFilePath(videosPath, wl);
		else
			throw new IOException("Cannot find videos folder");
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
