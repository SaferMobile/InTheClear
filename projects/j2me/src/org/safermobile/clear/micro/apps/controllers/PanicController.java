package org.safermobile.clear.micro.apps.controllers;

import java.util.Enumeration;

import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;

import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;

public class PanicController implements Runnable
{


	public final static int PANIC_COUNTDOWN_TIME = 3;
	
	/*
	 * stores the user data between the config app and this one
	 */
	private Preferences _prefs; 
	
	private CommandListener _cmdListener;
	private WipeListener _wipeListener;
	
	private DisplayManager _dManager;
	private LargeStringCanvas _lsCanvas;
	private Command _cmdCancel;
	
	/*
	 * used to cancel the panic loop
	 */
	private boolean _keepPanicing = false;

	private static L10nResources l10n = LocaleManager.getResources(); 
	
	public PanicController (Preferences prefs, WipeListener wipeListener, CommandListener cmdListener, DisplayManager dManager, Command cmdCancel)
	{
		_prefs = prefs;
		_wipeListener = wipeListener;
		_cmdListener = cmdListener;
		_dManager = dManager;
		_cmdCancel = cmdCancel;
		
	}
	
	public void stopPanic ()
	{
		_keepPanicing = false;
	}
	
	public void run ()
	{
		_keepPanicing = true;
		

		try
		{

			_lsCanvas = new LargeStringCanvas("");
			_lsCanvas.setCommandListener(_cmdListener);
			_lsCanvas.addCommand(_cmdCancel);
			
			_dManager.next(_lsCanvas);
				
			ShoutController sControl = new ShoutController();
			
			Logger.debug(ITCConstants.TAG, "starting panic run(); loading prefs...");
			
			String recipients = _prefs.get(ITCConstants.PREFS_KEY_RECIPIENT);
			String userName =  _prefs.get(ITCConstants.PREFS_KEY_NAME);
			String userMessage = _prefs.get(ITCConstants.PREFS_KEY_MESSAGE);
			String userLocation = _prefs.get(ITCConstants.PREFS_KEY_LOCATION);
			
			String panicMsg = sControl.buildShoutMessage(userName, userMessage, userLocation);
			String panicData = sControl.buildDataMessage (userName);
			
			_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_PANIC_START));
			
			doSecPause (PANIC_COUNTDOWN_TIME);
			
			if (!_keepPanicing)
				return;
			
			for (int i = PANIC_COUNTDOWN_TIME; i > 0; i--)
			{
				_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_SENDING) + ' ' + i + l10n.getString(L10nConstants.keys.KEY_ELLIPSE));			
				doSecPause (1);
			}
			
			int resendTimeout = ITCConstants.DEFAULT_RESEND_TIMEOUT; //one minute
			
			boolean wipeComplete = false;
			
			while (_keepPanicing)
			{
				try
				{
					_lsCanvas.setLargeString (l10n.getString(L10nConstants.keys.KEY_SENDING_MESSAGES));
					sControl.sendSMSShout (recipients, panicMsg, panicData);			
					_lsCanvas.setLargeString (l10n.getString(L10nConstants.keys.KEY_MESSAGE_SENT));
	
					doSecPause (2);
				}
				catch (Exception e)
				{
					doSecPause (1);
					_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_ERROR_SENDING) + e.toString());
					doSecPause (10);
	
				}
	
				//now that first shout has been sent, time to wipe
				if (!wipeComplete)
				{
					
					String prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_CONTACTS);
					boolean wipeContacts = (prefBool != null && prefBool.equals("true"));
					
					prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_EVENTS);
					boolean wipeEvents = (prefBool != null && prefBool.equals("true"));
										
					prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_PHOTOS);
					boolean wipePhotos = (prefBool != null && prefBool.equals("true"));
					
					prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_ALL_FILES);
					boolean wipeAllFiles = (prefBool != null && prefBool.equals("true"));
					
					WipeController.doWipe (wipeContacts, wipeEvents, wipePhotos, wipeAllFiles, _lsCanvas, _wipeListener);
					
					wipeComplete = true;
				}
				
				int secs = resendTimeout/1000;
				
				while (secs > 0)
				{
					_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.KEY_PANIC_AGAIN) + secs + l10n.getString(L10nConstants.keys.KEY_SECONDS));
					doSecPause (1);
					secs--;
				}
				
				//update message with new mobile cid, lac info
				panicMsg = sControl.buildShoutMessage(userName, userMessage, userLocation);
				
			}
		}
		catch (Exception ie)
		{
			//do nothing
			Logger.error("PanicController", "error in run thread", ie);
		}

	}
	
	
	
	private static void doSecPause (int secs)
	{
		try { Thread.sleep(secs * 1000);}
		catch(Exception e){}
	}
	

}
