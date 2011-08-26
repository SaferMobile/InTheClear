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
import org.safermobile.clear.micro.apps.ITCMainMIDlet;
import org.safermobile.clear.micro.apps.ITCConstants;
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
public class EnableOneTouchPanicForm
        extends Dialog implements OnClickListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;
        
        private CheckBox _cbOneTouch;
        		
    	L10nResources l10n = LocaleManager.getResources();
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public EnableOneTouchPanicForm (ITCMainMIDlet midlet)
        {
                _midlet = midlet;
                setupUI();
        }
        
        public void setupUI()
        {
        	
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.ONE_TOUCH_TITLE) );
        //        setMenuText( l10n.getString(L10nConstants.keys.MENU_BACK), l10n.getString(L10nConstants.keys.MENU_NEXT));

                // Center the text.
                Label label = new Label();
                label.setLabel(l10n.getString(L10nConstants.keys.ONE_TOUCH_INFO));
        		label.setHorizontalAlignment( Graphics.LEFT );
        		append(label );
        	
        		_cbOneTouch = new CheckBox();
        		_cbOneTouch.setLabel( l10n.getString(L10nConstants.keys.ONE_TOUCH_LBL_CB) );
        		_cbOneTouch.setChecked( false );
        		append( _cbOneTouch );

        		Button btn = new Button();
        		btn.setOnClickListener(this);
        		btn.setLabel(l10n.getString(L10nConstants.keys.BUTTON_CONTINUE));
        		append (btn);
        }

      
        public boolean hasMenuBar ()
        {
        	return false;
        }
        
		public void onClick(Component c) 
		{
			persist();
			
			_midlet.showNext();
			
		}
		
      
	    private void persist ()
        {
        
    	 	_midlet.savePref(ITCConstants.PREFS_KEY_ONE_TOUCH_PANIC, _cbOneTouch.isChecked()+"");
    	 
    	 	
        }
        
}
