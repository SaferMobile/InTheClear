package org.safermobile.clear.micro.apps.controllers;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;

import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

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
		
		showMessage ("PANIC MESSAGE:\n" + panicMsg + "\n\npreparing to send...");
		
		doSecPause (5);
		
		
		for (int i = 5; i > 0; i--)
		{
			showMessage("Sending in " + i + "...");			
			doSecPause (1);
		}
		
		int resendTimeout = ITCConstants.DEFAULT_RESEND_TIMEOUT; //one minute
		
		boolean wipeComplete = false;
		
		while (_keepPanicing)
		{
			try
			{
				showMessage ("Sending messages...");
				sControl.sendSMSShout (recipients, panicMsg, panicData);			
				showMessage ("Panic Sent!");

				doSecPause (2);
			}
			catch (Exception e)
			{
				doSecPause (1);
				showMessage("Error Sending:\n" + e.toString());
				doSecPause (10);

			}

			//now that first shout has been sent, time to wipe
			if (!wipeComplete)
			{
				showMessage("Preparing to\nwipe data...");
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
				showMessage("Wiping selected\npersonal data...");
				
				try
				{
					wc.wipePIMData(wipeContacts, wipeEvents, wipeToDos);
					showMessage("Success!\nPersonal data wiped!");
				}
				catch (Exception e)
				{
					showMessage("WARNING: There was an error wiping your personal data.");
					e.printStackTrace();
				}
				
				doSecPause (3);
				
				
				
				if (wipePhotos)
				{
					showMessage("Wiping photos...");
					try {
						wc.wipePhotos(_wipeListener);
						showMessage("Wiping photos...\nWIPE COMPLETE.");
					} catch (Exception e) {
						showMessage("Wiping photos...nERROR. UNABLE TO WIPE PHOTOS.");
						e.printStackTrace();
					}
				}
				
				doSecPause (3);
				
				if (wipeVideos)
				{
					showMessage("Wiping videos...");
					try {
						wc.wipePhotos(_wipeListener);
						showMessage("Wiping videos...\nWIPE COMPLETE.");
					} catch (Exception e) {
						showMessage("Wiping videos...nERROR!");
						e.printStackTrace();
					}
				}
				
				doSecPause (3);
				

				if (wipeAllFiles)
				{
					showMessage("Wiping files...");
					try {
						wc.wipeMemoryCard(_wipeListener);
						wc.wipeAllRootPaths(_wipeListener);
						showMessage("Wiping files...\nWIPE COMPLETE.");
					} catch (Exception e) {
						showMessage("Wiping photos...\nERROR!");
						e.printStackTrace();
					}
				}
				
				wipeComplete = true;
			}
			
			int secs = resendTimeout/1000;
			
			while (secs > 0)
			{
				showMessage("Panic! again in\n" + secs + "secs...");
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
