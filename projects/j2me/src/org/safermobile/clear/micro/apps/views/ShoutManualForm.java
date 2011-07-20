package org.safermobile.clear.micro.apps.views;


import javax.microedition.lcdui.Graphics;
import javax.microedition.rms.RecordStoreException;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.apps.ShoutMIDlet;
import org.safermobile.clear.micro.apps.controllers.ShoutController;
import org.safermobile.clear.micro.ui.ErrorAlert;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

/**
 * Example of a <code>TextBox</code> component.
 */
public class ShoutManualForm
        extends Dialog implements Runnable
{
        /**
         * The previous screen.
         */
        private ShoutMIDlet midlet;
        
        /**
         * The number box used by this example for entering phone numbers.
         */
        private TextBox tbPhoneNumber;        
        private TextBox tbMessage;
        
    	private Label _label = new Label();

    	L10nResources l10n = LocaleManager.getResources();

    	private Preferences _prefs;
    	
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public ShoutManualForm (ShoutMIDlet midlet)
        {
                this.midlet = midlet;
                
                try
        		{
        		 _prefs = new Preferences (PanicConstants.PANIC_PREFS_DB);
        		} catch (RecordStoreException e) {
        			
        			Logger.error(PanicConstants.TAG, "a problem saving the prefs: " + e, e);
        		}
                
                // Set the title and menu.
                setTitle( "Shout!" );
                setMenuText( "Exit" ,  l10n.getString(L10nConstants.keys.MENU_SEND) );

             // Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel("Send a Shout! Message");
        		
        		// Add the label to this screen.
        		append( _label );
        		
                // Add the phone number box.
        		tbPhoneNumber = new TextBox();
        		tbPhoneNumber.setLabel( l10n.getString(L10nConstants.keys.SMS_TEST_LBL_PHONE) );
                tbPhoneNumber.setForPhoneNumber();
                tbPhoneNumber.setMaxSize( 20 );
                append( tbPhoneNumber );
                
                // Add the phone number box.
        		tbMessage = new TextBox();
        		tbMessage.setLabel(l10n.getString(L10nConstants.keys.SMS_TEST_LBL_MSG));        		
                append( tbMessage );
                
                load();
               
        }

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
        	midlet.notifyDestroyed();

        }
        
        protected void acceptNotify() 
        {		
        	//do SMS test
        	new Thread(this).start();
        }
        
        public void run ()
        {
        	sendShoutMessage();

        }
        
        private void load ()
        {
        	if (_prefs.get(PanicConstants.PREFS_KEY_RECIPIENT) != null)
        	{
        		tbPhoneNumber.setString(_prefs.get(PanicConstants.PREFS_KEY_RECIPIENT));
        		tbMessage.setString(_prefs.get(PanicConstants.PREFS_KEY_MESSAGE));
        	}
        }
        
        private void sendShoutMessage ()
        {
        	String userName = "";
        	
        	String recip = tbPhoneNumber.getString();
        	String msg =  tbMessage.getString();
        	
        	ShoutController sc = new ShoutController();
        	

        	String data = sc.buildShoutData(userName);
        	
        	try {
				sc.sendSMSShout(recip, msg, data);
				ErrorAlert eAlert = new ErrorAlert ("Success!", "Your Shout! was sent.", null, this);
				eAlert.show();
				
			} catch (Exception e) {
				
				ErrorAlert eAlert = new ErrorAlert ("Error!", "Unable to send Shout! message. Try again.", null, this);
				eAlert.show();
				e.printStackTrace();
			}
        }
        
}
