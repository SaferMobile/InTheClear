package org.safermobile.clear.micro.apps.views;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.LocaleManager;
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
        private PanicConfigMIDlet _midlet;
        
        /**
         * The number box used by this example for entering phone numbers.
         */
        private TextBox _tbPhoneNumber;
        

        
    	private Label _label = new Label();

    	L10nResources l10n = LocaleManager.getResources();

        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public SMSSendTestForm (PanicConfigMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.SMS_TEST_TITLE) );
                setMenuText(  l10n.getString(L10nConstants.keys.MENU_BACK) ,  l10n.getString(L10nConstants.keys.MENU_SEND) );

             // Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel(l10n.getString(L10nConstants.keys.SMS_TEST_MSG));
        		
        		// Add the label to this screen.
        		append( _label );
        		
                // Add the phone number box.
                _tbPhoneNumber = new TextBox();
                _tbPhoneNumber.setLabel( l10n.getString(L10nConstants.keys.SMS_TEST_LBL_PHONE) );
                _tbPhoneNumber.setForPhoneNumber();                
                append( _tbPhoneNumber );

        }

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
                _midlet.showPrev();
        }
        
        protected void acceptNotify() 
        {		
        	//do SMS test
        	new Thread(this).start();
        }
        
        public void run ()
        {
        	sendSMSTestMessage();

        }
        
        private void sendSMSTestMessage ()
        {
        	String recip = _tbPhoneNumber.getString();

        	if (recip.trim().length() == 0)
        	{
				_midlet.showAlert(l10n.getString(L10nConstants.keys.TITLE_ERROR),  l10n.getString(L10nConstants.keys.ERROR_NOT_COMPLETE), _midlet.getCurrentScreenIdx());

        	}
        	else
        	{
        	
	    		
	        	String msg =  l10n.getString(L10nConstants.keys.SMS_TEST_DEFAULT_MSG);
	        	ShoutController sc = new ShoutController();
	        	
	        	try {
	        		
					sc.sendSMSShout(recip, msg, null);
	
		        	_midlet.showAlert(l10n.getString(L10nConstants.keys.TITLE_SUCCESS), l10n.getString(L10nConstants.keys.ERROR_SMS_SUCCESS), _midlet.getNextScreenIdx());

	        		
				} catch (Exception e) {
					
					_midlet.showAlert(l10n.getString(L10nConstants.keys.TITLE_ERROR),  l10n.getString(L10nConstants.keys.ERROR_SMS_FAILURE), _midlet.getCurrentScreenIdx());
					e.printStackTrace();
				}
        	}
        }
        
}
