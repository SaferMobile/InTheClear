
/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

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
import javax.microedition.lcdui.StringItem;
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


//release.build = false
public class PanicConfigMIDlet extends MIDlet implements CommandListener, Runnable {

	private Display _display;
	private Splash _splash;
	
	private Form _form;
	private TextField _tfRecp, _tfName, _tfLoc, _tfMsg;
	
	private Command	 _cmdSave;
	private Command	 _cmdExit;
	
	private SMSServer smsServer;
	private int SMS_SERVER_PORT = 0;
	
	private final static String TITLE_MAIN = "Panic! Config";

	private Preferences _prefs;
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public PanicConfigMIDlet() {
		

		_display = Display.getDisplay(this);
		
		_splash = new Splash("/logo.gif",0xffffff);
		
		_cmdSave = new Command("Save", Command.SCREEN, 1);
		_cmdExit = new Command("Exit", Command.EXIT, 1);
		
		try
		{
		 _prefs = new Preferences (PanicConstants.PANIC_PREFS_DB);
		} catch (RecordStoreException e) {
			
			showAlert("Boo!","We couldn't access your settings!",_form);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
		}

		setupForm();
		fillForm ();
	}

	private void setupForm() {
		
		_form = new Form(TITLE_MAIN);
		
		_form.append(new StringItem(null,"This form is used to configure the Panic! button app"));

		_form.append(new StringItem(null,"Enter the mobile phone number(s), separated by comma, of who you want to alert via SMS"));

		_tfRecp = new TextField("Mobile number(s)","",50,TextField.ANY);
		_form.append(_tfRecp);
		
		_tfName = new TextField("What is your name?","",50,TextField.ANY);
		_form.append(_tfName);
		
		 _tfLoc = new TextField("Where are you now?","",50,TextField.ANY);
		_form.append(_tfLoc);
		
		 _tfMsg = new TextField("What is your PANIC! message?","",100,TextField.ANY);
		_form.append(_tfMsg);
		
		_form.append(new StringItem(null,"Press the 'save' button when you are done"));

		_form.setCommandListener(this);
		
		_form.addCommand(_cmdSave);
		_form.addCommand(_cmdExit);
		
	}
	
	private void fillForm ()
	{

		String pref = _prefs.get("user.recp");
		if (pref != null)
			_tfRecp.setString(pref);
		
		pref = _prefs.get("user.name");
		if (pref != null)
			_tfName.setString(pref);
		
		pref = _prefs.get("user.msg");
		if (pref != null)
			_tfMsg.setString(pref);
		
		pref = _prefs.get("user.loc");
		if (pref != null)
			_tfLoc.setString(pref);
	}
	
	private void savePrefs ()
	{

		try {

			Logger.debug(PanicConstants.TAG, "saving preferences to: " + PanicConstants.PANIC_PREFS_DB);
			
			_prefs.put(PanicConstants.PREFS_KEY_RECIPIENT, _tfRecp.getString());
			_prefs.put(PanicConstants.PREFS_KEY_NAME, _tfName.getString());
			_prefs.put(PanicConstants.PREFS_KEY_MESSAGE, _tfMsg.getString());
			_prefs.put(PanicConstants.PREFS_KEY_LOCATION, _tfLoc.getString());
			
			_prefs.save();
			
			showAlert("Yay!","Your settings have been updated",_form);
			
		} catch (RecordStoreException e) {
			
			showAlert("Boo!","We couldn't save you settings!",_form);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		
	//	_manager.next(_form);
		_splash.show(_display, _form, 3000);
		
	}
	
	public void showAlert (String title, String msg, Displayable next)
	{
		Alert alert = new Alert(title);
		alert.setString(msg);
		alert.setTimeout(5000);
		
		_display.setCurrent(alert, _form);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
	
		if (command == _cmdSave) 
		{
			savePrefs();
		}
		else if (command == _cmdExit)
		{
			try {
				destroyApp(false);
			} catch (MIDletStateChangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    notifyDestroyed();
			
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

	
	
	public void run ()
	{
		
	}
	
	
	
}
