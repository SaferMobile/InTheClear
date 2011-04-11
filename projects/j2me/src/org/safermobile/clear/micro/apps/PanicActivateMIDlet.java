package org.safermobile.clear.micro.apps;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;

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
import javax.microedition.rms.RecordStoreException;

import org.safermobile.clear.micro.data.PhoneInfo;
import org.safermobile.clear.micro.sms.SMSServer;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.ui.Splash;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;
import org.safermobile.micro.utils.StringTokenizer;


public class PanicActivateMIDlet extends MIDlet implements CommandListener, Runnable {

	private DisplayManager _manager;
	private Display _display;

	private Displayable _screenMain;
	private TextBox _tbMain;
	
	private Command	 _cmdBack;
	private Command	 _cmdPanic;
	private Command	 _cmdCancel;
	
	
	private SMSServer _smsServer;
	private int SMS_SERVER_PORT = 0;
	
	private final static String TAG = "Panic";
	private final static String TITLE_MAIN = "Panic!";
	private final static int DEFAULT_RESEND_TIMEOUT = 60000;
	private final static String PANIC_PREFS_DB = "panicprefs";
	
	private Thread _thread;
	private boolean _keepPanicing = false;
	
	private Preferences _prefs = null;
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public PanicActivateMIDlet() {
		
		_display = Display.getDisplay(this);
		_manager = new DisplayManager(_display);
		
		_cmdBack = new Command("Back", Command.BACK, 2);
		_cmdPanic = new Command("Panic!", Command.SCREEN, 1);
		_cmdCancel = new Command("Cancel", Command.CANCEL,3);
		
		_screenMain = getScreenMain();
		_screenMain.setCommandListener(this);
		
		_screenMain.addCommand(_cmdPanic);
		_screenMain.addCommand(_cmdCancel);

		
	}

	private void startPanic ()
	{
		try {
			_prefs = new Preferences (PANIC_PREFS_DB);
		
			startSmsServer();
			
			_thread = new Thread(this);
			_thread.start();
		
		} catch (RecordStoreException e) {
			
			Logger.error(TAG, "error access preferences", e);
			showAlert("Error","Unable to open preferences",null);
		}
	}
	
	private void stopPanic ()
	{

		_keepPanicing = false;
		_thread.interrupt();
	}

	private Displayable getScreenMain() {
		_tbMain = new TextBox("Panic! Activated", "", 100, TextField.ANY);
		
		return _tbMain;
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		_manager.next(_screenMain);
		
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
		
		if (command == _cmdPanic) {
			startPanic();

		}
		else if (command == _cmdCancel) {
			
			stopPanic ();
		}
		else if (command == _cmdBack) {
			_manager.back();
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
			
				showAlert("error","Unable to initiate SMS access",_screenMain);
				Logger.error(TAG, "error starting sms server", e);
			}
		}
		
	}
	
	public void run ()
	{
		_keepPanicing = true;
		
		String recipients = _prefs.get("user.recp");
		String userName =  _prefs.get("user.name");
		String userMessage = _prefs.get("user.msg");
		String userLocation = _prefs.get("user.loc");
		
		String panicMsg = buildPanicMessage(userName, userMessage, userLocation);
		
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
		
		sbPanicMsg.append("Panic! from ");
		sbPanicMsg.append(userName);
		sbPanicMsg.append(": ");
		sbPanicMsg.append(userMessage);
		sbPanicMsg.append(".");
		sbPanicMsg.append(" Location:");
		sbPanicMsg.append(userLocation);

		
		//append loc info
		String cid = PhoneInfo.getCellId();
		if (cid != null)
		{
			sbPanicMsg.append(" cid:");
			sbPanicMsg.append(cid);
		}
		
		String lac = PhoneInfo.getLAC();
		if (lac != null)
		{
			sbPanicMsg.append(" lac:");
			sbPanicMsg.append(lac);
		}
		
		//append timestamp
		sbPanicMsg.append(" ts:");
		sbPanicMsg.append(new Date().toString());
	
		return sbPanicMsg.toString();
	}
	
	private void sendSMSPanic (String recipients, String alertMsg)
	{
	
		StringTokenizer st = new StringTokenizer(recipients,",");
		
		while (st.hasMoreTokens())
		{
			String recp = st.nextToken().trim();
			_smsServer.sendSMSAlert(recp, alertMsg);
			
		}
		
		//showAlert("Panic Sent", "Sent to: " + (String)_userHash.get("recp") +  "\nmsg: " + alertMsg.toString(), null);
	}
	
	
}
