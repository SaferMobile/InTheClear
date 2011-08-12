package org.safermobile.clear.micro.apps.controllers;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;

import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;

public class PanicController implements Runnable
{



	/*
	 * stores the user data between the config app and this one
	 */
	private Preferences _prefs; 
	
	private WipeListener _wipeListener;
	private CommandListener _cmdListener;
	
	private DisplayManager _dManager;
	private LargeStringCanvas _lsCanvas;
	private Command _cmdCancel;
	
	/*
	 * used to cancel the panic loop
	 */
	private boolean _keepPanicing = false;

	L10nResources l10n = L10nResources.getL10nResources(null); 
	
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
		String panicData = sControl.buildShoutData (userName);
		
		showMessage (l10n.getString(L10nConstants.keys.KEY_PANIC_START));
		
		doSecPause (5);
		
		if (!_keepPanicing)
			return;
		
		for (int i = 5; i > 0; i--)
		{
			showMessage(l10n.getString(L10nConstants.keys.KEY_SENDING) + i + l10n.getString(L10nConstants.keys.KEY_ELLIPSE));			
			doSecPause (1);
		}
		
		int resendTimeout = ITCConstants.DEFAULT_RESEND_TIMEOUT; //one minute
		
		boolean wipeComplete = false;
		
		while (_keepPanicing)
		{
			try
			{
				showMessage (l10n.getString(L10nConstants.keys.KEY_SENDING_MESSAGES));
				sControl.sendSMSShout (recipients, panicMsg, panicData);			
				showMessage (l10n.getString(L10nConstants.keys.KEY_MESSAGE_SENT));

				doSecPause (2);
			}
			catch (Exception e)
			{
				doSecPause (1);
				showMessage(l10n.getString(L10nConstants.keys.KEY_ERROR_SENDING) + e.toString());
				doSecPause (10);

			}

			//now that first shout has been sent, time to wipe
			if (!wipeComplete)
			{
				showMessage(l10n.getString(L10nConstants.keys.KEY_PREPARING_WIPE));
				WipeController wc = new WipeController();
				
				String prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_CONTACTS);
				boolean wipeContacts = (prefBool != null && prefBool.equals("true"));
				
				prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_EVENTS);
				boolean wipeEvents = (prefBool != null && prefBool.equals("true"));
				boolean wipeToDos = wipeEvents; //grouped together
				
				prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_PHOTOS);
				boolean wipePhotos = (prefBool != null && prefBool.equals("true"));
				boolean wipeVideos = wipePhotos; //grouped together
				
				prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_ALL_FILES);
				boolean wipeAllFiles = (prefBool != null && prefBool.equals("true"));
				
				doSecPause (1);
				showMessage(l10n.getString(L10nConstants.keys.KEY_WIPING_STARTING));
				
				try
				{
					wc.wipePIMData(wipeContacts, wipeEvents, wipeToDos);
					showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_SUCCESS));
				}
				catch (Exception e)
				{
					showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_ERROR));
					e.printStackTrace();
				}
				
				doSecPause (3);
				
				
				
				if (wipePhotos)
				{
					showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_PHOTOS));
					try {
						wc.wipeMedia(WipeController.TYPE_PHOTOS,false,_wipeListener);
						wc.wipeMedia(WipeController.TYPE_PHOTOS,true,_wipeListener);
						
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_PHOTOS_COMPLETE));
					} catch (Exception e) {
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_PHOTOS_ERROR));
						e.printStackTrace();
					}
				}
				
				doSecPause (3);
				
				if (wipeVideos)
				{
					showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_VIDEOS));
					try {
						wc.wipeMedia(WipeController.TYPE_VIDEOS,false,_wipeListener);
						wc.wipeMedia(WipeController.TYPE_VIDEOS,true,_wipeListener);
						
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_VIDEOS_COMPLETE));
					} catch (Exception e) {
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_VIDEOS_ERROR));
						e.printStackTrace();
					}
					
					showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_RECORDINGS));
					try {
						wc.wipeMedia(WipeController.TYPE_RECORDINGS,false,_wipeListener);
						wc.wipeMedia(WipeController.TYPE_RECORDINGS,true,_wipeListener);
						
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_RECORDINGS_COMPLETE));
					} catch (Exception e) {
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_RECORDINGS_ERROR));
						e.printStackTrace();
					}
				}
				
				doSecPause (3);
				

				if (wipeAllFiles)
				{
					showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_FILES));
					try {
						wc.wipeMedia(WipeController.TYPE_MEMORYCARD,false,_wipeListener);
						wc.wipeAllRootPaths(_wipeListener);
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_FILES_COMPLETE));
					} catch (Exception e) {
						showMessage(l10n.getString(L10nConstants.keys.KEY_WIPE_FILES_ERROR));
						e.printStackTrace();
					}
				}
				
				wipeComplete = true;
			}
			
			int secs = resendTimeout/1000;
			
			while (secs > 0)
			{
				showMessage(l10n.getString(L10nConstants.keys.KEY_PANIC_AGAIN) + secs + l10n.getString(L10nConstants.keys.KEY_SECONDS));
				doSecPause (1);
				secs--;
			}
			
			//update message with new mobile cid, lac info
			panicMsg = sControl.buildShoutMessage(userName, userMessage, userLocation);
			
		}
		

	}
	
	private void doSecPause (int secs)
	{
		try { Thread.sleep(secs * 1000);}
		catch(Exception e){}
	}
	
	private void showMessage (String msg)
	{
		Logger.debug(ITCConstants.TAG, "msg: " + msg);

		_lsCanvas.setLargeString(msg);
		
	}

}
