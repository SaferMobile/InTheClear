package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.apps.WipeMIDlet;
import org.safermobile.clear.micro.apps.controllers.WipeController;

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
        private CheckBox _cbMemos;
        private CheckBox _cbPhotos;
        private CheckBox _cbAllStorage;
        
        
		L10nResources l10n = L10nResources.getL10nResources(null);
        
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
                label.setLabel("Select one or more data types below to wipe.\n\nWARNING: THIS WILL REMOVE THE ACTUAL DATA FROM YOUR PHONE.");
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
        		
        		_cbMemos = new CheckBox();
        		_cbMemos.setLabel( "Wipe Memos" );
        		_cbMemos.setChecked( false );
        		append( _cbMemos );
        		

        }

        public void run ()
        {
        	showConfirmDialog ();
        	
        }
        
        private void showConfirmDialog ()
        {
        	//are you sure?
        	doWipe();
        }
        
        private void doWipe ()
        {
        	WipeController wControl = new WipeController();
        	
        	if (_cbContacts.isChecked())
        	{
        		try
        		{
        			wControl.wipeContacts();
        		}
        		catch (Exception e)
        		{
        			
        		}
        	}
        	
        	if (_cbCalendar.isChecked())
        	{
        		try
        		{
        			wControl.wipeCalendar();
        		}
        		catch (Exception e)
        		{
        			
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
