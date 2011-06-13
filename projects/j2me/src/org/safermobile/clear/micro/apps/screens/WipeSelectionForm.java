package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WipeSelectionForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private PanicConfigMIDlet _midlet;
        
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
        public WipeSelectionForm (PanicConfigMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( "Wipe! Selection" );
                setMenuText( l10n.getString(L10nConstants.keys.KEY_MENU_BACK), l10n.getString(L10nConstants.keys.KEY_MENU_NEXT) );

                // Center the text.
                Label label = new Label();
                label.setLabel("Choose how you'd like Wipe! to work below. Caution - erasing or overwriting data is an unrecoverable action, so choose your setup wisely.");
        		label.setHorizontalAlignment( Graphics.LEFT );
        		label.setLabel(l10n.getString(L10nConstants.keys.KEY_PANIC_SMS_TEST_MESSAGE));
        		append(label );
        	
        		
        		_cbContacts = new CheckBox();
        		_cbContacts.setLabel( "Wipe Contacts" );
        		_cbContacts.setChecked( true );
        		append( _cbContacts );

        		_cbPhotos = new CheckBox();
        		_cbPhotos.setLabel( "Wipe Photos" );
        		_cbPhotos.setChecked( true );
        		append( _cbPhotos );
        		
        		_cbAllStorage = new CheckBox();
        		_cbAllStorage.setLabel( "Wipe All Files" );
        		_cbAllStorage.setChecked( true );
        		append( _cbAllStorage );
        		
        		
        		
        		_cbCalendar = new CheckBox();
        		_cbCalendar.setLabel( "Wipe Calendar" );
        		_cbCalendar.setChecked( true );
        		append( _cbCalendar );
        		
        		_cbToDo = new CheckBox();
        		_cbToDo.setLabel( "Wipe ToDo" );
        		_cbToDo.setChecked( true );
        		append( _cbToDo );
        		
        		_cbMemos = new CheckBox();
        		_cbMemos.setLabel( "Wipe Memos" );
        		_cbMemos.setChecked( true );
        		append( _cbMemos );
        		

        }

        private void persist ()
        {
//        	_midlet.savePref(PanicConstants.PREFS_KEY_NAME, tbUserName.getString());
        	
        }
        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
        	_midlet.showStartScreen();
        }

		protected void acceptNotify() {
			
			persist();
			
			DeviceScreen next = new PanicConfigCompleteForm(_midlet);
			_midlet.showAlert(l10n.getString(L10nConstants.keys.KEY_WIPE_TITLE), "Your Wipe! settings have been saved. WARNING: If you have chosen to wipe important data, make sure it is backed up to another device or written on paper first.", next);
			
			
		}
        
        
}
