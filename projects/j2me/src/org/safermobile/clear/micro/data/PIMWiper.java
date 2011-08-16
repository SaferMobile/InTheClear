/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.data;

import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.Event;
import javax.microedition.pim.EventList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;
import javax.microedition.pim.PIMList;
import javax.microedition.pim.ToDo;
import javax.microedition.pim.ToDoList;


/*
 * 
 * based on code from this example: http://developers.sun.com/mobility/apis/articles/pim/index.html
 */
public class PIMWiper {

	private final static String ZERO_STRING = "000000000000"; //limit to 15 for name and phone number fields
	
	//private static Random RAND = new Random(); 

	public static boolean hasPIM ()
	{
		String currentVersion = System.getProperty("microedition.pim.version " );

		return (currentVersion != null);
	}
	
	public static String getVersion ()
	{
		String currentVersion = System.getProperty("microedition.pim.version " );

		return currentVersion;
	}
	
	public static boolean checkContacts ()
	{
		return checkPermission(PIM.CONTACT_LIST, PIM.READ_WRITE);
	}
	
	public static boolean checkEventsAndToDo ()
	{
		boolean result = checkPermission(PIM.EVENT_LIST, PIM.READ_WRITE);
		
		if (result)
		{
			result = checkPermission(PIM.TODO_LIST, PIM.READ_WRITE);
		}
		
		return result;
	}
	
	public static boolean checkPermission (int type, int perm)
	{
		// Open default contact list
		PIM pim = PIM.getInstance();
		
		try {
			pim.openPIMList(type, perm);
			return true;
		} catch (PIMException e) {
			return false;
		}

	}
	
	
	
	public static int wipeContacts () throws PIMException
	{
		int result = wipePIMItemsByType(PIM.CONTACT_LIST);
		
		try
		{
			zeroContacts(result);
		
			result = wipePIMItemsByType(PIM.CONTACT_LIST);
		}
		catch (PIMException pe)
		{
			log("error zeroing contacts: " + pe.getMessage());
		}
		
		return result;
	}
	
	public static int wipeEvents () throws PIMException
	{
		return wipePIMItemsByType(PIM.EVENT_LIST);
	}
	
	public static int wipeToDos () throws PIMException
	{
		return wipePIMItemsByType(PIM.TODO_LIST);		
	}
	
	public static int wipePIMItemsByType (int pimType) throws PIMException
	{

		PIMList pList = null;
		
		int result = 0;
		
		PIM pim = PIM.getInstance();

		String[] pimLists = pim.listPIMLists(pimType);
		
		for (int i = 0; i < pimLists.length; i++)
		{
			pList = (PIMList) pim.openPIMList(pimType, PIM.READ_WRITE, pimLists[i]);
	 
			String[] cats = pList.getCategories();
			
			for (int n = 0; n < cats.length; n++)
			{
				result += wipeList(pList, pList.itemsByCategory(cats[n]));
			}
			
			result += wipeList(pList, pList.items());
			
			pList.close();
		}
		
		return result;
		
	}
	
	private static int wipeList (PIMList pList, Enumeration enumItems) throws PIMException
	{
		PIMItem pItem = null;

		int result = 0;
		
		while (enumItems.hasMoreElements())
		{
			pItem = (PIMItem) enumItems.nextElement();
			
			
			if (pItem instanceof Contact)
			{
				((ContactList)pList).removeContact((Contact)pItem);
			}
			else if (pItem instanceof Event)
			{
				((EventList)pList).removeEvent((Event)pItem);
			}
			else if (pItem instanceof ToDo)
			{
				((ToDoList)pList).removeToDo((ToDo)pItem);
			}
			
			result++;
		}
		
		return result;

	}
	
	private static void log(String msg)
	{
		System.out.println(msg);
	}
	
	
	
	public static void zeroContacts (int max) throws PIMException
	{
		Contact c = null;

		ContactList clist = null;
		
		// Open default contact list
		PIM pim = PIM.getInstance();
		
		clist = (ContactList) pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
			

		for (int i = 0; i < max; i++)
		{
			//Add contact values
			c = clist.createContact();
			int attrs = Contact.ATTR_HOME;
			
		    if (clist.isSupportedField(Contact.TEL))
		    	c.addString(Contact.TEL, attrs, ZERO_STRING);
			// Some fields can be added without attributes
			
		   if (clist.isSupportedField(Contact.FORMATTED_NAME)) {
	            // the contact implementation doesn't have individual  name
	            // fields, so try the single name field FORMATTED_NAME
	           c.addString(Contact.FORMATTED_NAME, 
	                PIMItem.ATTR_NONE, ZERO_STRING + ' ' + ZERO_STRING);
	        }
		    
		    if (clist.isSupportedField(Contact.NOTE))
	            c.addString(Contact.NOTE, PIMItem.ATTR_NONE, ZERO_STRING);
		    
		    if (clist.isSupportedField(Contact.ORG))
		    	c.addString(Contact.ORG, PIMItem.ATTR_NONE, ZERO_STRING);
				
			// Add the item to the native contact database
			c.commit();
		}
		
		clist.close();
	}
	
	
	/*
	public static String generateRandomNumber (int digits)
	{
		StringBuffer buffer = new StringBuffer();
		
		
		 for (int j=0;j < digits;j++)
		 {
			 buffer.append(((int)(RAND.nextFloat()*10))+"");
		 } 
		 
		 return buffer.toString();
	}*/
	
}
