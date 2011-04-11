/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */
package org.safermobile.clear.micro.apps;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.pim.PIMException;

import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.data.PIMWiper;
import org.safermobile.micro.ui.DisplayManager;

public class AntiSocialMIDlet extends MIDlet implements Runnable, CommandListener {

	private DisplayManager manager;
	private List listMain;
	private Command	 cmdRun;
	private Command	 cmdBack;
	
	L10nResources l10n = L10nResources.getL10nResources(null);
	
	
	private Command	cmdNo = new Command(l10n.getString(L10nConstants.keys.KEY_0), Command.BACK, 2);
	private Command	cmdYes = new Command(l10n.getString(L10nConstants.keys.KEY_1), Command.OK, 1);
	private Command	cmdHome = new Command(l10n.getString(L10nConstants.keys.KEY_2), Command.OK, 3);
    
	private int lastCmdIdx = 1;
	
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public AntiSocialMIDlet() {
		this.manager = new DisplayManager(Display.getDisplay(this));
		this.cmdRun = new Command(l10n.getString(L10nConstants.keys.KEY_3), Command.OK, 4);
		this.cmdBack = new Command(l10n.getString(L10nConstants.keys.KEY_4), Command.BACK, 5);

		this.listMain = getListMain();
		this.listMain.setCommandListener(this);
		this.listMain.addCommand(this.cmdRun);
		
		
	}

	private List getListMain() {
		listMain = new List(l10n.getString(L10nConstants.keys.KEY_5), List.IMPLICIT);
		

		listMain.append(l10n.getString(L10nConstants.keys.KEY_6), null);
		listMain.append(l10n.getString(L10nConstants.keys.KEY_7), null);
		listMain.append(l10n.getString(L10nConstants.keys.KEY_8), null);
		listMain.append(l10n.getString(L10nConstants.keys.KEY_9), null);
		
		return listMain;
	}
	
	public void displayContactList ()
	{
	 	List listContacts = new List(l10n.getString(L10nConstants.keys.KEY_10), List.IMPLICIT);
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
		Alert yesNoAlert = new Alert(l10n.getString(L10nConstants.keys.KEY_11));
        yesNoAlert.setString(l10n.getString(L10nConstants.keys.KEY_12));
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
					showAlert(l10n.getString(L10nConstants.keys.KEY_13),l10n.getString(L10nConstants.keys.KEY_14), listMain);
				break;
				case 2:
					PIMWiper.fillContacts(100);
					showAlert(l10n.getString(L10nConstants.keys.KEY_15),l10n.getString(L10nConstants.keys.KEY_16), listMain);
				break;
				case 3:
					PIMWiper.zeroContacts(100);
					showAlert(l10n.getString(L10nConstants.keys.KEY_17),l10n.getString(L10nConstants.keys.KEY_18), listMain);

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
