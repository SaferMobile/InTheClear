package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;

/**
 * Example of a <code>TextBox</code> component.
 */
public class SetupAlertMessageForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private PanicConfigMIDlet _midlet;
        
        /**
         * The number box used by this example for entering phone numbers.
         */
        private TextBox phoneNumber;
        
        
    	private Label _label = new Label();

		L10nResources l10n = L10nResources.getL10nResources(null);

        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public SetupAlertMessageForm (PanicConfigMIDlet midlet)
        {
               _midlet = midlet;
                
                // Set the title and menu.
                setTitle(l10n.getString(L10nConstants.keys.KEY_ALERT_MESSAGE_TITLE) );
                setMenuText(  l10n.getString(L10nConstants.keys.KEY_MENU_BACK) ,  l10n.getString(L10nConstants.keys.KEY_MENU_NEXT) );

             // Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel(l10n.getString(L10nConstants.keys.KEY_ALERT_MESSAGE_INFO));
        		
        		// Add the label to this screen.
        		append( _label );
        		
                // Add the phone number box.
                phoneNumber = new TextBox();
                phoneNumber.setLabel( l10n.getString(L10nConstants.keys.KEY_PANIC_LBL_PHONE_NUMBER) );
                phoneNumber.setForPhoneNumber();
                phoneNumber.setMaxSize( 20 );
                append( phoneNumber );
                
                // Add the phone number box.
                phoneNumber = new TextBox();
                phoneNumber.setLabel(l10n.getString(L10nConstants.keys.KEY_ALERT_LBL));                
                append( phoneNumber );
               
        }

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
               _midlet.showShoutConfigMenu();
        }
        
        protected void acceptNotify() 
        {		
        	//do SMS test
		}
}
