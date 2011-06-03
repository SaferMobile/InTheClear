package org.safermobile.clear.micro.apps.screens;


import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;

/**
 * Example of a <code>TextBox</code> component.
 */
public class StartForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private DeviceScreen previous;
        

    	/**
    	 * The label 
    	 */
    	private Label label = new Label();
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public StartForm (DeviceScreen previous)
        {
                this.previous = previous;
                
                // Set the title and menu.
                setTitle( "Panic! Setup" );
                setMenuText( "Exit", "Next");
                
            	// Center the text.
        		label.setHorizontalAlignment( Graphics.LEFT );

        		// Make the label be mutliple paragraphs.
        		label.setLabel(
        				"Panic! is a mobile app designed to help improve your safety in difficult situations. It does this with two main features that you must configure."
        				+ "\n\n"
        				+ "SHOUT!   and   WIPE!"
        				+ "\n\n"
        				+ "This wizard will walk you through the setup process for both.");
        		
        		// Add the label to this screen.
        		append( label );


        }
        
        private void showShoutMenu ()
    	{
    		
    		// The first screen is a menu to choose among the example screens.
    		Menu menu = new Menu( "Panic - Shout", null );
    		
    		// Create a submenu for showing component example screens.
    		Menu menuCheckSMS = new Menu( "CheckSMSPermission", menu );
    		menu.appendSubmenu( menuCheckSMS );
    		
    		TestForm testForm = new TestForm (menuCheckSMS);
    		menuCheckSMS.appendMenuOption(testForm);
    		
    		// Attach an exit option.
    		/*
    		menu.appendMenuOption( new MenuItem()
    				{
    					public String getText ()
    					{
    						return "Exit";
    					}

    					public void onSelection ()
    					{
    						PanicConfigMIDlet.this.notifyDestroyed();
    					}
    				} );
    		*/
    		
    		// Show the menu.
    		menu.show();
    	}

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
                previous.show();
        }
}
