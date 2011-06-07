package org.safermobile.clear.micro.apps.controllers;

import java.util.Vector;

import javax.microedition.pim.PIMException;

import org.safermobile.clear.micro.data.PIMWiper;

public class WipeController {

	
	public Vector getContacts () throws PIMException
	{
		return PIMWiper.getContacts();
		
	}
	
	public void autoWipeAll () throws Exception
	{
		wipeContacts();
		wipeCalendar();
		wipeToDo();
		
		wipeFiles("/");		
		wipePhotos();
	}
	
	public void wipeContacts () throws PIMException
	{
		PIMWiper.removeContacts();
	}
	
	public void wipeCalendar () throws PIMException
	{
		
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
	
	public void wipeFiles (String path)
	{
		
	}
	
	public void wipePhotos ()
	{
		
	}
}
