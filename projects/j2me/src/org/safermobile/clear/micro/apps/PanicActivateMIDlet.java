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
	
	private SMSServer _smsServer;
	private int SMS_SERVER_PORT = 0;
	
	
	/*//let's keep GPS out for now
	 * 	private boolean _useGPS = true;

	// #if hasLocationCapability
	private MovementTracker _mTracker;
	private float MOVEMENT_CHANGE = 0.1f;
	private Location _lastLocation;
	// #endif
	 
	 */
	
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
	
	/*
	 * localized resources
	 */
	L10nResources l10n = L10nResources.getL10nResources(null);
	
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

		
		/*
		if (_useGPS)
		{
			// #if hasLocationCapability
			try {
				_mTracker = new MovementTracker(MOVEMENT_CHANGE, new LocationListener ()
				{
					 public void locationUpdated(LocationProvider provider, Location location) {
						 _lastLocation = location;
						 
					    }

					    public void providerStateChanged(LocationProvider provider, int newState) {
					    }
				});
				
				
				
			} catch (LocationException e) {
				Logger.error(PanicConstants.TAG, "unable to get location: " + e.getMessage(), e);
			}
			// #endif
		}*/
		
	}

	
	private void insertTestData ()
	{
		_prefs.put(PanicConstants.PREFS_KEY_RECIPIENT,"2125551212");
		 _prefs.put(PanicConstants.PREFS_KEY_NAME,"foo");
		 _prefs.put(PanicConstants.PREFS_KEY_MESSAGE,"what");
		 _prefs.put(PanicConstants.PREFS_KEY_LOCATION,"where am i");
	}
	
	private void startPanic ()
	{
		try {

			_prefs = new Preferences (PanicConstants.PANIC_PREFS_DB);
			insertTestData();
			
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
				
				startSmsServer();
				
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
	
	
	private void startSmsServer ()
	{
		if (_smsServer == null)
		{
			try {
				
				_smsServer = SMSServer.getInstance(SMS_SERVER_PORT);
			
			} catch (IOException e) {
			
				showAlert("error",l10n.getString(L10nConstants.keys.KEY_PANIC_ERROR_SMS),_tbMain);
				Logger.error(PanicConstants.TAG, "error starting sms server", e);
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
		
		Logger.debug(PanicConstants.TAG, "starting panic run(); loading prefs...");
		
		String recipients = _prefs.get(PanicConstants.PREFS_KEY_RECIPIENT);
		String userName =  _prefs.get(PanicConstants.PREFS_KEY_NAME);
		String userMessage = _prefs.get(PanicConstants.PREFS_KEY_MESSAGE);
		String userLocation = _prefs.get(PanicConstants.PREFS_KEY_LOCATION);
		
		String panicMsg = buildPanicMessage(userName, userMessage, userLocation);
		String panicData = buildPanicData (userName);
		
		showMessage ("PANIC MESSAGE: " + panicMsg + "\n\npreparing to send...");
		
		doSecPause (5);
		
		_lsCanvas = new LargeStringCanvas("");
		_lsCanvas.setCommandListener(this);
		_lsCanvas.addCommand(_cmdCancel);
		
		_manager.next(_lsCanvas);
		
		_lsCanvas.setLargeString("Sending in 5...");
		doSecPause (1);
		_lsCanvas.setLargeString("4");
		doSecPause (1);
		_lsCanvas.setLargeString("3");
		doSecPause (1);
		_lsCanvas.setLargeString("2");
		doSecPause (1);
		_lsCanvas.setLargeString("1");
		doSecPause (1);
		
		int resendTimeout = PanicConstants.DEFAULT_RESEND_TIMEOUT; //one minute
		
		
		while (_keepPanicing)
		{
			sendSMSPanic (recipients, panicMsg, panicData);
			
			int secs = resendTimeout/1000;
			
			while (secs > 0)
			{
				showMessage("Panic! again in " + secs + "secs...");
				doSecPause (1);
				secs--;
			}
			
			//update message with new mobile cid, lac info
			panicMsg = buildPanicMessage(userName, userMessage, userLocation);
			
		}
		

		_manager.next(_tbMain);
	}
	
	private String buildPanicMessage (String userName, String userMessage, String userLocation)
	{
		
		
		StringBuffer sbPanicMsg = new StringBuffer();
		
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_FROM));
		sbPanicMsg.append(' ');
		sbPanicMsg.append(userName);
		sbPanicMsg.append(':');
		sbPanicMsg.append(' ');
		sbPanicMsg.append(userMessage);
		
		sbPanicMsg.append(" #");
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_LOCATION));
		sbPanicMsg.append(userLocation);
		
		
		//append timestamp
		sbPanicMsg.append(" #");
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_TIMESTAMP));
		sbPanicMsg.append(new Date().toString());
	
		return sbPanicMsg.toString();
	}
	
	private String buildPanicData (String userName)
	{
		
		
		StringBuffer sbPanicMsg = new StringBuffer();
		
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_FROM));
		sbPanicMsg.append(' ');
		sbPanicMsg.append(userName);
		sbPanicMsg.append(':');
		
		
		String IMEI = PhoneInfo.getIMEI();
		if (IMEI != null)
		{

			sbPanicMsg.append(" #");
			sbPanicMsg.append("IMEI:");
			sbPanicMsg.append(IMEI);

		}
		
		String IMSI = PhoneInfo.getIMSI();
		if (IMSI != null)
		{
			sbPanicMsg.append(" #");
			sbPanicMsg.append("IMSI:");
			sbPanicMsg.append(IMSI);
		}
		
		//append loc info
		String cid = PhoneInfo.getCellId();
		if (cid != null)
		{
			sbPanicMsg.append(" #");
			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_CID));
			sbPanicMsg.append(cid);
		}
		
		
		String lac = PhoneInfo.getLAC();
		if (lac != null)
		{
			sbPanicMsg.append(" #");
			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_LAC));
			sbPanicMsg.append(lac);
		}
		
		
		/*
		if (_useGPS)
		{
			// #if hasLocationCapability
			
			if (_lastLocation != null)
			{	
				sbPanicMsg.append(" GPS:");
				sbPanicMsg.append(_lastLocation.getQualifiedCoordinates().getLatitude());
				sbPanicMsg.append(",");
				sbPanicMsg.append(_lastLocation.getQualifiedCoordinates().getLongitude());

				sbPanicMsg.append(' ');
			}
			// #endif
		}*/
		
		//append timestamp
		sbPanicMsg.append(" #");
		sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_TIMESTAMP));
		sbPanicMsg.append(new Date().toString());
	
		return sbPanicMsg.toString();
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
	
	private void sendSMSPanic (String recipients, String panicMsg, String panicData)
	{
		
		StringTokenizer st = new StringTokenizer(recipients,",");
		
		while (st.hasMoreTokens())
		{
			String recp = st.nextToken().trim();
			
			showMessage ("Sending to " + recp + "...");
			
			_smsServer.sendSMSAlert(recp, panicMsg);
			doSecPause (1);
			_smsServer.sendSMSAlert(recp, panicData);
			
			showMessage ("Panic Sent!");
			
			doSecPause (2);
			
		}
		
		//showAlert(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_SENT), l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_SENTTO) + (String)_userHash.get("recp") +  "\nmsg: " + alertMsg.toString(), null);
	}
	
	
}
