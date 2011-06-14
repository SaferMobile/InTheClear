package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.apps.controllers.ShoutController;

/**
 * Example of a <code>TextBox</code> component.
 */
public class SMSSendTestForm
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

    	L10nResources l10n = L10nResources.getL10nResources(PanicConstants.DEFAULT_LOCALE);

        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public SMSSendTestForm (PanicConfigMIDlet midlet)
        {
                this.midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.KEY_PANIC_TITLE_SMS_TEST) );
                setMenuText(  l10n.getString(L10nConstants.keys.KEY_MENU_BACK) ,  l10n.getString(L10nConstants.keys.KEY_MENU_SEND) );

             // Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel(l10n.getString(L10nConstants.keys.KEY_PANIC_SMS_TEST_MESSAGE));
        		
        		// Add the label to this screen.
        		append( _label );
        		
                // Add the phone number box.
                phoneNumber = new TextBox();
                phoneNumber.setLabel( l10n.getString(L10nConstants.keys.KEY_PANIC_LBL_PHONE_NUMBER) );
                phoneNumber.setForPhoneNumber();
                phoneNumber.setMaxSize( 20 );
                append( phoneNumber );
               
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
        	sendSMSTestMessage();
        	midlet.showAlert("Success!", "It seems like your message went through. Please check with the recipient to make sure.", midlet.getShoutConfigMenu());

        }
        
        private void sendSMSTestMessage ()
        {
        	String recip = phoneNumber.getString();
        	String msg = "This is a test message. Reply if you get it!";
        	ShoutController sc = new ShoutController();
        	
        	try {
				sc.sendSMSShout(recip, msg, null);
				
			} catch (Exception e) {
				
				midlet.showAlert("Error!", "Unable to send SMS message", this);
				e.printStackTrace();
			}
        }
        
}
