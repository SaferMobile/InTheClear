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

	private final static String ZERO_STRING = "000000000000000"; //limit to 15 for name and phone number fields
	private final static int DEFAULT_ZERO_AMOUNT = 1000000000;
	
	private static Random RAND = new Random(); 

	
		
	public void test ()
	{
		try
		{
			//first remove the contacts that are there
			wipePIMItemsByType(PIM.CONTACT_LIST);
			
			//second fill up the contact list until an error is thrown
			zeroContacts(DEFAULT_ZERO_AMOUNT);
		}
		catch (PIMException pe)
		{
			pe.printStackTrace();
		}
	}
	
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
	
	public static Vector getContacts () throws PIMException
	{

		Vector result = new Vector();
		
		Contact c = null;

		ContactList clist = null;
		
		// Open default contact list
		PIM pim = PIM.getInstance();
		
		clist = (ContactList) pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);

		// Retrieve contact values
		// The countValues() method returns the number of data values currently
		// set in a particular field.
		Enumeration contacts = clist.items();
		int idx = 0;
		
		while (contacts.hasMoreElements())
		{ 
			c = (Contact) contacts.nextElement();
			
			try
			{
			//	String num = c.getString(Contact.TEL, Contact.ATTR_PREFERRED);
				String[] name = c.getStringArray(Contact.NAME, PIMItem.ATTR_NONE);
				
				StringBuffer resultEntry = new StringBuffer();
				for (int i = 0; i < name.length; i++)
				{
					if (name[i] != null && name[i].length() > 0)
					{
					resultEntry.append(name[i]);
					resultEntry.append(' ');
					}
				}
				
				result.addElement(resultEntry.toString());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}

		
		clist.close();
		
		return result;
	}
	
	public static void wipeContacts () throws PIMException
	{
		wipePIMItemsByType(PIM.CONTACT_LIST);
	}
	
	public static void wipeEvents () throws PIMException
	{
		wipePIMItemsByType(PIM.EVENT_LIST);
	}
	
	public static void wipeToDos () throws PIMException
	{
		wipePIMItemsByType(PIM.TODO_LIST);		
	}
	
	public static void wipePIMItemsByType (int pimType) throws PIMException
	{

		PIMList pList = null;
		
		// Open default contact list
		PIM pim = PIM.getInstance();

		String[] pimLists = pim.listPIMLists(pimType);
		
		for (int i = 0; i < pimLists.length; i++)
		{
			pList = (PIMList) pim.openPIMList(pimType, PIM.READ_WRITE, pimLists[i]);
	 

			String[] cats = pList.getCategories();
			
			for (int n = 0; n < cats.length; n++)
			{
				wipeListCategory(pList, cats[n]);
			}
			
			wipeList(pList);
			
			pList.close();
		}
		
	}
	
	private static void wipeListCategory (PIMList pList, String category) throws PIMException
	{
		PIMItem pItem = null;

		Enumeration enumItems = pList.itemsByCategory(category);
		
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
		}

	}
	
	private static void wipeList (PIMList pList) throws PIMException
	{
		PIMItem pItem = null;

		Enumeration enumItems = pList.items();
		
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
		}

	}
	
	private static void log(String msg)
	{
		System.out.println(msg);
	}
	
	
	
	public static void zeroContacts (int max) throws PIMException
	{
		log ("zeroing contacts");

		Contact c = null;

		ContactList clist = null;
		
		// Open default contact list
		PIM pim = PIM.getInstance();
		
		clist = (ContactList) pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
			

		for (int i = 0; i < max; i++)
		{
			log ("zeroing contact: " + i);
			//Add contact values
			c = clist.createContact();
			int attrs = Contact.ATTR_HOME;
			c.addString(Contact.TEL, attrs, ZERO_STRING);
			// Some fields can be added without attributes
			
			String[] rName = {"",ZERO_STRING,"",ZERO_STRING,""};
			c.addStringArray(Contact.NAME, PIMItem.ATTR_NONE, rName);
			
			// Add the item to the native contact database
			c.commit();
		}
		
		clist.close();
	}
	
	
	
	public static String generateRandomNumber (int digits)
	{
		StringBuffer buffer = new StringBuffer();
		
		
		 for (int j=0;j < digits;j++)
		 {
			 buffer.append(((int)(RAND.nextFloat()*10))+"");
		 } 
		 
		 return buffer.toString();
	}
	
}
