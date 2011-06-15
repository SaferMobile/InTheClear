package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;

/**
 * Example of a <code>TextBox</code> component.
 */
public class PanicConfigCompleteForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private PanicConfigMIDlet _midlet;

    	/**
    	 * The label 
    	 */
    	private Label _label = new Label();
    	L10nResources l10n = LocaleManager.getResources();
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public PanicConfigCompleteForm (PanicConfigMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.PANIC_SETUP_TITLE) );
                setMenuText(null, l10n.getString(L10nConstants.keys.MENU_EXIT));
                
            	// Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		// Make the label be mutliple paragraphs.
        		_label.setLabel("Your configuration of the Panic! settings are now complete. You may run this wizard again to change your settings.");
        		
        		// Add the label to this screen.
        		append( _label );

        		

        }
        
     
   
        
        /**
         * Takes the user to the previous screen.
         */
        protected void acceptNotify ()
        {
        	_midlet.notifyDestroyed();
        }
        
      
}
