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
import org.safermobile.clear.micro.ui.ErrorAlert;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WipeManualForm
        extends Dialog implements Runnable
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
                setTitle( "Wipe! Now" );
                setMenuText( "Exit", "WIPE NOW!");

                // Center the text.
                Label label = new Label();
                label.setLabel("Select one or more data types below to wipe. WARNING: THIS WILL DELETE REAL DATA FROM YOUR PHONE.");
        		label.setHorizontalAlignment( Graphics.LEFT );
        		append(label );
        	
        		_cbContacts = new CheckBox();
        		_cbContacts.setLabel( "Wipe Contacts" );
        		_cbContacts.setChecked( false );
        		append( _cbContacts );

        		_cbPhotos = new CheckBox();
        		_cbPhotos.setLabel( "Wipe Photos" );
        		_cbPhotos.setChecked( false );
        		append( _cbPhotos );
        		
        		_cbAllStorage = new CheckBox();
        		_cbAllStorage.setLabel( "Wipe All Files" );
        		_cbAllStorage.setChecked( false );
        		append( _cbAllStorage );
        		
        		
        		
        		_cbCalendar = new CheckBox();
        		_cbCalendar.setLabel( "Wipe Calendar" );
        		_cbCalendar.setChecked( false );
        		append( _cbCalendar );
        		
        		_cbToDo = new CheckBox();
        		_cbToDo.setLabel( "Wipe ToDo" );
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
                		wControl.wipePhotos();
                	
                	if (_cbAllStorage.isChecked())
                		wControl.wipeAllRootPaths();
                	
                	ErrorAlert eAlert = new ErrorAlert ("Wipe! Complete", "You have successfully performed the wipe.", null, this);
        			eAlert.show();
        			
        		}
        		catch (Exception e)
        		{
        			String msg = e.getMessage();
        			ErrorAlert eAlert = new ErrorAlert ("Wipe! Error", "There was an error performing the wipe: " + msg, null, this);
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
			
			new Thread(this).start();
			
			
		}
        
        
}
