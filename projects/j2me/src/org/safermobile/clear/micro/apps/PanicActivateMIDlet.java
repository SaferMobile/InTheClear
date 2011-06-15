/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.apps;
import java.io.IOException;
import java.util.Date;

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
import org.safermobile.clear.micro.apps.controllers.ShoutController;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.data.PhoneInfo;
import org.safermobile.clear.micro.sms.SMSManager;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;
import org.safermobile.micro.utils.StringTokenizer;

// hasLocationCapability=false
//release.build = false
public class PanicActivateMIDlet extends MIDlet implements CommandListener, Runnable {

	
	private DisplayManager _manager;
	private Display _display;

	private TextBox _tbMain;
	private LargeStringCanvas _lsCanvas;
	
	private Command	 _cmdCancel;
	private Command	 _cmdExit;
	
	L10nResources l10n = LocaleManager.getResources();

	/*
	 * the thread which manages the panic sending
	 */
	private Thread _thread;
	
	/*
	 * used to cancel the panic loop
	 */
	private boolean _keepPanicing = false; 
	
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
		
		_cmdCancel = new Command(l10n.getString(L10nConstants.keys.KEY_PANIC_BTN_CANCEL), Command.SCREEN,1);
		_cmdExit = new Command("Exit", Command.EXIT,1);
		
		_tbMain = new TextBox(l10n.getString(L10nConstants.keys.KEY_PANIC_TITLE_MAIN), "", 500, TextField.ANY);
		
		_tbMain.setCommandListener(this);
		
		_tbMain.addCommand(_cmdCancel);
	
	}

	
	private void startPanic ()
	{
		try {

			_prefs = new Preferences (PanicConstants.PANIC_PREFS_DB);
			
			String recipients = _prefs.get(PanicConstants.PREFS_KEY_RECIPIENT);
			
			if (recipients == null)
			{
				showMessage("Please run the 'Panic! Config' app first to enter your alert information.");
				_tbMain.removeCommand(_cmdCancel);
				_tbMain.addCommand(_cmdExit);

			}
			else
			{

				_keepPanicing = true;
				
				//startSmsServer();
				
				_thread = new Thread(this);
				_thread.start();
			}
		
		} catch (RecordStoreException e) {
			
			Logger.error(PanicConstants.TAG, "error access preferences", e);
			showAlert(l10n.getString(L10nConstants.keys.KEY_PANIC_TITLE_ERROR),l10n.getString(L10nConstants.keys.KEY_PANIC_ERROR_PREFS),null);
		}
	}
	
	private void stopPanic ()
	{

		_keepPanicing = false;
		_thread.interrupt();
	}

	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		_manager.next(_tbMain);
		
		Thread thread = new Thread ()
		{
			public void run ()
			{
				startPanic();
			}
		};
		thread.start();
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
	
	
	private void doSecPause (int secs)
	{
		try { Thread.sleep(secs * 1000);}
		catch(Exception e){}
	}
	
	public void run ()
	{
		ShoutController sControl = new ShoutController();
		
		Logger.debug(PanicConstants.TAG, "starting panic run(); loading prefs...");
		
		String recipients = _prefs.get(PanicConstants.PREFS_KEY_RECIPIENT);
		String userName =  _prefs.get(PanicConstants.PREFS_KEY_NAME);
		String userMessage = _prefs.get(PanicConstants.PREFS_KEY_MESSAGE);
		String userLocation = _prefs.get(PanicConstants.PREFS_KEY_LOCATION);
		
		String panicMsg = sControl.buildShoutMessage(userName, userMessage, userLocation);
		String panicData = sControl.buildShoutData (userName);
		
		showMessage ("PANIC MESSAGE: " + panicMsg + "\n\npreparing to send...");
		
		doSecPause (5);
		
		_lsCanvas = new LargeStringCanvas("");
		_lsCanvas.setCommandListener(this);
		_lsCanvas.addCommand(_cmdCancel);
		
		_manager.next(_lsCanvas);
		
		for (int i = 5; i > 0; i--)
		{
			showMessage("Sending in " + i + "...");			
			doSecPause (1);
		}
		
		int resendTimeout = PanicConstants.DEFAULT_RESEND_TIMEOUT; //one minute
		
		boolean wipeComplete = false;
		
		while (_keepPanicing)
		{
			try
			{
				showMessage ("Sending messages...");
				sControl.sendSMSShout (recipients, panicMsg, panicData);			
				showMessage ("Panic Sent!");

				doSecPause (2);
			}
			catch (Exception e)
			{
				_display.setCurrent(_tbMain);
				doSecPause (1);
				showMessage("Error Sending: " + e.toString());
				doSecPause (10);

			}

			//now that first shout has been sent, time to wipe
			if (!wipeComplete)
			{
				showMessage("Preparing to wipe data...");
				WipeController wc = new WipeController();
				
				String prefBool = _prefs.get(PanicConstants.PREFS_KEY_WIPE_CONTACTS);
				boolean wipeContacts = (prefBool != null && prefBool.equals("true"));
				
				prefBool = _prefs.get(PanicConstants.PREFS_KEY_WIPE_EVENTS);
				boolean wipeEvents = (prefBool != null && prefBool.equals("true"));
				
				prefBool = _prefs.get(PanicConstants.PREFS_KEY_WIPE_TODOS);
				boolean wipeToDos = (prefBool != null && prefBool.equals("true"));
				
				prefBool = _prefs.get(PanicConstants.PREFS_KEY_WIPE_PHOTOS);
				boolean wipePhotos = (prefBool != null && prefBool.equals("true"));
				
				prefBool = _prefs.get(PanicConstants.PREFS_KEY_WIPE_ALL_FILES);
				boolean wipeAllFiles = (prefBool != null && prefBool.equals("true"));
				
				doSecPause (1);
				showMessage("Wiping selected personal data...");
				
				try
				{
					wc.wipePIMData(wipeContacts, wipeEvents, wipeToDos);
					showMessage("Success! Personal data wiped!");
				}
				catch (Exception e)
				{
					showMessage("WARNING: There was an error wiping your personal data.");
					e.printStackTrace();
				}
				
				doSecPause (3);
				
				
				if (wipeAllFiles)
				{
					showMessage("Wiping all available files...");
					try {
						wc.wipeAllRootPaths();
						showMessage("Wiping all available files... WIPE COMPLETE.");
					} catch (IOException e) {
						showMessage("Wiping all photos... ERROR. UNABLE TO WIPE ALL FILES.");
						e.printStackTrace();
					}
				}
				else if (wipePhotos)
				{
					showMessage("Wiping all photos...");
					try {
						wc.wipePhotos();
						showMessage("Wiping all photos... WIPE COMPLETE.");
					} catch (IOException e) {
						showMessage("Wiping all photos... ERROR. UNABLE TO WIPE PHOTOS.");
						e.printStackTrace();
					}
				}
				
				wipeComplete = true;
			}
			
			int secs = resendTimeout/1000;
			
			while (secs > 0)
			{
				showMessage("Panic! again in " + secs + "secs...");
				doSecPause (1);
				secs--;
			}
			
			//update message with new mobile cid, lac info
			panicMsg = sControl.buildShoutMessage(userName, userMessage, userLocation);
			
		}
		

		_manager.next(_tbMain);
	}
	
	
	
	private void showMessage (String msg)
	{
		Logger.debug(PanicConstants.TAG, "msg: " + msg);

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
		else if (_display.getCurrent() == _lsCanvas)
		{
			_lsCanvas.setLargeString(msg);
		}
	}
	
	
	
	
}
