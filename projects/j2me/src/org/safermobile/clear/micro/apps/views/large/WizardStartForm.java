package org.safermobile.clear.micro.apps.views.large;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.Dialog;
import org.j4me.ui.components.Label;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCWizardMIDlet;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WizardStartForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private ITCWizardMIDlet _midlet;

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
        public WizardStartForm (ITCWizardMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.SETUP_TITLE) );
                setMenuText( l10n.getString(L10nConstants.keys.MENU_EXIT), l10n.getString(L10nConstants.keys.MENU_NEXT));
                
            	// Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel(l10n.getString(L10nConstants.keys.SETUP_INTRO));
        		
        		// Add the label to this screen.
        		append( _label );

        }
        
     
        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
        	_midlet.notifyDestroyed();
        }
        
        
        /**
         * Takes the user to the previous screen.
         */
        protected void acceptNotify ()
        {
        	_midlet.showNext();
        }
        
      
}
