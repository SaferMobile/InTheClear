package org.safermobile.intheclear;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

import org.safermobile.micro.ui.Splash;

public class InTheClear extends MIDlet implements CommandListener {
	private Display _display;
	private Splash _splash;
	
	private Form _form;
	
	long splashDelay = 3000L;
	
	protected void startApp() throws MIDletStateChangeException {
		_display = Display.getDisplay(this);
		_splash = new Splash("/res/logo.gif",0xffffff);
		
		_form = new Form("In The Clear Preferences");
		
		_splash.show(_display, _form, splashDelay);
		
	}
	
	public void commandAction(Command arg0, Displayable arg1) {
		// TODO Auto-generated method stub
		
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}
	
}