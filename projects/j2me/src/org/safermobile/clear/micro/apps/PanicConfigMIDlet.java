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

import org.safermobile.clear.micro.data.PhoneInfo;
import org.safermobile.clear.micro.sms.SMSServer;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.StringTokenizer;


public class PanicConfigMIDlet extends MIDlet implements CommandListener, Runnable {

	private DisplayManager manager;
	private Displayable _screenMain;
	private Displayable _screenSettings;
	private Displayable _screenAbout;
	
	private Command	 _cmdBack;
	private Command	 _cmdPanic;
	private Command	 _cmdSettings;
	private Command	 _cmdAbout;
	
	
	private Hashtable _userHash;
	private SMSServer smsServer;
	private int SMS_SERVER_PORT = 0;
	
	private final static String TAG = "Panic";
	
	private final static String TITLE_MAIN = "Panic!";
	private final static String TITLE_SETTINGS = "Settings";
	private final static String TITLE_ABOUT = "About";
	
	private Thread thread;
	private boolean keepPanicing = false;
	/**
	 * Creates several screens and navigates between them.
	 */
	public PanicConfigMIDlet() {
		

		initUserData();
		
		this.manager = new DisplayManager(Display.getDisplay(this));
		
		_cmdBack = new Command("Back", Command.BACK, 1);
		_cmdPanic = new Command("Panic!", Command.ITEM, 1);
		_cmdSettings = new Command("Settings", Command.OK, 1);
		_cmdAbout = new Command("About", Command.HELP, 1);
		
		
		_screenMain = getScreenMain();
		_screenMain.setCommandListener(this);
		
		_screenMain.addCommand(_cmdPanic);
		
		
	}

	private Displayable getScreenMain() {
		List list = new List(TITLE_MAIN, List.IMPLICIT);
		list.append("Your Name: " + _userHash.get("name"), null);
		list.append("Alert Msg: " + _userHash.get("msg"), null);
		list.append("Location: " + _userHash.get("loc"), null);
		list.append("Send To: " + _userHash.get("recp"), null);
		
		return list;
	}

	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		this.manager.next(_screenMain);
		

		startSmsServer();
	}
	
	public void showAlert (String title, String msg, Displayable next)
	{
		Alert alert = new Alert(title);
		alert.setString(msg);
        manager.next(alert, next);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		
		if (command == _cmdPanic) {
			thread = new Thread(this);
			thread.start();
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

	private void initUserData ()
	{
		_userHash = new Hashtable();
		_userHash.put("name", "Freda Fayter");
		_userHash.put("msg", "The police must have nabbed me");
		_userHash.put("loc", "SomewhereVille");
		_userHash.put("recp", "+17185697272,+14136879877");
		
		//load values from db
	}
	
	private void startSmsServer ()
	{
		if (smsServer == null)
		{
			smsServer = new SMSServer (SMS_SERVER_PORT);
			try {
				smsServer.start();
			} catch (IOException e) {
			
				
				Logger.error(TAG, "error starting sms server", e);
			}
		}
		
	}
	
	public void run ()
	{
		
	}
	
	
	
}
