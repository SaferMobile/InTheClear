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

/**
 * Example of a <code>TextBox</code> component.
 */
public class UserInfoForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private PanicConfigMIDlet _midlet;
        
        
        /**
         * The number box used by this example for entering phone numbers.
         */
        private TextBox tbUserName;
        
        /**
         * A number box for entering a PIN.
         */
        private TextBox tbOtherInfo;
    	L10nResources l10n = LocaleManager.getResources();
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public UserInfoForm (PanicConfigMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.KEY_PANIC_TITLE_YOUR_INFO) );
                setMenuText( l10n.getString(L10nConstants.keys.KEY_MENU_BACK), l10n.getString(L10nConstants.keys.KEY_MENU_NEXT) );

                // Center the text.
                Label label = new Label();
                label.setLabel(l10n.getString(L10nConstants.keys.KEY_WIZARD_USER_INFO));
        		label.setHorizontalAlignment( Graphics.LEFT );
        		append(label );
        	        		
                // Add the phone number box.
                tbUserName = new TextBox();
                tbUserName.setLabel( l10n.getString(L10nConstants.keys.KEY_LBL_YOUR_NAME) );
                tbUserName.setForAnyText();
                append( tbUserName );
                
                // Add the PIN number box.
                tbOtherInfo = new TextBox();
                tbOtherInfo.setForAnyText();
                tbOtherInfo.setLabel( l10n.getString(L10nConstants.keys.KEY_LBL_YOUR_INFO) );               
                append( tbOtherInfo );
        }

        private void persist ()
        {
        	_midlet.savePref(PanicConstants.PREFS_KEY_NAME, tbUserName.getString());
        	_midlet.savePref(PanicConstants.PREFS_KEY_LOCATION, tbOtherInfo.getString());        	
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
			
			DeviceScreen next = _midlet.getShoutConfigMenu();
			_midlet.showAlert(l10n.getString(L10nConstants.keys.KEY_PANIC_TITLE), l10n.getString(L10nConstants.keys.KEY_WIZARD_PHONE_CONFIG), next);
			
			//_midlet.showShoutConfigMenu();
			
		}
        
        
}
