/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.apps;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.j4me.ui.UIManager;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.screens.ShoutManualForm;
import org.safermobile.clear.micro.ui.ClearTheme;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

// hasLocationCapability=false
//release.build = false
public class ShoutMIDlet extends MIDlet implements CommandListener {

	
	private DisplayManager _manager;
	private Display _display;

	private TextBox _tbMain;
	private LargeStringCanvas _lsCanvas;
	private ShoutManualForm _form;
	
	/*
	 * the thread which manages the panic sending
	 */
	private Thread _thread;
	
	/*
	 * stores the user data between the config app and this one
	 */
	private Preferences _prefs = null; 
	
	/*
	 * localized resources
	 */
	L10nResources l10n = L10nResources.getL10nResources("en-US");
	
	/**
	 * Creates Panic Activate app
	 */
	public ShoutMIDlet() {
		
		_display = Display.getDisplay(this);
		
		UIManager.init(this);
		 UIManager.setTheme( new ClearTheme()  );

		 _form = new ShoutManualForm(this);
	}

	

	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		

		_form.show();
	
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
