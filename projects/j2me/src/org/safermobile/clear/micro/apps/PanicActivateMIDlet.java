/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.apps;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.controllers.PanicController;
import org.safermobile.clear.micro.apps.controllers.ShoutController;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.apps.controllers.WipeListener;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

// hasLocationCapability=false
//release.build = false
public class PanicActivateMIDlet extends MIDlet implements CommandListener, WipeListener {

	
	private DisplayManager _manager;
	private Display _display;

	private TextBox _tbMain;

	private Command	 _cmdPanic;
	private Command	 _cmdCancel;
	private Command	 _cmdExit;
	
	L10nResources l10n = LocaleManager.getResources();

	/*
	 * the thread which manages the panic sending
	 */
	private Thread _thread;
	private PanicController _pc;
	
	/*
	 * stores the user data between the config app and this one
	 */
	private Preferences _prefs = null; 
	
	
	/**
	 * Creates Panic Activate app
	 */
	public PanicActivateMIDlet() {
		
		_display = Display.getDisplay(this);
		_manager = new DisplayManager(_display);
		
		_tbMain = new TextBox(l10n.getString(L10nConstants.keys.PANIC_TITLE), "", 500, TextField.ANY);
		
		_tbMain.setCommandListener(this);

		_cmdPanic = new Command(l10n.getString(L10nConstants.keys.MENU_PANIC), Command.SCREEN,1);		
		_cmdCancel = new Command(l10n.getString(L10nConstants.keys.MENU_CANCEL), Command.SCREEN,1);
		_cmdExit = new Command(l10n.getString(L10nConstants.keys.MENU_EXIT), Command.EXIT,1);
		
		_tbMain.addCommand(_cmdExit);

	}

	
	private void startPanic ()
	{
		
		_tbMain.removeCommand(_cmdPanic);
		_tbMain.removeCommand(_cmdExit);
		_tbMain.addCommand(_cmdCancel);
	
		_pc = new PanicController(_prefs, this, this, _manager, _cmdCancel);
		_thread = new Thread(_pc);
		_thread.start();
		
	}
	
	private void stopPanic ()
	{
		_pc.stopPanic();
		
		_thread.interrupt();
	}

	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		_manager.next(_tbMain);
		
		try
		{
		
			_prefs = new Preferences (ITCConstants.PANIC_PREFS_DB);
			String oneTouch = _prefs.get(ITCConstants.PREFS_KEY_ONE_TOUCH_PANIC);
			String recipients = _prefs.get(ITCConstants.PREFS_KEY_RECIPIENT);

			if (recipients == null)
			{

				_tbMain.setString(l10n.getString(L10nConstants.keys.ERROR_RUN_SETUP));

			}
			else if (oneTouch == null || oneTouch.equals("false"))
			{
				_tbMain.setString(l10n.getString(L10nConstants.keys.PANIC_PRESS_TO_ACTIVATE));
				_tbMain.addCommand(_cmdPanic);
			}
			else
			{
				startPanic();
			}
		
		} catch (RecordStoreException e) {
			
			Logger.error(ITCConstants.TAG, "error access preferences", e);
			showAlert(l10n.getString(L10nConstants.keys.TITLE_ERROR),l10n.getString(L10nConstants.keys.ERROR_PREFS),null);
		}
		
	}
	
	public void showAlert (String title, String msg, Displayable next)
	{
		Alert alert = new Alert(title);
		alert.setString(msg);
        _manager.next(alert, next);
        
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		
		if (command == _cmdCancel) 
		{
			stopPanic ();
			
			try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.notifyDestroyed();
		}
		else if (command == _cmdExit)
		{
			try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.notifyDestroyed();
		}
		else if (command == _cmdPanic)
		{
			startPanic();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		
		
		
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}
	
	
	
	private void showMessage (String msg)
	{
		Logger.debug(ITCConstants.TAG, "msg: " + msg);

		if (_display.getCurrent() == _tbMain)
		{
			try
			{
				_tbMain.setString(msg);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	public void wipingFileSuccess(String path) {
		
		showMessage("wiping: " + path);
		Logger.debug(ITCConstants.TAG, "wiping: " + path);

	}
	
	public void wipingFileError(String path, String msg) {
		
		showMessage("ERROR wiping: " + path);
		Logger.debug(ITCConstants.TAG, "wiping error: " + path + ": " + msg);

	}


	public void wipePercentComplete(int percent) {
		
		//nothing so far
		
	}
	
	
	
	
}
