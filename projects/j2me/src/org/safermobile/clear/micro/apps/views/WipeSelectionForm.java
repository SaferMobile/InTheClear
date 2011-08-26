package org.safermobile.clear.micro.apps.views;


import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.pim.PIMException;

import org.j4me.ui.*;
import org.j4me.ui.components.*;
import org.safermobile.clear.micro.L10nResources;
import org.safermobile.clear.micro.L10nConstants;
import org.safermobile.clear.micro.apps.LocaleManager;
import org.safermobile.clear.micro.apps.ITCMainMIDlet;
import org.safermobile.clear.micro.apps.ITCConstants;
import org.safermobile.clear.micro.apps.controllers.WipeController;
import org.safermobile.clear.micro.apps.models.WipeDataType;
import org.safermobile.clear.micro.data.PIMWiper;
import org.safermobile.micro.utils.Preferences;

/**
 * Example of a <code>TextBox</code> component.
 */
public class WipeSelectionForm
        extends Dialog implements Runnable, OnClickListener
{
        /**
         * The previous screen.
         */
        private ITCMainMIDlet _midlet;
        
        private Vector _wipeDataTypes;
        private Vector _checkBoxes;        
        
    	private L10nResources l10n = LocaleManager.getResources();

        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public WipeSelectionForm (ITCMainMIDlet midlet, Vector wipeDataTypes)
        {
                _midlet = midlet;
                _wipeDataTypes = wipeDataTypes;
         
                setupUI();
        }
        
        public void setupUI ()
        {
                // Set the title and menu.
                setTitle( l10n.getString(L10nConstants.keys.WIPE_SELECT_TITLE) );
               // setMenuText( l10n.getString(L10nConstants.keys.MENU_BACK), l10n.getString(L10nConstants.keys.MENU_NEXT) );

                // Center the text.
                Label label = new Label();
                label.setLabel(l10n.getString(L10nConstants.keys.WIPE_MESSAGE));
        		label.setHorizontalAlignment( Graphics.LEFT );
       
        		append(label );
        	
        		_checkBoxes = new Vector();
        		CheckBox cb;
        		
        		Enumeration enumWDTs = _wipeDataTypes.elements();
        		
        		while (enumWDTs.hasMoreElements())
        		{
        			WipeDataType wdt = (WipeDataType)enumWDTs.nextElement();
        			
        			cb = new CheckBox();
            		cb.setLabel(wdt.getLabel());
            		cb.setChecked(wdt.isEnabled());
            		append(cb);
            		_checkBoxes.addElement(cb);
        		}

        		Button btn = new Button();
        		btn.setOnClickListener(this);
        		btn.setLabel(l10n.getString(L10nConstants.keys.BUTTON_CONTINUE));
        		append (btn);

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
        
        public boolean hasMenuBar ()
        {
        	return false;
        }
        
		public void onClick(Component c) {
			new Thread (this).start();
			
		}
		
      
		
		public void run ()
		{
			persist();
			
			WipeController wc = new WipeController();
			boolean wipePermsOk = false;
			
			try
			{
				Preferences prefs = new Preferences (ITCConstants.PANIC_PREFS_DB);
	
				String prefBool = prefs.get(ITCConstants.PREFS_KEY_WIPE_CONTACTS);
				boolean wipeContacts = (prefBool != null && prefBool.equals("true"));
				
				prefBool = prefs.get(ITCConstants.PREFS_KEY_WIPE_EVENTS);
				boolean wipeEvents = (prefBool != null && prefBool.equals("true"));
				
				prefBool = prefs.get(ITCConstants.PREFS_KEY_WIPE_PHOTOS);
				boolean wipePhotos = (prefBool != null && prefBool.equals("true"));
							
				prefBool = prefs.get(ITCConstants.PREFS_KEY_WIPE_ALL_FILES);
				boolean wipeAllFiles = (prefBool != null && prefBool.equals("true"));
				
	        	wipePermsOk = wc.checkPermissions(wipeContacts, wipeEvents, wipePhotos, wipeAllFiles);
	        	
					
			}
			catch (Exception e)
			{
				wipePermsOk = false;
			}
			finally
			{

	        	if (wipePermsOk)
	        	{
	        		_midlet.showAlert(l10n.getString(L10nConstants.keys.SETUP_TITLE), l10n.getString(L10nConstants.keys.WIPE_MESSAGE_SAVED), _midlet.getNextScreenIdx());
	        	}
	        	else
	        	{
					_midlet.showAlert(l10n.getString(L10nConstants.keys.TITLE_ERROR), l10n.getString(L10nConstants.keys.ERROR_PIM_DATA), _midlet.getCurrentScreenIdx());
	
	        	}
			}
			
		}
		
		
        
}
