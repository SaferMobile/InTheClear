package org.safermobile.clear.micro.apps.views;


import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.rms.RecordStoreException;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.apps.WipeMIDlet;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.apps.controllers.WipeListener;
import org.safermobile.clear.micro.apps.models.WipeDataType;
import org.safermobile.clear.micro.ui.ErrorAlert;
import org.safermobile.clear.micro.ui.LargeStringCanvas;
import org.safermobile.micro.utils.Logger;
import org.safermobile.micro.utils.Preferences;

/**
 * Example of a <code>TextBox</code> component.
 */
public class OneTouchPanicForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private PanicConfigMIDlet _midlet;
        
        private CheckBox _cbOneTouch;
        		
    	L10nResources l10n = LocaleManager.getResources();
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public OneTouchPanicForm (PanicConfigMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.ONE_TOUCH_TITLE) );
                setMenuText( l10n.getString(L10nConstants.keys.MENU_BACK), l10n.getString(L10nConstants.keys.MENU_NEXT));

                // Center the text.
                Label label = new Label();
                label.setLabel(l10n.getString(L10nConstants.keys.ONE_TOUCH_INFO));
        		label.setHorizontalAlignment( Graphics.LEFT );
        		append(label );
        	
        		_cbOneTouch = new CheckBox();
        		_cbOneTouch.setLabel( l10n.getString(L10nConstants.keys.ONE_TOUCH_LBL_CB) );
        		_cbOneTouch.setChecked( false );
        		append( _cbOneTouch );

        }

      
        
        protected void declineNotify ()
        {
        	_midlet.showPrev();
        }

        
		protected void acceptNotify() {
			
			
			persist();
			
			_midlet.showNext();
			
		}
		

	     private void persist ()
	        {
	        
	    	 	_midlet.savePref(PanicConstants.PREFS_KEY_ONE_TOUCH_PANIC, _cbOneTouch.isChecked()+"");
	    	 
	    	 	
	        }
        
        
}
