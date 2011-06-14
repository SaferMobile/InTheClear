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
                setTitle( l10n.getString(L10nConstants.keys.KEY_WIPE_SELECT_TITLE) );
                setMenuText( l10n.getString(L10nConstants.keys.KEY_MENU_BACK), l10n.getString(L10nConstants.keys.KEY_MENU_NEXT) );

                // Center the text.
                Label label = new Label();
                label.setLabel(l10n.getString(L10nConstants.keys.KEY_WIPE_MESSAGE));
        		label.setHorizontalAlignment( Graphics.LEFT );
       
        		append(label );
        	
        		
        		_cbContacts = new CheckBox();
        		_cbContacts.setLabel( l10n.getString(L10nConstants.keys.KEY_WIPE_MENU_CONTACTS) );
        		_cbContacts.setChecked( true );
        		append( _cbContacts );

        		_cbPhotos = new CheckBox();
        		_cbPhotos.setLabel( l10n.getString(L10nConstants.keys.KEY_WIPE_MENU_PHOTOS) );
        		_cbPhotos.setChecked( true );
        		append( _cbPhotos );
        		
        		_cbAllStorage = new CheckBox();
        		_cbAllStorage.setLabel( l10n.getString(L10nConstants.keys.KEY_WIPE_MENU_FILES) );
        		_cbAllStorage.setChecked( true );
        		append( _cbAllStorage );
        		
        		_cbCalendar = new CheckBox();
        		_cbCalendar.setLabel( l10n.getString(L10nConstants.keys.KEY_WIPE_MENU_CALENDAR) );
        		_cbCalendar.setChecked( true );
        		append( _cbCalendar );
        		
        		_cbToDo = new CheckBox();
        		_cbToDo.setLabel( l10n.getString(L10nConstants.keys.KEY_WIPE_MENU_TODO) );
        		_cbToDo.setChecked( true );
        		append( _cbToDo );
        		

        }

        private void persist ()
        {
        	
        	_midlet.savePref(PanicConstants.PREFS_KEY_WIPE_CONTACTS, _cbContacts.isChecked()+"");
        	_midlet.savePref(PanicConstants.PREFS_KEY_WIPE_EVENTS, _cbCalendar.isChecked()+"");
        	_midlet.savePref(PanicConstants.PREFS_KEY_WIPE_TODOS, _cbToDo.isChecked()+"");
        	_midlet.savePref(PanicConstants.PREFS_KEY_WIPE_PHOTOS, _cbPhotos.isChecked()+"");
        	_midlet.savePref(PanicConstants.PREFS_KEY_WIPE_ALL_FILES, _cbAllStorage.isChecked()+"");
        	
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
			_midlet.showAlert(l10n.getString(L10nConstants.keys.KEY_WIPE_TITLE), l10n.getString(L10nConstants.keys.KEY_WIPE_MESSAGE_SAVED), next);
			
			
		}
        
        
}
