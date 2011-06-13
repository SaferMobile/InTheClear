
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
import org.safermobile.clear.micro.apps.screens.WipePermissionForm;
import org.safermobile.clear.micro.apps.screens.WipeSelectionForm;
import org.safermobile.clear.micro.ui.ClearTheme;
import org.safermobile.clear.micro.ui.ErrorAlert;
import org.safermobile.micro.ui.Splash;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;


//release.build = false
public class PanicConfigMIDlet extends MIDlet implements Runnable {

	private Display _display;
	private Splash _splash;
	
	
	private PanicWizardForm _startForm;	
	private UserInfoForm _formUserInfo;
		
	private Preferences _prefs;
	private Menu _shoutMenu;
	
	private int formIdx = 1;
	
	/**
	 * Creates several screens and navigates between them.
	 */
	public PanicConfigMIDlet() {
	
		try
		{
		 _prefs = new Preferences (PanicConstants.PANIC_PREFS_DB);
		} catch (RecordStoreException e) {
			
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

		 _startForm = new PanicWizardForm (this);	     
	}
		
	public void showNext ()
	{
		formIdx++;
		
		if (formIdx == 1)
			_startForm.show();
		else if (formIdx == 2)
			_formUserInfo.show();
			
	}
	
	public void showStartScreen ()
	{
		_startForm.show();
	}
	
	 public Menu getShoutConfigMenu ()
     {

		  if (_shoutMenu == null)
			  createShoutConfigMenu();
		  
		 return _shoutMenu;
		 
     }
	 
	  public void showShoutConfigMenu ()
      {
		  if (_shoutMenu == null)
			  createShoutConfigMenu();
		  
		  _shoutMenu.show();
      }
	  
	  private void createShoutConfigMenu ()
	  {
  		
  		// The first screen is a menu to choose among the example screens.
		  _shoutMenu = new Menu( "Panic! Shout", null );
  		

  		// Attach an exit option.
		  _shoutMenu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "1) Check SMS Permissions";
  					}

  					public void onSelection ()
  					{
  						SMSSendTestForm form = new SMSSendTestForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );
  		
  	// Attach an exit option.
		  _shoutMenu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "2) Check Location Access";
  					}

  					public void onSelection ()
  					{
  						LocationPermissionForm form = new LocationPermissionForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );
  		
  	// Attach an exit option.
		  _shoutMenu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "3) Setup Alert Message";
  					}

  					public void onSelection ()
  					{
  						SetupAlertMessageForm form = new SetupAlertMessageForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );
		  
		// Attach an exit option.
		  _shoutMenu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "4) Enable Wipe! Permission";
  					}

  					public void onSelection ()
  					{
  						WipePermissionForm form = new WipePermissionForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );

			// Attach an exit option.
		  _shoutMenu.appendMenuOption( new MenuItem()
  				{
  					public String getText ()
  					{
  						return "5) Configure Wipe! Options";
  					}

  					public void onSelection ()
  					{
  						WipeSelectionForm form = new WipeSelectionForm(PanicConfigMIDlet.this);
  						form.show();
  					}
  				} );

		  
  		// Show the menu.
		  _shoutMenu.show();

      }
	

	public void showSplash ()
	{

		_display = Display.getDisplay(this);
		
		_splash = new Splash("/logo.gif",0xffffff);
		
		
		_splash.show(_display, _startForm.getCanvas(), 2000);
		
	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		
		//_splash.show(_display, _form, 3000);
		
		
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
	
	public void savePref (String key, String value)
	{
		try {

			Logger.debug(PanicConstants.TAG, "saving " + key + "='" + value + "' to: " + PanicConstants.PANIC_PREFS_DB);
			
			_prefs.put(key, value);
			
			_prefs.save();
			
			
		} catch (RecordStoreException e) {
			
			showAlert("Error!","We couldn't save you settings!",_startForm);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
		}
	}
	
	public void savePrefs (String[] keys, String[] values)
	{
		try {

			String key, value;
			
			for (int i = 0; i < keys.length; i++)
			{
				key = keys[i];
				value = values[i];
				
				Logger.debug(PanicConstants.TAG, "saving " + key + "='" + value + "' to: " + PanicConstants.PANIC_PREFS_DB);
			
				_prefs.put(key, value);
			}
			
			_prefs.save();
			
			
		} catch (RecordStoreException e) {
			
			showAlert("Error!","We couldn't save you settings!",_startForm);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
		}
	}
	
	
	
	public void showAlert (String title, String msg, DeviceScreen next)
	{
		/*
		Alert alert = new Alert(title);
		alert.setString(msg);
		//alert.setTimeout(5000);		
		_display.setCurrent(alert, next);
		*/
		ErrorAlert eAlert = new ErrorAlert (title, msg, null, next);
		eAlert.show();
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
