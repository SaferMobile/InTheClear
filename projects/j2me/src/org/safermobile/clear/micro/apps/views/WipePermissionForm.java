package org.safermobile.clear.micro.apps.views;


import javax.microedition.lcdui.Graphics;
import javax.microedition.pim.PIMException;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.controllers.ShoutController;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.data.PhoneInfo;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WipePermissionForm
        extends Dialog implements Runnable
{
        /**
         * The previous screen.
         */
        private PanicConfigMIDlet midlet;
        
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
        public WipePermissionForm (PanicConfigMIDlet midlet)
        {
                this.midlet = midlet;
                
                // Set the title and menu.
                setTitle("Wipe Permission");
                setMenuText(  l10n.getString(L10nConstants.keys.MENU_BACK) ,  "Enable" );

             // Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel("Wipe can be set up to erase or overwrite your phone's existing data when activated. It requires permissions to access your contact list or SMS database to do so.");
        		
        		// Add the label to this screen.
        		append( _label );
        		
               
        }

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
                midlet.showShoutConfigMenu();
        }
        
        protected void acceptNotify() 
        {		
        	//do SMS test
        	new Thread(this).start();
        }
        
        public void run ()
        {
        	checkWipePermission();

        }
        
        private void checkWipePermission ()
        {
        	
        	WipeController wc = new WipeController();
        	
        	try {
				wc.getContacts();
				midlet.showAlert("Wipe Info", "Great! Seems like we can access your local data.", midlet.getShoutConfigMenu());
				
        	} catch (PIMException e) {

				midlet.showAlert("Error!", "We were unable to access your data, which means we cannot wipe it.", this);
				e.printStackTrace();
			}
        	
        	

        }
        
}
