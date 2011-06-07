package org.safermobile.clear.micro.apps.screens;


import org.j4me.ui.*;
import org.j4me.ui.components.*;

/**
 * Example of a <code>TextBox</code> component.
 */
public class UserInfoForm
        extends Dialog
{
        /**
         * The previous screen.
         */
        private DeviceScreen previous;
        
        /**
         * The number box used by this example for entering phone numbers.
         */
        private TextBox phoneNumber;
        
        /**
         * A number box for entering a PIN.
         */
        private TextBox pinNumber;
        
        /**
         * Constructs a screen that shows a <code>TextBox</code> component in action.
         * 
         * @param previous is the screen to return to once this done.
         */
        public UserInfoForm (DeviceScreen previous)
        {
                this.previous = previous;
                
                // Set the title and menu.
                setTitle( "TextBox Example" );
                setMenuText( "Back", null );

                // Add the phone number box.
                phoneNumber = new TextBox();
                phoneNumber.setLabel( "Phone Number" );
                phoneNumber.setForPhoneNumber();
                phoneNumber.setMaxSize( 10 );
                append( phoneNumber );
                
                // Add the PIN number box.
                pinNumber = new TextBox();
                pinNumber.setLabel( "PIN Number" );
                pinNumber.setForNumericOnly();
                pinNumber.setPassword( true );
                append( pinNumber );
        }

        /**
         * Takes the user to the previous screen.
         */
        protected void declineNotify ()
        {
                previous.show();
        }
}
