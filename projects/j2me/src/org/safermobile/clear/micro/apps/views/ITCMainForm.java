package org.safermobile.clear.micro.apps.views;


import org.j4me.ui.Dialog;
import org.j4me.ui.components.Button;
import org.j4me.ui.components.Component;
import org.j4me.ui.components.OnClickListener;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;
import org.safermobile.clear.micro.apps.LocaleManager;

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
            
            // Set the title and menu.
            setTitle( "In The Clear" );                
    
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
