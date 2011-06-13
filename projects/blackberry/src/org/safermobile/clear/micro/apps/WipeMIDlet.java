/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */
package org.safermobile.clear.micro.apps;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.j4me.ui.UIManager;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.screens.WipeManualForm;
import org.safermobile.clear.micro.ui.ClearTheme;
import org.safermobile.micro.ui.DisplayManager;

public class WipeMIDlet extends MIDlet
{

	L10nResources l10n = L10nResources.getL10nResources("en-US");
	
	private WipeManualForm _form;
	/**
	 * Creates several screens and navigates between them.
	 */
	public WipeMIDlet() 	
	{
		
		UIManager.init(this);
		 UIManager.setTheme( new ClearTheme()  );

		 _form = new WipeManualForm(this);
		
	}

	
	/*
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
	}*/
	

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		_form.show();
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
