package org.safermobile.clear.micro.apps.views;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;
import org.safermobile.clear.micro.apps.ITCConstants;
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
        private ITCMainMIDlet midlet;
        
    	private Label _label = new Label();

    	L10nResources l10n = LocaleManager.getResources();
    	
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public LocationPermissionForm (ITCMainMIDlet midlet)
        {
                this.midlet = midlet;
                setupUI();
        }
        
        public void setupUI()
        {
                // Set the title and menu.
                setTitle(l10n.getString(L10nConstants.keys.TITLE_LOCATION_PERMISSION));
                setMenuText(  l10n.getString(L10nConstants.keys.MENU_BACK) ,  l10n.getString(L10nConstants.keys.MENU_ENABLE) );

             // Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel(l10n.getString(L10nConstants.keys.PERMISSION_INFO));
        		
        		// Add the label to this screen.
        		append( _label );
        		
               
        }

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
                midlet.showPrev();
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

    			sbPanicMsg.append(l10n.getString(L10nConstants.keys.LBL_IMEI));
    			sbPanicMsg.append(IMEI);
    			sbPanicMsg.append("\n");
    		}
    		
    		String IMSI = PhoneInfo.getIMSI();
    		if (IMSI != null && IMSI.length() > 0)
    		{
    			
    			sbPanicMsg.append(l10n.getString(L10nConstants.keys.LBL_IMSI));
    			sbPanicMsg.append(IMSI);
    			sbPanicMsg.append("\n");
    		}
    		
    		//append loc info
    		String cid = PhoneInfo.getCellId();
    		if (cid != null && cid.length() > 0)
    		{
    			
    			sbPanicMsg.append(l10n.getString(L10nConstants.keys.PANIC_MSG_CID));
    			sbPanicMsg.append(cid);
    			sbPanicMsg.append("\n");    			
    		}
    		
    		
    		String lac = PhoneInfo.getLAC();
    		if (lac != null && lac.length() > 0)
    		{    		
    			sbPanicMsg.append(l10n.getString(L10nConstants.keys.PANIC_MSG_LAC));
    			sbPanicMsg.append(lac);
    			sbPanicMsg.append("\n");    			
    		}
    		
    		if (sbPanicMsg.length()>0)
    		{
    			midlet.showAlert(l10n.getString(L10nConstants.keys.TITLE_LOCATION_INFO), l10n.getString(L10nConstants.keys.PERMISSION_LOC_SUCCESS) + "\n" + sbPanicMsg.toString(), midlet.getNextScreenIdx());
    		}
    		else
    		{
    			midlet.showAlert(l10n.getString(L10nConstants.keys.TITLE_LOCATION_INFO), l10n.getString(L10nConstants.keys.PERMISSION_LOC_FAILURE), midlet.getCurrentScreenIdx());

    		}
        }
}
