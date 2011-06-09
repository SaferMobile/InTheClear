package org.safermobile.clear.micro.apps.screens;


import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;

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
		L10nResources l10n = L10nResources.getL10nResources(null);
        
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

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
        	_midlet.showNext();
        }

		protected void acceptNotify() {
			
			_midlet.showShoutConfigMenu();
			
		}
        
        
}
