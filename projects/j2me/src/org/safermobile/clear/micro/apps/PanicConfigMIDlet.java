
/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.apps;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.Menu;
import org.j4me.ui.MenuItem;
import org.j4me.ui.UIManager;
import org.safermobile.clear.micro.apps.screens.LocationPermissionForm;
import org.safermobile.clear.micro.apps.screens.PanicWizardForm;
import org.safermobile.clear.micro.apps.screens.SMSSendTestForm;
import org.safermobile.clear.micro.apps.screens.SetupAlertMessageForm;
import org.safermobile.clear.micro.apps.screens.UserInfoForm;
import org.safermobile.clear.micro.ui.ClearTheme;
import org.safermobile.micro.ui.Splash;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;


//release.build = false
public class PanicConfigMIDlet extends MIDlet implements Runnable {

	private Display _display;
	private Splash _splash;
	
	private Form _form;
	private UserInfoForm _formUserInfo;
		
	private Preferences _prefs;
	
	private PanicWizardForm _currentForm;
	
	private int formIdx = 1;
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public PanicConfigMIDlet() {
	
		try
		{
		 _prefs = new Preferences (PanicConstants.PANIC_PREFS_DB);
		} catch (RecordStoreException e) {
			
			showAlert("Boo!","We couldn't access your settings!",_form);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
		}
		
		setupUI();
		showSplash();
	}
	
	private void setupUI ()
	{
		UIManager.init(this);
		 UIManager.setTheme( new ClearTheme()  );
		 
		 _formUserInfo = new UserInfoForm(this);

		 _currentForm = new PanicWizardForm (this);	     
	}
		
	public void showNext ()
	{
		formIdx++;
		
		if (formIdx == 1)
			_currentForm.show();
		else if (formIdx == 2)
			_formUserInfo.show();
			
	}
	
	  public void showShoutConfigMenu ()
      {
  		
  		// The theme is the default represented <code>Theme</code> class.
  		// To change it, create a new <code>Theme</code>-derived object and call
  		// <code>UIManager.setTheme</code>.
  		
  		// The first screen is a menu to choose among the example screens.
  		Menu menu = new Menu( "Panic! Shout", null );
  		

  		// Attach an exit option.
  		menu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "Check SMS Permissions";
  					}

  					public void onSelection ()
  					{
  						SMSSendTestForm form = new SMSSendTestForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );
  		
  	// Attach an exit option.
  		menu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "Check Location";
  					}

  					public void onSelection ()
  					{
  						LocationPermissionForm form = new LocationPermissionForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );
  		
  	// Attach an exit option.
  		menu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "Setup Alert Message";
  					}

  					public void onSelection ()
  					{
  						SetupAlertMessageForm form = new SetupAlertMessageForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );
  		
  		// Show the menu.
  		menu.show();

      }
	

	public void showSplash ()
	{

		_display = Display.getDisplay(this);
		
		_splash = new Splash("/logo.gif",0xffffff);
		
		
		_splash.show(_display, _currentForm.getCanvas(), 3000);
		
	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		
		_splash.show(_display, _form, 3000);
		
		
	}

	/*
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
	*/
	
	private void savePrefs ()
	{

		try {

			Logger.debug(PanicConstants.TAG, "saving preferences to: " + PanicConstants.PANIC_PREFS_DB);
			
			/*
			_prefs.put(PanicConstants.PREFS_KEY_RECIPIENT, _tfRecp.getString());
			_prefs.put(PanicConstants.PREFS_KEY_NAME, _tfName.getString());
			_prefs.put(PanicConstants.PREFS_KEY_MESSAGE, _tfMsg.getString());
			_prefs.put(PanicConstants.PREFS_KEY_LOCATION, _tfLoc.getString());
			*/
			
			_prefs.save();
			
			showAlert("Yay!","Your settings have been updated",_form);
			
		} catch (RecordStoreException e) {
			
			showAlert("Boo!","We couldn't save you settings!",_form);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
		}
	}

	
	
	public void showAlert (String title, String msg, Displayable next)
	{
		Alert alert = new Alert(title);
		alert.setString(msg);
		alert.setTimeout(5000);
		
		_display.setCurrent(alert, _form);
	}
	


	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}

	public void run() {
		// TODO Auto-generated method stub
		
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	
	
}
