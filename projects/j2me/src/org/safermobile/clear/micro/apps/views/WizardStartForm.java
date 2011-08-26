package org.safermobile.clear.micro.apps.views;


import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.j4me.ui.Dialog;
import org.j4me.ui.components.Button;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.HorizontalRule;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.OnClickListener;
import org.j4me.ui.components.Picture;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WizardStartForm
        extends Dialog implements OnClickListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;

        /*
         * 
         */
        private Button[] btnLocales;
        
    	/**
    	 * The label 
    	 */
    	private Label _label = new Label();
    	
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public WizardStartForm (ITCMainMIDlet midlet)
        {
        	super();
                _midlet = midlet;
                
                // Set the title and menu.
                setTitle( LocaleManager.getResources().getString(L10nConstants.keys.SETUP_TITLE) );                
         //       setMenuText( l10n.getString(L10nConstants.keys.MENU_EXIT), l10n.getString(L10nConstants.keys.MENU_NEXT));
               
            	// Center the text.
        		_label.setHorizontalAlignment( Graphics.LEFT );

        		_label.setLabel(LocaleManager.getResources().getString(L10nConstants.keys.SETUP_INTRO));
        		
        		_label.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM));
        		
        		// Add the label to this screen.
        		append( _label );

        		btnLocales = new Button[ITCConstants.LOCALES.length];
        		
        		for (int i = 0; i < btnLocales.length; i++)
        		{
        			btnLocales[i] = new Button();
        			btnLocales[i].setOnClickListener(this);
        			btnLocales[i].setLabel(ITCConstants.LOCALES[i][1]);
        			append (btnLocales[i]);
        		}
        		
        }
        
        public boolean hasMenuBar ()
        {
        	return false;
        }
        
		public void onClick(Component c) {
			
			for (int i = 0; i < btnLocales.length; i++)
			{
				if (c == btnLocales[i])
				{
					_midlet.setLocale(ITCConstants.LOCALES[i][0]);
					
				}
			}
			
			_midlet.showNext();
			
		}
        
		protected void declineNotify() {
			_midlet.showMainForm();
		}
      
}
