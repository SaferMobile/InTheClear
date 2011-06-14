package org.j4me.ui;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import org.j4me.logging.*;

/**
 * The UI manager orchestrates the UI for a MIDlet.  There is one
 * <code>UIManager</code> object for the entire application and all of its
 * methods are static.  You must call the <code>init</code> method first.
 * <p>
 * Each screen, defined by a class derived from <code>DeviceScreen</code>, can be
 * attached to the manager.  The manager is responsible for navigating
 * between screens, getting them to paint themselves, and all other UI
 * control.
 * <p>
 * The application's appearance can be changed by providing a new theme.
 * Create a new theme class, derived from <code>Theme</code>, and then attach it
 * to the UI manager.  All the different screens will take on the colors
 * and layout defined by the new theme.
 * 
 * @see DeviceScreen
 * @see Theme
 */
public class UIManager
{
	/**
	 * The exception message text displayed when the user tries to set
	 * screens without first calling <code>init</code>.
	 */
	private static final String INIT_NOT_SET_EXCEPTION = "Must first call UIManager.init";
	
	/**
	 * The application's theme.
	 * <p>
	 * This is protected so other classes in this package can directly
	 * access it.
	 */
	private static Theme theme = new Theme();  // Default theme
	
	/**
	 * The MIDlet's <code>Display</code> object.
	 * <p>
	 * This is protected so other classes in this package can directly
	 * access it.
	 */
	private static Display display;
	
	/**
	 * The object currently shown on the device's screen.
	 */
	private static DeviceScreen current;

	/**
	 * Creates a <code>UIManager</code> implementation.  One of these should
	 * be created per MIDlet and be stored in the application's model.
	 * <p>
	 * The theme is set to the default, represented by the <code>Theme</code> class.
	 * To apply your own theme use the <code>setTheme</code> method. 
	 * <p>
	 * Note that you should ensure your application is running on MIDP 2.0+
	 * device.  Otherwise the system will throw a runtime exception that
	 * will immediately kill your MIDlet without warning.
	 * 
	 * @param midlet is the <code>MIDlet</code> class that defines this application.
	 */
	public static void init (MIDlet midlet)
	{
		if ( midlet == null )
		{
			// Unfortunately J2ME devices will just exit and will not show this.
			throw new IllegalArgumentException();
		}
		
		UIManager.display = Display.getDisplay( midlet );
	}
	
	/**
	 * Returns the theme used throughout the application.  The theme controls
	 * the color schemes and fonts used by the application.  It also defines
	 * the appearance of the title bar at the top of any screens and the
	 * menu bar at the bottom.
	 * 
	 * @return The current theme of the application.
	 */
	public static Theme getTheme ()
	{
		return theme;
	}
	
	/**
	 * Sets the theme used throughout the application.  The theme controls
	 * the color schemes and fonts used by the application.  It also defines
	 * the appearance of the title bar at the top of any screens and the
	 * menu bar at the bottom.
	 * 
	 * @param theme is the new theme for the application.
	 */
	public static void setTheme (Theme theme)
	{
		if ( theme == null )
		{
			throw new IllegalArgumentException( "Cannot set a null theme.");
		}
		
		UIManager.theme = theme;
	}

	/**
	 * Sets the current screen for the application.  This is used
	 * exclusively by the canvas class in this package.
	 * 
	 * @param canvas is the J4ME object that wraps <code>screen</code>.
	 * @param screen is the actual LCDUI <code>Displayable</code> which takes
	 *  over the device's screen.
	 * @see DeviceScreen#show()
	 */
	protected static void setScreen (DeviceScreen canvas, Displayable screen)
	{
		if ( display == null )
		{
			throw new IllegalStateException( INIT_NOT_SET_EXCEPTION );
		}
		
		if ( screen == null )
		{
			throw new IllegalArgumentException();
		}
		
		synchronized ( display )
		{
			// Deselect the current screen.
			if ( current != null )
			{
				try
				{
					current.hideNotify();
				}
				catch (Throwable t)
				{
					Log.warn("Unhandled exception in hideNotify() of " + current, t);
				}
			}
	
			// Select and set the new screen.
			current = canvas;

			try
			{
				canvas.showNotify();
			}
			catch (Throwable t)
			{
				Log.warn("Unhandled exception in showNotify() of " + current, t);
			}
			
			display.setCurrent( screen );
			
			// Issue a repaint command to the screen.
			//  This fixes problems on some phones to make sure the screen
			//  appears correctly.  For example BlackBerry phones sometimes
			//  render only a part of the screen.
			canvas.repaint();
			
			if ( Log.isDebugEnabled() )
			{
				Log.debug("Screen switched to " + canvas);
			}
		}
	}
	
	/**
	 * Returns the currently selected J4ME screen.  If no screen is set, or
	 * a non-J4ME screen is displayed, this will return <code>null</code>.
	 * <p>
	 * The application can call <code>toString</code> on the returned screen to
	 * get its name for logging purposes.
	 * 
	 * @return The currently displayed J4ME screen or <code>null</code> if none is
	 *  set.
	 */
	public static DeviceScreen getScreen ()
	{
		return current;
	}
	
	/**
	 * Gets the <code>Display</code> for this MIDlet.
	 * 
	 * @return The application's <code>Display</code>.
	 */
	public static Display getDisplay ()
	{
		if ( display == null )
		{
			throw new IllegalStateException( INIT_NOT_SET_EXCEPTION );
		}

		return display;
	}
}
