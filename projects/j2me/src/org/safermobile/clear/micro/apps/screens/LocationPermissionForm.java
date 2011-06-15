package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.apps.controllers.ShoutController;
import org.safermobile.clear.micro.data.PhoneInfo;

/**
 * Example of a <code>TextBox</code> component.
 */
public class LocationPermissionForm
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

    	L10nResources l10n = LocaleManager.getResources();
    	
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public LocationPermissionForm (PanicConfigMIDlet midlet)
        {
                this.midlet = midlet;
                
                // Set the title and menu.
                setTitle("Location Permission");
                setMenuText(  l10n.getString(L10nConstants.keys.KEY_MENU_BACK) ,  "Enable" );

             // Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel("When activated, Shout! will optionally send your location information along with your alert message. This information could be essential for your safety, even if your phone does not have GPS. Enable location permissions below.");
        		
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
        	checkLocationPermission();

        }
        
        private void checkLocationPermission ()
        {
        	StringBuffer sbPanicMsg = new StringBuffer();
    	
    		String IMEI = PhoneInfo.getIMEI();
    		if (IMEI != null && IMEI.length() > 0)
    		{

    			sbPanicMsg.append("IMEI:");
    			sbPanicMsg.append(IMEI);
    			sbPanicMsg.append("\n");
    		}
    		
    		String IMSI = PhoneInfo.getIMSI();
    		if (IMSI != null && IMSI.length() > 0)
    		{
    			
    			sbPanicMsg.append("IMSI:");
    			sbPanicMsg.append(IMSI);
    			sbPanicMsg.append("\n");
    		}
    		
    		//append loc info
    		String cid = PhoneInfo.getCellId();
    		if (cid != null && cid.length() > 0)
    		{
    			
    			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_CID));
    			sbPanicMsg.append(cid);
    			sbPanicMsg.append("\n");    			
    		}
    		
    		
    		String lac = PhoneInfo.getLAC();
    		if (lac != null && lac.length() > 0)
    		{    		
    			sbPanicMsg.append(l10n.getString(L10nConstants.keys.KEY_PANIC_MSG_LAC));
    			sbPanicMsg.append(lac);
    			sbPanicMsg.append("\n");    			
    		}
    		
        	midlet.showAlert("Location Info", "This is the location data we could access:\n" + sbPanicMsg.toString(), midlet.getShoutConfigMenu());

        }
        
}
