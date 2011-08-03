package org.safermobile.clear.micro.apps.views;


import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;
import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;

/**
 * Example of a <code>TextBox</code> component.
 */
public class SetupCompleteForm
        extends Dialog implements OnClickListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;

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
        public SetupCompleteForm (ITCMainMIDlet midlet)
        {
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.SETUP_COMPLETE_TITLE) );
               
                
            	// Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		// Make the label be mutliple paragraphs.
        		_label.setLabel(l10n.getString(L10nConstants.keys.SETUP_COMPLETE_MSG));
        		
        		// Add the label to this screen.
        		append( _label );

        		Button btn = new Button();
        		btn.setOnClickListener(this);
        		btn.setLabel(l10n.getString(L10nConstants.keys.BUTTON_CONTINUE));
        		append (btn);

        }
        
        public boolean hasMenuBar ()
        {
        	return false;
        }
        
		public void onClick(Component c) {
			_midlet.showMainForm();
			
		}
   
        
      
        
      
}
