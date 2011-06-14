package org.safermobile.clear.micro.apps.controllers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
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
	
	public void wipeContacts () throws PIMException
	{
		PIMWiper.removeContacts();
	}
	
	public void wipeCalendar () throws PIMException
	{
		PIMWiper.removeCalendarEntries();
	}
	
	public void wipeToDo () throws PIMException
	{
		
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
	
	public boolean wipeFiles (String path) throws IOException
	{
		if (isFileAPIAvailable())
		{
			
			   FileConnection fc = (FileConnection) Connector.open("file://localhost" + path);
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
		    			  FileConnection fcNext = (FileConnection)enumFiles.nextElement();
		    			  wipeFiles(fcNext.getPath());
		    		  }
		    		  
		    	  }
		    	  else
		    	  {
		    		  fc.delete();
		    	  }
		    	  
		      }
			
			return true;
		}
		else
			return false;
	}
	
	public void wipePhotos () throws IOException
	{
		wipeFiles("/DCIM");
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
