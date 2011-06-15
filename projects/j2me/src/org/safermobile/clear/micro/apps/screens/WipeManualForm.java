package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.apps.WipeMIDlet;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.apps.controllers.WipeListener;
import org.safermobile.clear.micro.ui.ErrorAlert;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WipeManualForm
        extends Dialog implements Runnable, WipeListener
{
        /**
         * The previous screen.
         */
        private WipeMIDlet _midlet;
        
        private CheckBox _cbContacts;
        private CheckBox _cbCalendar;
        private CheckBox _cbToDo;
        private CheckBox _cbPhotos;
        private CheckBox _cbAllStorage;
        

        ErrorAlert statusDialog;

		StringBuffer log = new StringBuffer();
		
    	L10nResources l10n = LocaleManager.getResources();
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public WipeManualForm (WipeMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.TITLE_WIPE_MANUAL) );
                setMenuText( l10n.getString(L10nConstants.keys.MENU_EXIT), l10n.getString(L10nConstants.keys.MENU_WIPE_NOW));

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
        		
        		_cbToDo = new CheckBox();
        		_cbToDo.setLabel( l10n.getString(L10nConstants.keys.WIPE_MENU_TODO) );
        		_cbToDo.setChecked( false );
        		append( _cbToDo );
        		

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
        		try
        		{
        			
        			WipeController wControl = new WipeController();
                	
                	wControl.wipePIMData(_cbContacts.isChecked(), _cbCalendar.isChecked(), _cbToDo.isChecked());
                	
                	
                	if (_cbPhotos.isChecked())
                		wControl.wipePhotos(this);
                	
                	if (_cbAllStorage.isChecked())
                		wControl.wipeAllRootPaths(this);

        			log.append("\n\n" + l10n.getString(L10nConstants.keys.WIPE_MSG_SUCCESS));
        			
                	ErrorAlert eAlert = new ErrorAlert (l10n.getString(L10nConstants.keys.TITLE_WIPE_COMPLETE), log.toString(), null, this);
        			eAlert.show();

        		}
        		catch (Exception e)
        		{
        			String msg = e.getMessage();
        			
        			log.append(l10n.getString(L10nConstants.keys.WIPE_MSG_ERR) + msg);
        			
        			ErrorAlert eAlert = new ErrorAlert (l10n.getString(L10nConstants.keys.TITLE_WIPE_ERR), log.toString(), null, this);
        			eAlert.show();
        			
        			//todo: need to show error alert here
        			e.printStackTrace();
        		}
	        }
        }
        
        protected void declineNotify ()
        {
        	_midlet.notifyDestroyed();
        }

        
		protected void acceptNotify() {
			
			log = new StringBuffer();
			statusDialog = new ErrorAlert (l10n.getString(L10nConstants.keys.TITLE_WIPE_INPROGRESS), l10n.getString(L10nConstants.keys.WIPE_MSG_INPROGRESS), null, this);
			statusDialog.show();
			
			new Thread(this).start();
			
			
		}
		

		public void wipingFile(String path) {
			
			String update = l10n.getString(L10nConstants.keys.WIPE_MSG_FILE) + path;
			
			log.append(update);
			log.append('\n');
		}

		public void wipePercentComplete(int percent) {
			
			
		}
        
        
}
