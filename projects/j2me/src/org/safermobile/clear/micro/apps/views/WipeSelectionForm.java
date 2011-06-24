package org.safermobile.clear.micro.apps.views;


import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.PanicConfigMIDlet;
import org.safermobile.clear.micro.apps.PanicConstants;
import org.safermobile.clear.micro.apps.models.WipeDataType;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WipeSelectionForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private PanicConfigMIDlet _midlet;
        
        private Vector _wipeDataTypes;
        private Vector _checkBoxes;        
        
    	L10nResources l10n = LocaleManager.getResources();
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public WipeSelectionForm (PanicConfigMIDlet midlet, Vector wipeDataTypes)
        {
                _midlet = midlet;
                _wipeDataTypes = wipeDataTypes;
                
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.WIPE_SELECT_TITLE) );
                setMenuText( l10n.getString(L10nConstants.keys.MENU_BACK), l10n.getString(L10nConstants.keys.MENU_NEXT) );

                // Center the text.
                Label label = new Label();
                label.setLabel(l10n.getString(L10nConstants.keys.WIPE_MESSAGE));
        		label.setHorizontalAlignment( Graphics.LEFT );
       
        		append(label );
        	
        		_checkBoxes = new Vector();
        		CheckBox cb;
        		
        		Enumeration enumWDTs = wipeDataTypes.elements();
        		
        		while (enumWDTs.hasMoreElements())
        		{
        			WipeDataType wdt = (WipeDataType)enumWDTs.nextElement();
        			
        			cb = new CheckBox();
            		cb.setLabel(wdt.getLabel());
            		cb.setChecked(wdt.isEnabled());
            		append(cb);
            		_checkBoxes.addElement(cb);
        		}


        }

        private void persist ()
        {
        
        	for (int i = 0; i < _checkBoxes.size(); i++)
        	{
        		CheckBox cb = (CheckBox)_checkBoxes.elementAt(i);
        		WipeDataType wdt = (WipeDataType)_wipeDataTypes.elementAt(i);
        		_midlet.savePref(wdt.getKey(), cb.isChecked()+"");
        	}
        	
        }
        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
        	_midlet.showStartScreen();
        }

		protected void acceptNotify() {
			
			persist();
			
			DeviceScreen next = new PanicConfigCompleteForm(_midlet);
			_midlet.showAlert(l10n.getString(L10nConstants.keys.WIPE_TITLE), l10n.getString(L10nConstants.keys.WIPE_MESSAGE_SAVED), next);
			
			
		}
        
}
