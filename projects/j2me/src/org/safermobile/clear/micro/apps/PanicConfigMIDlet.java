
/* Copyright (c) 2011, SaferMobile/MobileActive - https://safermobile.org */
/* See LICENSE for licensing information */

package org.safermobile.clear.micro.apps;
import java.util.Vector;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import org.j4me.ui.DeviceScreen;
import org.j4me.ui.UIManager;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.models.WipeDataType;
import org.safermobile.clear.micro.apps.views.OneTouchPanicForm;
import org.safermobile.clear.micro.apps.views.PanicWizardForm;
import org.safermobile.clear.micro.apps.views.SMSSendTestForm;
import org.safermobile.clear.micro.apps.views.SetupAlertMessageForm;
import org.safermobile.clear.micro.apps.views.SetupCompleteForm;
import org.safermobile.clear.micro.apps.views.WipeSelectionForm;
import org.safermobile.clear.micro.ui.ClearTheme;
import org.safermobile.clear.micro.ui.ErrorAlert;
import org.safermobile.micro.ui.Splash;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;


//release.build = false
public class PanicConfigMIDlet extends MIDlet implements Runnable {

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
	public PanicConfigMIDlet() {
	
		try
		{
		 _prefs = new Preferences (PanicConstants.PANIC_PREFS_DB);
		} catch (RecordStoreException e) {
			
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
		}
		
		setupWipeDataTypes();
		
		
		setupUI();
		showSplash();
	}
	
	private void setupWipeDataTypes ()
	{

		_wipeDataTypes = new Vector();
		WipeDataType wdt = null;
		
		wdt = new WipeDataType(PanicConstants.PREFS_KEY_WIPE_CONTACTS, l10n.getString(L10nConstants.keys.WIPE_MENU_CONTACTS));
		_wipeDataTypes.addElement(wdt);
		
		wdt = new WipeDataType(PanicConstants.PREFS_KEY_WIPE_PHOTOS, l10n.getString(L10nConstants.keys.WIPE_MENU_PHOTOS));
		_wipeDataTypes.addElement(wdt);
		

		//for BB this is where we will add CallLogs and Messages
		/*
		 * 
		 * wdt = new WipeDataType(PanicConstants.PREFS_KEY_WIPE_MESSAGES, l10n.getString(L10nConstants.keys.WIPE_MENU_MESSAGES));
		_wipeDataTypes.addElement(wdt);
		
		
		wdt = new WipeDataType(PanicConstants.PREFS_KEY_WIPE_CALLLOGS, l10n.getString(L10nConstants.keys.WIPE_MENU_CALLLOGS));
		_wipeDataTypes.addElement(wdt);
		
		 * 
		 * 
		 */
		
		//
		
		wdt = new WipeDataType(PanicConstants.PREFS_KEY_WIPE_ALL_FILES, l10n.getString(L10nConstants.keys.WIPE_MENU_FILES));
		_wipeDataTypes.addElement(wdt);
		
		wdt = new WipeDataType(PanicConstants.PREFS_KEY_WIPE_EVENTS, l10n.getString(L10nConstants.keys.WIPE_MENU_CALENDAR));
		_wipeDataTypes.addElement(wdt);
		
		
	}
	
	private void setupUI ()
	{
		UIManager.init(this);
		 UIManager.setTheme( new ClearTheme()  );

		 
		 _screens = new DeviceScreen[6];
		 _screens[0] = new PanicWizardForm (this);
		 _screens[1] = new SMSSendTestForm (this);
		 _screens[2] = new SetupAlertMessageForm (this);
		 _screens[3] = new WipeSelectionForm (this, _wipeDataTypes);
		 _screens[4] = new OneTouchPanicForm (this);
		 _screens[5] = new SetupCompleteForm (this);
		 
		 
	}
		
	public void showNext ()
	{
		_screenIdx++;
		showScreen();
	}
	
	public void showPrev ()
	{
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
		_screens[0].show();	}
	
	
	

	public void showSplash ()
	{

		_display = Display.getDisplay(this);
		
		_splash = new Splash("/splash160.png","/splash320.png",0xffffff);
		
		_splash.show(_display, _screens[0].getCanvas(), 2000);
		
	}
	

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		
	}

	
	
	public void savePref (String key, String value)
	{
		try {

			Logger.debug(PanicConstants.TAG, "saving " + key + "='" + value + "' to: " + PanicConstants.PANIC_PREFS_DB);
			
			_prefs.put(key, value);
			
			_prefs.save();
			
			
		} catch (RecordStoreException e) {
			
			showAlert("Error!","We couldn't save your settings!",0);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
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
				
				Logger.debug(PanicConstants.TAG, "saving " + key + "='" + value + "' to: " + PanicConstants.PANIC_PREFS_DB);
			
				_prefs.put(key, value);
			}
			
			_prefs.save();
			
			
		} catch (RecordStoreException e) {
			
			showAlert("Error!","We couldn't save your settings!",0);
			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
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

	
	
}
