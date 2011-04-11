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
import org.safermobile.clear.micro.data.PhoneInfo;
import org.safermobile.clear.micro.sms.SMSServer;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;
import org.safermobile.micro.utils.StringTokenizer;

public class PanicActivateMIDlet extends MIDlet implements CommandListener, Runnable {

	private DisplayManager _manager;
	private Display _display;


	private TextBox _screenMain;
	private Command	 _cmdCancel;
	
	private SMSServer _smsServer;
	private int SMS_SERVER_PORT = 0;
	
	private final static String TAG = "Panic";
	private final static int DEFAULT_RESEND_TIMEOUT = 60000;
	private final static String PANIC_PREFS_DB = "panicprefs";
	
	private Thread _thread;
	private boolean _keepPanicing = false;
	
	private Preferences _prefs = null;
	L10nResources l10n = L10nResources.getL10nResources(null);
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public PanicActivateMIDlet() {
		
		_display = Display.getDisplay(this);
		_manager = new DisplayManager(_display);
		
		_cmdCancel = new Command(l10n.getString(L10nConstants.keys.KEY_PANIC_BTN_CANCEL), Command.SCREEN,3);
		
		_screenMain = new TextBox(l10n.getString(L10nConstants.keys.KEY_PANIC_TITLE_MAIN), "", 100, TextField.ANY);
		_screenMain.setCommandListener(this);
		
		_screenMain.addCommand(_cmdCancel);

		
	}

	private void startPanic ()
	{
		try {

			_keepPanicing = true;
			_prefs = new Preferences (PANIC_PREFS_DB);
		
			String recipients = _prefs.get("user.recp");
			if (recipients == null)
			{
				showMessage("Please run the 'Panic! Config' app first to enter your alert information.");
				_screenMain.removeCommand(_cmdCancel);

			}
			else
			{
			
				startSmsServer();
				
				_thread = new Thread(this);
				_thread.start();
			}
		
		} catch (RecordStoreException e) {
			
			Logger.error(TAG, "error access preferences", e);
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
		
		_manager.next(_screenMain);
		startPanic();
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
	
	//Okay, here is where the meat of the app is

	
	
	private void startSmsServer ()
	{
		if (_smsServer == null)
		{
			_smsServer = new SMSServer (SMS_SERVER_PORT);
			try {
				_smsServer.start();
			} catch (IOException e) {
			
				showAlert("error",l10n.getString(L10nConstants.keys.KEY_PANIC_ERROR_SMS),_screenMain);
				Logger.error(TAG, "error starting sms server", e);
			}
		}
		
	}
	
	private void doSecPause (int secs)
	{
		try { Thread.sleep(secs * 1000);}
		catch(Exception e){}
	}
	
	public void run ()
	{
		
		String recipients = _prefs.get("user.recp");
		String userName =  _prefs.get("user.name");
		String userMessage = _prefs.get("user.msg");
		String userLocation = _prefs.get("user.loc");
		
		String panicMsg = buildPanicMessage(userName, userMessage, userLocation);
		
		showMessage ("PANIC MESSAGE READY: " + panicMsg);
		
		doSecPause (5);
		
		showMessage ("Sending panic in 5...");
		doSecPause (1);
		showMessage("4...");
		doSecPause (1);
		showMessage("3...");
		doSecPause (1);
		showMessage("2...");
		doSecPause (1);
		showMessage("1...");
		
		int resendTimeout = DEFAULT_RESEND_TIMEOUT; //one minute
		
		while (_keepPanicing)
		{
			sendSMSPanic (recipients, panicMsg);
			
			try { Thread.sleep(resendTimeout); }
			catch (Exception e){}
		}
	}
	
	private String buildPanicMessage (String userName, String userMessage, String userLocation)
	{
		
		
		StringBuffer sbPanicMsg = new StringBuffer();
		
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_FROM));
		sbPanicMsg.append(userName);
		sbPanicMsg.append(": ");
		sbPanicMsg.append(userMessage);
		sbPanicMsg.append(".");
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_LOCATION));
		sbPanicMsg.append(userLocation);

		
		//append loc info
		String cid = PhoneInfo.getCellId();
		if (cid != null)
		{
			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_CID));
			sbPanicMsg.append(cid);
		}
		
		String lac = PhoneInfo.getLAC();
		if (lac != null)
		{
			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_LAC));
			sbPanicMsg.append(lac);
		}
		
		//append timestamp
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_TIMESTAMP));
		sbPanicMsg.append(new Date().toString());
	
		return sbPanicMsg.toString();
	}
	
	private void showMessage (String msg)
	{
		_screenMain.setString(msg);
	}
	
	private void sendSMSPanic (String recipients, String alertMsg)
	{
		
		StringTokenizer st = new StringTokenizer(recipients,",");
		
		while (st.hasMoreTokens())
		{
			String recp = st.nextToken().trim();
			showMessage ("sending panic to: " + recp);
			_smsServer.sendSMSAlert(recp, alertMsg);
			doSecPause (2);
			
		}
		
		//showAlert(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_SENT), l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_SENTTO) + (String)_userHash.get("recp") +  "\nmsg: " + alertMsg.toString(), null);
	}
	
	
}
