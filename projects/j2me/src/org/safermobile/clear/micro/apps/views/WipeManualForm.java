package org.safermobile.clear.micro.apps.views;


import java.util.Enumeration;

import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;
import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.apps.controllers.WipeListener;
import org.safermobile.clear.micro.ui.ErrorAlert;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WipeManualForm
        extends Dialog implements Runnable, WipeListener, CommandListener, OnClickListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;
        
        private CheckBox _cbContacts;
        private CheckBox _cbCalendar;
        private CheckBox _cbPhotos;
        private CheckBox _cbAllStorage;
        
        private WipeController _wControl;

        //ErrorAlert statusDialog;
        LargeStringCanvas _lsCanvas;
    	private Command	 _cmdExit;
    	
        String currentType;
        int successCount = 0;
        int errCount = 0;
		
    	L10nResources l10n = LocaleManager.getResources();
        
    	/*
    	 * stores the user data between the config app and this one
    	 */
    	private Preferences _prefs = null; 
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public WipeManualForm (ITCMainMIDlet midlet)
        {
                _midlet = midlet;
                
                try
        		{
        		 _prefs = new Preferences (ITCConstants.PANIC_PREFS_DB);
        		} catch (RecordStoreException e) {
        			
        			Logger.error(ITCConstants.TAG, "a problem saving the prefs: " + e, e);
        		}
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.TITLE_WIPE_MANUAL) );
              //  setMenuText( l10n.getString(L10nConstants.keys.MENU_EXIT), l10n.getString(L10nConstants.keys.MENU_WIPE_NOW));

                // Center the text.
                Label label = new Label();
                label.setLabel(l10n.getString(L10nConstants.keys.WIPE_WARNING_MSG));
        		label.setHorizontalAlignment( Graphics.LEFT );
        		append(label );
        	
        		_cbContacts = new CheckBox();
        		_cbContacts.setLabel( l10n.getString(L10nConstants.keys.WIPE_MENU_CONTACTS) );
        		_cbContacts.setChecked( false );
        		append( _cbContacts );

        		_cbPhotos = new CheckBox();
        		_cbPhotos.setLabel( l10n.getString(L10nConstants.keys.WIPE_MENU_PHOTOS) );
        		_cbPhotos.setChecked( false );
        		append( _cbPhotos );
        		
        		
        		_cbAllStorage = new CheckBox();
        		_cbAllStorage.setLabel( l10n.getString(L10nConstants.keys.WIPE_MENU_FILES) );
        		_cbAllStorage.setChecked( false );
        		append( _cbAllStorage );
        		
        		_cbCalendar = new CheckBox();
        		_cbCalendar.setLabel( l10n.getString(L10nConstants.keys.WIPE_MENU_CALENDAR) );
        		_cbCalendar.setChecked( false );
        		append( _cbCalendar );
        		
        		/*
        		Enumeration eRoots = FileSystemRegistry.listRoots();
        		
        		while (eRoots.hasMoreElements())
        		{
        			CheckBox cb = new CheckBox();
            		cb.setLabel((String)eRoots.nextElement());
            		cb.setChecked( false );
            		append( cb );
        		}
        		*/
        		
        		Button btn = new Button();
        		btn.setOnClickListener(this);
        		btn.setLabel(l10n.getString(L10nConstants.keys.MENU_WIPE_NOW));
        		append (btn);
        		
        		load();

        }

        public void load ()
        {
        	String prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_CONTACTS);
			boolean wipeContacts = (prefBool != null && prefBool.equals("true"));
			_cbContacts.setChecked(wipeContacts);
			
			prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_EVENTS);
			boolean wipeEvents = (prefBool != null && prefBool.equals("true"));
			boolean wipeToDos = wipeEvents; //grouped together
			_cbCalendar.setChecked(wipeEvents);
			
			prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_PHOTOS);
			boolean wipePhotos = (prefBool != null && prefBool.equals("true"));
			boolean wipeVideos = wipePhotos; //grouped together
			_cbPhotos.setChecked(wipePhotos);
						
			prefBool = _prefs.get(ITCConstants.PREFS_KEY_WIPE_ALL_FILES);
			boolean wipeAllFiles = (prefBool != null && prefBool.equals("true"));
			_cbAllStorage.setChecked(wipeAllFiles);
			
        }
        
        public void run ()
        {
        	confirmAndWipe ();
        	
        }
        
        private void confirmAndWipe ()
        {
        	//are you sure? if yes, then
        	boolean confirmed = true; //need to add this in
        	
        	if (confirmed)
	        {

        		_lsCanvas = new LargeStringCanvas("");
        		_cmdExit = new Command(l10n.getString(L10nConstants.keys.MENU_EXIT), Command.EXIT,1);
        		_lsCanvas.addCommand(_cmdExit);
        		_lsCanvas.setCommandListener(this);
        		
        		Display.getDisplay(_midlet).setCurrent(_lsCanvas);

        		//_lsCanvas.setCommandListener(this);
        		//_lsCanvas.addCommand(_cmdCancel);
        		
        		try
        		{
        			
        			_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_INPROGRESS));
        			
        			_wControl = new WipeController();
                	
            		_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_PERSONAL));
            		currentType = l10n.getString(L10nConstants.keys.WIPE_TYPE_PERSONAL);
            		_wControl.wipePIMData(_cbContacts.isChecked(), _cbCalendar.isChecked(), _cbCalendar.isChecked());
                	
            		try
            		{
	                	if (_cbPhotos.isChecked())
	                	{
	                		currentType = l10n.getString(L10nConstants.keys.WIPE_TYPE_PHOTOS);
	                		_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_PHOTOS));
	            			
	                		_wControl.wipeMedia(WipeController.TYPE_PHOTOS,false,this);
	                		_wControl.wipeMedia(WipeController.TYPE_PHOTOS,true,this);

	                		_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_VIDEOS));
	
	                		_wControl.wipeMedia(WipeController.TYPE_VIDEOS,false,this);
	                		_wControl.wipeMedia(WipeController.TYPE_VIDEOS,true,this);
	                		
	                		_lsCanvas.setLargeString("Wiping recordings...");
	                		
	                		_wControl.wipeMedia(WipeController.TYPE_RECORDINGS,false,this);
	                		_wControl.wipeMedia(WipeController.TYPE_RECORDINGS,true,this);
	                	}
            		}
                	catch (Exception e)
                	{
                		_lsCanvas.setLargeString(e.getMessage());
                	}
                	
                	
                	try
                	{
	                	if (_cbAllStorage.isChecked())
	                	{
	                		currentType = l10n.getString(L10nConstants.keys.WIPE_TYPE_FILES);
	                		_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_FILES));
	                		_wControl.wipeMedia("memorycard",false,this);
	                		_wControl.wipeAllRootPaths(this);
	                	}
                	}
                	catch (Exception e)
                	{
                		_lsCanvas.setLargeString(e.getMessage());
                	}
                	
                	
                	String msg = l10n.getString(L10nConstants.keys.WIPE_MSG_SUCCESS);
                	
                	if (successCount > 0)
                		msg += "\n" + successCount + ' ' + l10n.getString(L10nConstants.keys.WIPE_STATUS_FILES_DELETED);
                	else
                		msg += "\n" + "No items founds";
                	
                	if (errCount > 0)
                		msg += "\n" + errCount + ' ' + l10n.getString(L10nConstants.keys.WIPE_STATUS_ERRORS);
                	
                	_lsCanvas.setLargeString(msg);
                	
        		}
        		catch (Exception e)
        		{
        			String msg = e.getMessage();
        			_lsCanvas.setLargeString(msg);
        			
        			e.printStackTrace();
        		}
	        }
        }
        
        public boolean hasMenuBar ()
        {
        	return false;
        }
        
		public void onClick(Component c) 
		{
			successCount = 0;
			errCount = 0;
			
			new Thread(this).start();
			
		}
		
		protected void declineNotify ()
		{
			_midlet.showMainForm();
		}
		

		public void wipingFileSuccess(String path) {
			successCount++;
			
    		_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_WIPING_WORD) + currentType + ": " + successCount);

		}
		
		public void wipingFileError(String path, String err) {
			
			errCount++;
			
    		_lsCanvas.setLargeString(l10n.getString(L10nConstants.keys.WIPE_STATUS_WIPING_WORD) + currentType + " err: " + errCount);

		}

		public void wipePercentComplete(int percent) 
		{
			
		}
		

		/* (non-Javadoc)
		 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
		 */
		public void commandAction(Command command, Displayable displayable) {
			
			if (command == _cmdExit)
			{
				_wControl.cancel();
				_midlet.notifyDestroyed();
				
			}
		}
        
        
}
