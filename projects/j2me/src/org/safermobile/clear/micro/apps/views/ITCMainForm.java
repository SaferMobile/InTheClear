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
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;

/**
 * Example of a <code>TextBox</code> component.
 */
public class ITCMainForm
        extends Dialog implements OnClickListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;

    	/**
    	 * The label 
    	 */
    	
    	L10nResources l10n = LocaleManager.getResources();
        
    	private Button btnPanic, btnMsg, btnWipe, btnWizard;
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public ITCMainForm (ITCMainMIDlet midlet)
        {
        	super();
                _midlet = midlet;
                
       		
    		/* 
    		 formMain.appendMenuOption("One-Touch Panic", new org.safermobile.clear.micro.apps.views.StartPanicForm(this));
    		 formMain.appendMenuOption("Send Emergency Message", new org.safermobile.clear.micro.apps.views.ShoutManualForm(this));
    		 formMain.appendMenuOption("Data Wipe", new org.safermobile.clear.micro.apps.views.WipeManualForm(this));
    		 formMain.appendMenuOption("Setup Wizard", new org.safermobile.clear.micro.apps.views.WipeManualForm(this));
    		 */
                // Set the title and menu.
                setTitle( "In The Clear" );                
         //       setMenuText( l10n.getString(L10nConstants.keys.MENU_EXIT), l10n.getString(L10nConstants.keys.MENU_NEXT));
            

        		btnPanic = new Button();
        		btnPanic.setOnClickListener(this);
        		btnPanic.setLabel("One-touch Panic");
        		append (btnPanic);
        		
        		btnMsg = new Button();
        		btnMsg.setOnClickListener(this);
        		btnMsg.setLabel("Emergency Message");
        		append (btnMsg);
        		
        		btnWipe = new Button();
        		btnWipe.setOnClickListener(this);
        		btnWipe.setLabel("Data Wipe");
        		append (btnWipe);
        		
        		btnWizard = new Button();
        		btnWizard.setOnClickListener(this);
        		btnWizard.setLabel("Setup Wizard");
        		append (btnWizard);
        }
        
        public boolean hasMenuBar ()
        {
        	return false;
        }
        
		public void onClick(Component c) {
			
			if (c == btnPanic)
			{
				new StartPanicForm(_midlet).show();
			}
			else if (c == btnMsg)
			{
				new ShoutManualForm(_midlet).show();
			}
			else if (c == btnWipe)
			{
				new WipeManualForm(_midlet).show();
			}
			else
			{
				_midlet.showStartScreen();
			}
			
			
		}
        
      
}
