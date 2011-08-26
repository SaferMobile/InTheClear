package org.safermobile.clear.micro.apps.views;


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
import org.safermobile.clear.micro.apps.controllers.PanicController;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.apps.controllers.WipeListener;
import org.safermobile.clear.micro.ui.ErrorAlert;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.ui.DisplayManager;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

/**
 * Example of a <code>TextBox</code> component.
 */
public class StartPanicForm
        extends Dialog implements WipeListener, CommandListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;


        String currentType;
        int successCount = 0;
        int errCount = 0;
        
        Command _cmdCancel;
        
        PanicController _pc;
        Thread _thread;
		

    	private L10nResources l10n;
    	
    	/*
    	 * stores the user data between the config app and this one
    	 */
    	private Preferences _prefs = null; 
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public StartPanicForm (ITCMainMIDlet midlet)
        {
                _midlet = midlet;
                
                try
        		{
        		 _prefs = new Preferences (ITCConstants.PANIC_PREFS_DB);
        		} catch (RecordStoreException e) {
        			
        			Logger.error(ITCConstants.TAG, "a problem saving the prefs: " + e, e);
        		}
                
               
        }

        public void show ()
        {
        	super.show();
	    	l10n = LocaleManager.getResources();
	    	deleteAll();
	    	
    		DisplayManager manager = new DisplayManager(_midlet.getCurrentDisplay());

    		_cmdCancel = new Command(l10n.getString(L10nConstants.keys.MENU_CANCEL), Command.SCREEN,1);
    	
    		_pc = new PanicController(_prefs, this, this, manager, _cmdCancel);
    		_thread = new Thread(_pc);
    		_thread.start();
        }
       
        
        
        protected void declineNotify ()
        {
        	_midlet.showMainForm();
        }

        
		protected void acceptNotify() {
			
			
			successCount = 0;
			errCount = 0;
			
			
			
			
		}
		

		public void wipingFileSuccess(String path) {
			successCount++;
			

		}
		
		public void wipingFileError(String path, String err) {
			
			errCount++;
			

		}

		public void wipePercentComplete(int percent) 
		{
			
		}
		

		/* (non-Javadoc)
		 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
		 */
		public void commandAction(Command command, Displayable displayable) {
			
			if (command == _cmdCancel)
			{
				_pc.stopPanic();
				_midlet.showMainForm();
				
			}
		}

		public void wipeStatus(String message) {
			// TODO Auto-generated method stub
			
		}
        
        
		

}
