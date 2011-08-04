package org.safermobile.clear.micro.apps.views;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;
import org.safermobile.clear.micro.apps.ITCConstants;

/**
 * Example of a <code>TextBox</code> component.
 */
public class SetupAlertMessageForm
        extends Dialog implements Runnable, OnClickListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;


        /**
         * The textbox for the list of comma-sep numbers to send the alert to
         */        
        private TextBox _tbRecipients;

        /*
         * The textbox for the alert message sent
         */
        private TextBox _tbAlertMsg;
        

    	L10nResources l10n = LocaleManager.getResources();

        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public SetupAlertMessageForm (ITCMainMIDlet midlet)
        {
               _midlet = midlet;
                
                // Set the title and menu.
                setTitle(l10n.getString(L10nConstants.keys.PANIC_MESSAGE_TITLE) );
                //setMenuText(  l10n.getString(L10nConstants.keys.MENU_BACK) ,  l10n.getString(L10nConstants.keys.MENU_NEXT) );

             // Center the text.
                Label label = new Label();
        		label.setHorizontalAlignment( Graphics.LEFT );
        		label.setLabel(l10n.getString(L10nConstants.keys.PANIC_MESSAGE_INFO));
        		
        		// Add the label to this screen.
        		append( label );
        		
        	    // Add the phone number box.
        		_tbRecipients = new TextBox();
        		_tbRecipients.setLabel(l10n.getString(L10nConstants.keys.PANIC_MSG_LBL_PHONE));        
        		_tbRecipients.setForPhoneNumber();
                append( _tbRecipients );
                
                // Add the message box
        		_tbAlertMsg = new TextBox();
        		_tbAlertMsg.setLabel(l10n.getString(L10nConstants.keys.PANIC_MESSAGE_LBL_MSG));                
                append( _tbAlertMsg );
               
                Button btn = new Button();
        		btn.setOnClickListener(this);
        		btn.setLabel(l10n.getString(L10nConstants.keys.BUTTON_CONTINUE));
        		append (btn);
        		
                load();
        }

        private boolean persist ()
        {
        	String recips = _tbRecipients.getString();
        	String alertmsg = _tbAlertMsg.getString();
        	
        	if (recips.length() > 0 && alertmsg.length() > 0)
        	{

				//save the phone number if the SMS sends okay
	    		_midlet.savePref(ITCConstants.PREFS_KEY_RECIPIENT, recips);    
	    		
        		_midlet.savePref(ITCConstants.PREFS_KEY_MESSAGE, alertmsg);
        		return true;
        	}
        	
        	return false;
        }
        
        private void load ()
        {
        	
        	String alertMsgString = _midlet.loadPref(ITCConstants.PREFS_KEY_MESSAGE);
        	
        	if (alertMsgString != null && alertMsgString.length() > 0)
        		_tbAlertMsg.setString(alertMsgString);
        	else
        		_tbAlertMsg.setString(l10n.getString(L10nConstants.keys.DEFAULT_ALERT_MSG));
        }
        
        
        public void run ()
        {
        	if (persist())
        	{
        		_midlet.showNext();
        	}
        	else
        	{
            	_midlet.showAlert(l10n.getString(L10nConstants.keys.TITLE_ERROR), l10n.getString(L10nConstants.keys.ERROR_NOT_COMPLETE), _midlet.getCurrentScreenIdx());
        		
        	}
        }
        
        public boolean hasMenuBar ()
        {
        	return false;
        }
        
		public void onClick(Component c) {
			new Thread(this).start();
			
		}
       
}
