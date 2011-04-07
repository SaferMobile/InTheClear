
package org.safermobile.clear.micro.data;

import java.util.Enumeration;
import java.util.Vector;


import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pim.PIMException;

public class ContactsMidlet extends MIDlet implements Runnable, CommandListener {

	private DisplayManager manager;
	private List listMain;
	private Command	 cmdRun;
	private Command	 cmdBack;
	
	private Command	cmdNo = new Command("No", Command.BACK, 2);
	private Command	cmdYes = new Command("Yes", Command.OK, 1);
	private Command	cmdHome = new Command("Home", Command.OK, 3);
    
	private int lastCmdIdx = 1;
	
	
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public ContactsMidlet() {
		this.manager = new DisplayManager(Display.getDisplay(this));
		this.cmdRun = new Command("Run", Command.OK, 4);
		this.cmdBack = new Command("Back", Command.BACK, 5);

		this.listMain = getListMain();
		this.listMain.setCommandListener(this);
		this.listMain.addCommand(this.cmdRun);
		
		
	}

	private List getListMain() {
		listMain = new List("DevoidME [Main Menu]", List.IMPLICIT);
		

		listMain.append("List Contacts", null);
		listMain.append("Delete All Contacts", null);
		listMain.append("Random Fill Contacts (100)", null);
		listMain.append("Zero Fill Contacts (100)", null);
		
		return listMain;
	}
	
	public void displayContactList ()
	{
	 	List listContacts = new List("AntiSocial", List.IMPLICIT);
		listContacts.setCommandListener(this);
		
	 	try {
			Vector contacts = PIMWiper.getContacts();
			
			Enumeration enumContacts = contacts.elements();
			while(enumContacts.hasMoreElements())
			{
				String contact = (String)enumContacts.nextElement();
				listContacts.append(contact, null);
			}
			
		} catch (PIMException e) {
			e.printStackTrace();
		}
	 	
	 	
		listContacts.addCommand(cmdBack);
		
		manager.next(listContacts);
		
	}
	
	public void showYesNo ()
	{
		Alert yesNoAlert = new Alert("Attention");
        yesNoAlert.setString("Are you sure?");
        yesNoAlert.addCommand(cmdNo);
        yesNoAlert.addCommand(cmdYes);
        yesNoAlert.setCommandListener(this);
        manager.next(yesNoAlert);
	}
	
	public void showAlert (String title, String msg, Displayable next)
	{
		Alert alert = new Alert(title);
		alert.setString(msg);
        manager.next(alert, next);
	}
	
	public void run ()
	{
		try
		{
			switch (lastCmdIdx)
			{

				case 0:
					displayContactList();
				break;
				case 1:
					PIMWiper.removeContacts();
					showAlert("Removed","All of your contacts have been removed from your phone.", listMain);
				break;
				case 2:
					PIMWiper.fillContacts(100);
					showAlert("Random","100 randomly generated contacts have been added to your phone", listMain);
				break;
				case 3:
					PIMWiper.zeroContacts(100);
					showAlert("Zero","100 'zeroed' contacts have been added to overwrite memory on your phone", listMain);

				break;
				
					
			}
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (PIMException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		this.manager.next(this.listMain);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == this.cmdRun || command == List.SELECT_COMMAND) {
			
			lastCmdIdx = listMain.getSelectedIndex();
			
			if (lastCmdIdx == 0)
			{
				new Thread(this).start();
			}
			else
			{
				showYesNo();
			}
			
			/*
			if (displayable == this.screen1) {
				this.manager.next(this.screen2);
			} else
			if (displayable == this.screen2) {
				this.manager.next(this.screen3);
			}*/
			
		}
		else if (command == this.cmdYes)
		{
			new Thread(this).start();
		}
		else if (command == this.cmdHome)
		{
			this.manager.next(listMain);
		}
		else if (command == this.cmdBack || command == this.cmdNo) {
			this.manager.back();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}
}
