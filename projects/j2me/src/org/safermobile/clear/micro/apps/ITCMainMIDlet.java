
/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.apps;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.UIManager;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.models.WipeDataType;
import org.safermobile.clear.micro.apps.views.ITCMainForm;
import org.safermobile.clear.micro.ui.ErrorAlert;
import org.safermobile.clear.micro.ui.InTheClearTheme;
import org.safermobile.micro.ui.Splash;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;


//release.build = false
public class ITCMainMIDlet extends MIDlet implements Runnable {

	private Display _display;
	private Splash _splash;

	
	private DeviceScreen[] _screens;
	
	private Preferences _prefs;
	
	private Vector _wipeDataTypes;
	
	private int _screenIdx = 0;
	
	private L10nResources l10n = LocaleManager.getResources();

	/**
	 * Creates several screens and navigates between them.
	 */
	public ITCMainMIDlet() {
	
		try
		{
		 _prefs = new Preferences (ITCConstants.PANIC_PREFS_DB);
		} catch (RecordStoreException e) {
			
			Logger.error(ITCConstants.TAG, "a problem saving the prefs: " + e, e);
		}
		
		setupWipeDataTypes();
		

		UIManager.init(this);
		_display = Display.getDisplay(this);
		setupUI();
		
		String recips = _prefs.get(ITCConstants.PREFS_KEY_RECIPIENT);
		if (recips == null || recips.length() == 0)
		{
		
			_splash = new Splash("/splash160.png","/splash320.png",0xffffff);			
			_splash.show(_display, _screens[0].getCanvas(), 2000);
			
		}
		else
		{
			showMainForm();
			
				
		}
	}
	
	public Display getCurrentDisplay ()
	{
		return _display;
	}
	
	private void setupWipeDataTypes ()
	{

		_wipeDataTypes = new Vector();
		WipeDataType wdt = null;
		
		wdt = new WipeDataType(ITCConstants.PREFS_KEY_WIPE_CONTACTS, l10n.getString(L10nConstants.keys.WIPE_MENU_CONTACTS));
		_wipeDataTypes.addElement(wdt);
		
		wdt = new WipeDataType(ITCConstants.PREFS_KEY_WIPE_PHOTOS, l10n.getString(L10nConstants.keys.WIPE_MENU_PHOTOS));
		_wipeDataTypes.addElement(wdt);
		
		wdt = new WipeDataType(ITCConstants.PREFS_KEY_WIPE_ALL_FILES, l10n.getString(L10nConstants.keys.WIPE_MENU_FILES));
		_wipeDataTypes.addElement(wdt);
		
		wdt = new WipeDataType(ITCConstants.PREFS_KEY_WIPE_EVENTS, l10n.getString(L10nConstants.keys.WIPE_MENU_CALENDAR));
		_wipeDataTypes.addElement(wdt);
		
		
	}

	
	public void showMainForm ()
	{
	
		 ITCMainForm formMain =	new ITCMainForm (this); 


		 formMain.show();
		 
	}
	
	private void setupUI ()
	{
		 UIManager.setTheme( new InTheClearTheme()  );
		 
		 _screens = new DeviceScreen[6];
		 _screens[0] = new org.safermobile.clear.micro.apps.views.WizardStartForm (this);
		 _screens[1] = new org.safermobile.clear.micro.apps.views.SMSSendTestForm (this);
		 _screens[2] = new org.safermobile.clear.micro.apps.views.SetupAlertMessageForm (this);
		 _screens[3] = new org.safermobile.clear.micro.apps.views.WipeSelectionForm (this, _wipeDataTypes);
		 _screens[4] = new org.safermobile.clear.micro.apps.views.EnableOneTouchPanicForm (this);
		 _screens[5] = new org.safermobile.clear.micro.apps.views.SetupCompleteForm (this);
		 
		
	}
		
	public void showNext ()
	{
		_screenIdx++;
		showScreen();
	}
	
	public void showPrev ()
	{
		if (_screenIdx > 0)
			_screenIdx--;
		
		showScreen();
	}
	
	private void showScreen ()
	{
		_screens[_screenIdx].show();
		
	}
	
	public int getNextScreenIdx()
	{
		return _screenIdx+1;
		
	}
	
	public int getCurrentScreenIdx()
	{
		return _screenIdx;
		
	}
	
	public void showStartScreen ()
	{
		_screenIdx = 0;
		_screens[_screenIdx].show();	
	}
	
	
	
	

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		
	}

	
	
	public void savePref (String key, String value)
	{
		try {

			Logger.debug(ITCConstants.TAG, "saving " + key + "='" + value + "' to: " + ITCConstants.PANIC_PREFS_DB);
			
			_prefs.put(key, value);
			
			_prefs.save();
			
			
		} catch (RecordStoreException e) {
			
			showAlert("Error!","We couldn't save your settings!",0);
			Logger.error(ITCConstants.TAG, "a problem saving the prefs: " + e, e);
		}
	}
	
	public String loadPref (String key)
	{
		return _prefs.get(key);
			
	}
	
	public void savePrefs (String[] keys, String[] values)
	{
		try {

			String key, value;
			
			for (int i = 0; i < keys.length; i++)
			{
				key = keys[i];
				value = values[i];
				
				Logger.debug(ITCConstants.TAG, "saving " + key + "='" + value + "' to: " + ITCConstants.PANIC_PREFS_DB);
			
				_prefs.put(key, value);
			}
			
			_prefs.save();
			
			
		} catch (RecordStoreException e) {
			
			showAlert("Error!","We couldn't save your settings!",0);
			Logger.error(ITCConstants.TAG, "a problem saving the prefs: " + e, e);
		}
	}
	
	
	
	public void showAlert (String title, String msg, int screenIdx)
	{
		
		DeviceScreen next = null;
		if (screenIdx != -1)
			next  = _screens[screenIdx];
		
		ErrorAlert eAlert = new ErrorAlert (title, msg, null, next);
		eAlert.show();
		_screenIdx = screenIdx;
	}
	


	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {}

	public void run() {
		
		
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		
		
	}

	
	class DisplaySizeCanvas extends Canvas {
		
	  int width;
	  int height;
	
	  public void paint(Graphics g) {
		  width = getWidth();
		  height = getHeight();
		  
		  
		  }
	}
	
}
