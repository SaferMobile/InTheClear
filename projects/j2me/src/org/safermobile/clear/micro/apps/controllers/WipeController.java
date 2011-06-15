package org.safermobile.clear.micro.apps.controllers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.pim.PIMException;

import org.safermobile.clear.micro.data.PIMWiper;

public class WipeController {

	private final static String UP_DIRECTORY = "..";
	  private final static String MEGA_ROOT = "/";
	  private final static String SEP_STR = "/";
	  private final static char   SEP = '/';
	
	public Vector getContacts () throws PIMException
	{
		return PIMWiper.getContacts();
		
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
		
		while (drives.hasMoreElements())
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
				wl.wipingFile("ERROR: " + path);
				//just catch these individually
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	public boolean wipeFilePath (String path, WipeListener wl) throws Exception
	{

		   wl.wipingFile(path);
		   
		   FileConnection fc = (FileConnection) Connector.open(path, Connector.READ);
		   
		   
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
	    				  wl.wipingFile("ERROR: unable to delete: " + fc.getURL() + ": " + e.getMessage());
	    			  }
	    		  }
	    		  
	    		  wipeFilePath(fc.getURL(), wl);
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
	    			  wl.wipingFile("cannot wipe file (read only): " + path);
	    			  
	    		  }
	    	  }
	    	  
	      }
		
		return true;
	
	}
	
	public void wipePhotos (WipeListener wl) throws Exception
	{
		String photosPath = System.getProperty("fileconn.dir.photos");
		
		if (photosPath != null)
			wipeFilePath(photosPath, wl);
		else
			throw new IOException("Cannot find photos folder");
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
