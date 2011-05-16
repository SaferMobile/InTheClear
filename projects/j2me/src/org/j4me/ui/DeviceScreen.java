package org.j4me.ui;

import java.util.*;
import javax.microedition.lcdui.*;
import org.j4me.logging.*;

/**
 * The <code>DeviceScreen</code> class is a base class for any screen that needs complete
 * control over how it is painted.  It is based on and similar to the MIDP
 * <code>Canvas</code> class.
 * <p>
 * This class removes the following methods from the MIDP <code>Canvas</code> class:
 * <ul>
 *  <li><code>isDoubleBuffered</code> - This class is always double buffered.
 *  <li><code>hasPointerEvents</code> and <code>hasPointerMotionEvents</code> - There is
 *      no use for this method.  Implement the pointer event methods and
 *      if the device has no pointer they will be ignored.  
 *  <li> - Same reason as <code>hasPointerEvents</code>.
 *  <li><code>hasRepeatEvents</code> - This class always has key repeat events.
 *  <li><code>getKeyName</code> - The application should define the names to make them
 *      consistent across all devices.
 *  <li><code>getKeyCode</code> - The key code is passed into the key event methods.
 *  <li><code>getGameAction</code> - The game action is passed into the key event methods.
 *  <li><code>sizeChanged</code> - This method is notoriously buggy and applications
 *      should use <code>getWidth</code> and <code>getHeight</code> instead.
 *  <li><code>getTicker</code> and <code>setTicker</code> - The ticker functionality has
 *      not been implemented.
 *  <li><code>addCommand</code>, <code>removeCommand</code>, and <code>setCommandListener</code> -
 *      Menu options have been replaced with something that works across all
 *      MIDP 2.0 devices.  There are left and right menu options.  See
 *      <code>setMenuText</code> for details.
 * </ul>
 * 
 * @see javax.microedition.lcdui.Canvas
 */
public abstract class DeviceScreen
{
	/**
	 * Constant for the <code>LEFT</code> game action.
	 */
	public static final int LEFT = -1 * javax.microedition.lcdui.Canvas.LEFT;

	/**
	 * Constant for the <code>RIGHT</code> game action.
	 */
	public static final int RIGHT = -1 * javax.microedition.lcdui.Canvas.RIGHT;

	/**
	 * Constant for the <code>UP</code> game action.
	 */
	public static final int UP = -1 * javax.microedition.lcdui.Canvas.UP;

	/**
	 * Constant for the <code>DOWN</code> game action.
	 */
	public static final int DOWN = -1 * javax.microedition.lcdui.Canvas.DOWN;

	/**
	 * Constant for the <code>FIRE</code> game action.
	 */
	public static final int FIRE = -1 * javax.microedition.lcdui.Canvas.FIRE;

	/**
	 * Constant for the general purpose "A" game action.
	 */
	public static final int GAME_A = -1 * javax.microedition.lcdui.Canvas.GAME_A;

	/**
	 * Constant for the general purpose "B" game action.
	 */
	public static final int GAME_B = -1 * javax.microedition.lcdui.Canvas.GAME_B;

	/**
	 * Constant for the general purpose "C" game action.
	 */
	public static final int GAME_C = -1 * javax.microedition.lcdui.Canvas.GAME_C;

	/**
	 * Constant for the general purpose "D" game action.
	 */
	public static final int GAME_D = -1 * javax.microedition.lcdui.Canvas.GAME_D;

	/**
	 * <code>keyCode</code> for ITU-T key 0.
	 * <p>
	 * Constant value 48 is set to <code>KEY_NUM0</code>.
	 */
	public static final int KEY_NUM0 = javax.microedition.lcdui.Canvas.KEY_NUM0;

	/**
	 * <code>keyCode</code> for ITU-T key 1.
	 * <p>
	 * Constant value 49 is set to <code>KEY_NUM1</code>.
	 */
	public static final int KEY_NUM1 = javax.microedition.lcdui.Canvas.KEY_NUM1;

	/**
	 * <code>keyCode</code> for ITU-T key 2.
	 * <p>
	 * Constant value 50 is set to <code>KEY_NUM2</code>.
	 */
	public static final int KEY_NUM2 = javax.microedition.lcdui.Canvas.KEY_NUM2;

	/**
	 * <code>keyCode</code> for ITU-T key 3.
	 * <p>
	 * Constant value 51 is set to <code>KEY_NUM3</code>.
	 */
	public static final int KEY_NUM3 = javax.microedition.lcdui.Canvas.KEY_NUM3;

	/**
	 * <code>keyCode</code> for ITU-T key 4.
	 * <p>
	 * Constant value 52 is set to <code>KEY_NUM4</code>.
	 */
	public static final int KEY_NUM4 = javax.microedition.lcdui.Canvas.KEY_NUM4;

	/**
	 * <code>keyCode</code> for ITU-T key 5.
	 * <p>
	 * Constant value 53 is set to <code>KEY_NUM5</code>.
	 */
	public static final int KEY_NUM5 = javax.microedition.lcdui.Canvas.KEY_NUM5;

	/**
	 * <code>keyCode</code> for ITU-T key 6.
	 * <p>
	 * Constant value 54 is set to <code>KEY_NUM6</code>.
	 */
	public static final int KEY_NUM6 = javax.microedition.lcdui.Canvas.KEY_NUM6;

	/**
	 * <code>keyCode</code> for ITU-T key 7.
	 * <p>
	 * Constant value 55 is set to <code>KEY_NUM7</code>.
	 */
	public static final int KEY_NUM7 = javax.microedition.lcdui.Canvas.KEY_NUM7;

	/**
	 * <code>keyCode</code> for ITU-T key 8.
	 * <p>
	 * Constant value 56 is set to <code>KEY_NUM8</code>.
	 */
	public static final int KEY_NUM8 = javax.microedition.lcdui.Canvas.KEY_NUM8;

	/**
	 * <code>keyCode</code> for ITU-T key 9.
	 * <p>
	 * Constant value 57 is set to <code>KEY_NUM9</code>.
	 */
	public static final int KEY_NUM9 = javax.microedition.lcdui.Canvas.KEY_NUM9;

	/**
	 * <code>keyCode</code> for ITU-T key "pound" (#).
	 * <p>
	 * Constant value 35 is set to <code>KEY_POUND</code>.
	 */
	public static final int KEY_POUND = javax.microedition.lcdui.Canvas.KEY_POUND;

	/**
	 * <code>keyCode</code> for ITU-T key "star" (*).
	 * <p>
	 * Constant value 42 is set to <code>KEY_STAR</code>.
	 */
	public static final int KEY_STAR = javax.microedition.lcdui.Canvas.KEY_STAR;
	
	/**
	 * Constant for the left soft menu key found on MIDP 2.0 devices.
	 */
	public static final int MENU_LEFT = -21;
	
	/**
	 * Constant for the right soft menu key found on MIDP 2.0 devices.
	 */
	public static final int MENU_RIGHT = -22;
	
	/**
	 * The actual <code>Canvas</code> object that controls the device's screen.
	 * This object wraps it.
	 */
	private final CanvasWrapper slave;

	/**
	 * When <code>false</code> this class will paint the menu bar at the bottom
	 * of the screen.  When <code>true</code> it will not. 
	 */
	private boolean fullScreenMode = false;
	
	/**
	 * What is written as a title bar for this canvas.  When this is <code>null</code>
	 * no title bar will be written.  To show the header without any text
	 * set this to the empty string "".
	 */
	private String title;
	
	/**
	 * The text for the left menu button.  This is the negative side used
	 * for canceling and going back to previous screens.
	 */
	private String leftMenu;
	
	/**
	 * The text for the right menu button.  This is the positive side used
	 * for accepting input, invoking menus, and moving forward in the
	 * application's state.
	 */
	private String rightMenu;
	
	/**
	 * Implicitly called by derived classes to setup a new J4ME canvas.
	 */
	public DeviceScreen ()
	{
		// Create a wrapper around the canvas.
		slave = new CanvasWrapper( this );
	}
	
	/**
	 * Returns the LCDUI <code>Canvas</code> wrapped by this screen.  This is
	 * required for some APIs.
	 * 
	 * @return The <code>javax.microedition.lcdui.Canvas</code> wrapped by this screen.
	 */
	public Canvas getCanvas ()
	{
		return slave;
	}
	
	/**
	 * Makes this object take over the device's screen.
	 * <p>
	 * The previous screen will have its <code>hideNotify</code> method called.
	 * Then this screen's <code>showNotify</code> method will be invoked followed
	 * by the <code>paint</code> method.
	 */
	public void show ()
	{
		// Set the wrapped canvas as the current screen.
		UIManager.setScreen( this, slave );
	}
	
	/**
	 * Checks if this screen is actually visible on the display.  In
	 * order for a screen to be visible, all of the following must be true:
	 * the MIDlet must be running in the foreground, the screen must be the
	 * current one, and the screen must not be obscured by a system screen.
	 * 
	 * @return <code>true</code> if this screen is currently visible; <code>false</code>
	 *  otherwise.
	 */
	public boolean isShown ()
	{
		if ( UIManager.getScreen() == this )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * The implementation calls <code>showNotify()</code> immediately prior to
	 * this <code>Canvas</code> being made visible on the display.  <code>Canvas</code>
	 * subclasses may override this method to perform tasks before being
	 * shown, such as setting up animations, starting timers, etc.  The
	 * default implementation of this method in class <code>Canvas</code> is empty.
	 */
	public void showNotify ()
	{
	}
	
	/**
	 * The implementation calls <code>hideNotify()</code> shortly after the
	 * <code>Canvas</code> has been removed from the display.  <code>Canvas</code>
	 * subclasses may override this method in order to pause animations,
	 * revoke timers, etc.  The default implementation of this method in
	 * class <code>Canvas</code> is empty.
	 */
	public void hideNotify ()
	{
	}

	/**
	 * Shows or hides the menu bar at the bottom of the screen.
	 * 
	 * @param mode is <code>true</code> if the <code>DeviceScreen</code> is to be in full
	 *  screen mode, <code>false</code> otherwise.
	 */
	public void setFullScreenMode (boolean mode)
	{
		this.fullScreenMode = mode;
	}
	
	/**
	 * Returns if the title bar and menu bar are hidden or not.
	 *  
	 * @return <code>true</code> if in full screen mode (title bar and menu
	 *  bar are hidden); <code>false</code> otherwise.
	 */
	public boolean isFullScreenMode ()
	{
		return fullScreenMode;
	}
	
	/**
	 * Gets the title of this screen.  If this returns <code>null</code> the
	 * screen has no title.
	 * 
	 * @return The title of this screen.
	 */
	public String getTitle ()
	{
		return title;
	}

	/**
	 * Sets the title of this screen.  The default is <code>null</code> meaning no
	 * title.
	 * <p>
	 * For the title to be visible full screen mode must be off.
	 * This can be done with the <code>setFullScreenMode</code> method.
	 * 
	 * @param title is the new title for the screen.
	 */
	public void setTitle (String title)
	{
		this.title = title;
		
		// Notify the slave screen.
		slave.setTitle( title );
		slave.repaint();
	}
	
	/**
	 * Returns if this screen displays a title bar at the top.  Title
	 * bars require both setting a title (through <code>setTitle</code>) and
	 * that full screen mode is off (<code>setFullScreenMode(false)</code>).
	 * 
	 * @return <code>true</code> if the screen has a title bar; <code>false</code>
	 *  if it does not.
	 */
	public boolean hasTitleBar ()
	{
		// Full screen mode off?
		if ( fullScreenMode == false )
		{
			// There is some title?
			if ( title != null )
			{
				// Device supports title bars?
				if ( slave.supportsTitleBar() )
				{
					return true;
				}
			}
		}
		
		// If we made it here no title bar should be displayed.
		return false;
	}
	
	/**
	 * Returns the title of this screen.
	 * 
	 * @return The title of this screen.  If no title is set this returns
	 *  the empty string "".
	 */
	public String toString ()
	{
		if ( title == null )
		{
			return getClass().getName();
		}
		else
		{
			return title;
		}
	}

	/**
	 * Sets the menu bar text.
	 * <p>
	 * For the menu to be visible full screen mode must be off.
	 * This can be done with the <code>setFullScreenMode</code> method.
	 * 
	 * @param left is the text for the negative menu option or <code>null</code>
	 *  to remove the button.  Negative menu options are things like canceling
	 *  a form and moving back to a previous screen.
	 * @param right is the text for the positive menu option or <code>null</code>
	 *  to remove the button.  Positive menu options are things like accepting
	 *  a form, advancing to the next screen, or displaying a menu.
	 * @see #declineNotify()
	 * @see #acceptNotify()
	 */
	public void setMenuText (String left, String right)
	{
		this.leftMenu = left;
		this.rightMenu = right;
		
		// Notify the slave screen.
		slave.setMenuText( left, right );
		slave.repaint();
	}
	
	/**
	 * Returns the text for the left menu button.  The left menu button is
	 * for negative operations such as canceling a form and going back to
	 * a previous screen.
	 * 
	 * @return The text for the left menu button.  If there is no button
	 *  this returns <code>null</code>.
	 */
	public String getLeftMenuText ()
	{
		return leftMenu;
	}
	
	/**
	 * Returns the text for the right menu button.  The right menu button is
	 * for positive operations such as accepting a form and opening a menu.
	 * 
	 * @return The text for the right menu button.  If there is no button
	 *  this returns <code>null</code>.
	 */
	public String getRightMenuText ()
	{
		return rightMenu;
	}
	
	/**
	 * Returns if this screen displays a menu bar at the bottom.  Menu bars
	 * require both setting at least one menu option (through
	 * <code>setMenuText</code>) and that full screen mode is off
	 * (<code>setFullScreenMode(false)</code>).
	 * 
	 * @return <code>true</code> if the screen has a menu bar; <code>false</code>
	 *  if it does not.
	 */
	public boolean hasMenuBar ()
	{
		// Full screen mode off?
		if ( fullScreenMode == false )
		{
			// There is some menu text?
			if ( (leftMenu != null) || (rightMenu != null) )
			{
				// Device supports menus?
				if ( slave.supportsMenuBar() )
				{
					return true;
				}
			}
		}
		
		// If we made it here no menu bar should be displayed.
		return false;
	}
	
	/**
	 * Returns the width of the usuable portion of this canvas.  The usable
	 * portion excludes anything on the sides of the screen such as scroll
	 * bars.
	 * 
	 * @return The number of pixels wide the usable portion of the canvas is.
	 */
	public int getWidth ()
	{
		return slave.getWidth();
	}
	
	/**
	 * Returns the height of the usuable portion of this canvas.  The usable
	 * portion excludes the title area and menu bar unless this canvas has been
	 * set to full screen mode.
	 * 
	 * @return The number of pixels high the usable portion of the canvas is.
	 */
	public int getHeight ()
	{
		Theme theme = UIManager.getTheme();
		
		// Get the height of the entire canvas.
		int height = getScreenHeight();

		// Remove the height of the title bar. 
		if ( hasTitleBar() )
		{
			height -= theme.getTitleHeight();
		}
			
		// Remove the height of the menu bar.
		if ( hasMenuBar() )
		{
			height -= theme.getMenuHeight();
		}
		
		return height;
	}
	
	/**
	 * Gets the width of the entire screen in pixels.
	 * <p>
	 * <i>Platform bug note.</i>  Motorola and early Nokia phones return the
	 * incorrect size until after the first screen has actually been displayed.
	 * So, for example, calling this from a constructor before any screen has
	 * been displayed will give incorrect data.  The workaround is to put up
	 * another screen first, such as a splash screen.
	 * 
	 * @return The number of pixels wide the entire screen is.
	 */
	public int getScreenWidth ()
	{
		return slave.getWidth();
	}

	/**
	 * Gets the height of the entire screen in pixels.  This includes
	 * the title area at the top of the screen and menu bar at the bottom.
	 * Use <code>getHeight</code> to get the actual usable area of the canvas.
	 * <p>
	 * <i>Platform bug note.</i>  Motorola and early Nokia phones return the
	 * incorrect size until after the first screen has actually been displayed.
	 * So, for example, calling this from a constructor before any screen has
	 * been displayed will give incorrect data.  The workaround is to put up
	 * another screen first, such as a splash screen.
	 * 
	 * @return The number of pixels high the entire screen is.
	 */
	public int getScreenHeight ()
	{
		return slave.getHeight();
	}

	/**
	 * Method called by the framework when the user clicks on the left menu button.
	 * The default implementation does nothing.
	 * 
	 * @see #getLeftMenuText()
	 */
	protected void declineNotify ()
	{
	}
	
	/**
	 * Method called by the framework when the user clicks the right menu button.
	 * The default implementation does nothing.
	 * 
	 * @see #getRightMenuText()
	 */
	protected void acceptNotify ()
	{
	}
	
	/**
	 * Called when a key is pressed.  It can be identified using the
	 * constants defined in this class.
	 * <p>
	 * Special keys, like the joystick and menu buttons, have negative
	 * values.  Input characters, like the number keys, are positive.
	 * <p>
	 * Unlike the MIDP <code>Canvas</code> class which requires tranlation of game keys
	 * this implementation does not.  The <code>keyCode</code> value will match the
	 * constants of this class.
	 * 
	 * @param keyCode is the key code of the key that was pressed.
	 *  Negative values are special keys like the joystick and menu buttons.
	 */
	protected void keyPressed (int keyCode)
	{
	}

	/**
	 * Called when a key is repeated (held down).  It can be identified using the
	 * constants defined in this class.
	 * <p>
	 * Special keys, like the joystick and menu buttons, have negative
	 * values.  Input characters, like the number keys, are positive.
	 * <p>
	 * Unlike the MIDP <code>Canvas</code> class which requires tranlation of game keys
	 * this implementation does not.  The <code>keyCode</code> value will match the
	 * constants of this class.
	 * <p>
	 * Also unlike the MIDP <code>Canvas</code> class, this implementation always
	 * supports this method and does so the same across all devices.
	 * 
	 * @param keyCode is the key code of the key that was held down.
	 *  Negative values are special keys like the joystick and menu buttons.
	 */
	protected void keyRepeated (int keyCode)
	{
	}
	
	/**
	 * Called when a key is released.  It can be identified using the
	 * constants defined in this class.
	 * <p>
	 * Special keys, like the joystick and menu buttons, have negative
	 * values.  Input characters, like the number keys, are positive.
	 * <p>
	 * Unlike the MIDP <code>Canvas</code> class which requires tranlation of game keys
	 * this implementation does not.  The <code>keyCode</code> value will match the
	 * constants of this class.
	 * 
	 * @param keyCode is the key code of the key that was released.
	 *  Negative values are special keys like the joystick and menu buttons.
	 */
	protected void keyReleased (int keyCode)
	{
	}
	
	/**
	 * Called when the pointer is pressed.
	 * 
	 * @param x is the horizontal location where the pointer was pressed
	 *  relative to the canvas area (i.e. does not include the title or
	 *  menu bars).
	 * @param y is the vertical location where the pointer was pressed
	 *  relative to the canvas area (i.e. does not include the title or
	 *  menu bars).
	 */
	protected void pointerPressed (int x, int y)
	{
	}
	
	/**
	 * Called when the pointer is released.
	 * 
	 * @param x is the horizontal location where the pointer was pressed
	 *  relative to the canvas area (i.e. does not include the title or
	 *  menu bars).
	 * @param y is the vertical location where the pointer was pressed
	 *  relative to the canvas area (i.e. does not include the title or
	 *  menu bars).
	 */
	protected void pointerReleased (int x, int y)
	{
	}
	
	/**
	 * Called when the pointer is dragged.
	 * 
	 * @param x is the horizontal location where the pointer was pressed
	 *  relative to the canvas area (i.e. does not include the title or
	 *  menu bars).
	 * @param y is the vertical location where the pointer was pressed
	 *  relative to the canvas area (i.e. does not include the title or
	 *  menu bars).
	 */
	protected void pointerDragged (int x, int y)
	{
	}
		
	/**
	 * Requests a repaint for the entire <code>Canvas</code>. The effect is identical to
	 * <code>repaint(0, 0, getWidth(), getHeight());</code>. 
	 */
	public void repaint ()
	{
		// Make sure the wrapper is in full-screen mode.
		//   There is a bug on some implementations that turns the screen
		//   off full-screen mode.  This can be seen when going to a
		//   javax.microedition.lcdui.TextBox screen and back to this one.
		slave.setFullScreenMode( true );
		
		// Do the repaint.
		slave.repaint();
	}
	
	/**
	 * Requests a repaint for the specified region of the <code>Canvas<code>.  Calling this
	 * method may result in subsequent call to <code>paint()</code>, where the passed
	 * <code>Graphics</code> object's clip region will include at least the specified region.
	 * <p>
	 * If the canvas is not visible, or if width and height are zero or less, or if
	 * the rectangle does not specify a visible region of the display, this call has
	 * no effect.
	 * <p>
	 * The call to <code>paint()</code> occurs asynchronously of the call to <code>repaint()</code>.
	 * That is, <code>repaint()</code> will not block waiting for <code>paint()</code> to finish.  The
	 * <code>paint()</code> method will either be called after the caller of <code>repaint()</code> returns
	 * to the implementation (if the caller is a callback) or on another thread entirely.
	 * <p>
	 * To synchronize with its <code>paint()</code> routine applications can use
	 * <code>serviceRepaints()</code>, or they can code explicit synchronization into their
	 * <code>paint()</code> routine.
	 * <p>
	 * The origin of the coordinate system is above and to the left of the pixel in
	 * the upper left corner of the displayable area of the <code>Canvas</code>. The 
	 * X-coordinate is positive right and the Y-coordinate is positive downwards.
	 * 
	 * @param x is the x coordinate of the rectangle to be repainted.
	 * @param y is the y coordinate of the rectangle to be repainted.
	 * @param width is the width of the rectangle to be repainted.
	 * @param height is the height of the rectangle to be repainted.
	 * @see Canvas#serviceRepaints()
	 */
	public void repaint (int x, int y, int width, int height)
	{
		if ( hasTitleBar() )
		{
			// Offset the user's y by the height of the title bar.
			Theme theme = UIManager.getTheme();
			int titleHeight = theme.getTitleHeight();
			y += titleHeight;
		}
		
		slave.repaint( x, y, width, height );
	}
	
	/**
	 * Forces any pending repaint requests to be serviced immediately.  This
	 * method blocks until the pending requests have been serviced.  If there
	 * are no pending repaints, or if this canvas is not visible on the
	 * display, this call does nothing and returns immediately.
	 */
	public void serviceRepaints ()
	{
		slave.serviceRepaints();
	}
	
	/**
	 * Paints the background of the main section of the screen.  This includes
	 * everything except for the title bar at the top and menu bar at the bottom.
	 * However, if this canvas is in full screen mode, then this method paints the entire
	 * screen.
	 * <p>
	 * After this method is called, the <code>paintCanvas</code> method will be.
	 * <p>
	 * Override this method to change the background for just this screen.  Override
	 * <code>Theme.paintBackground</code> to change the background for the entire application.
	 * 
	 * @param g is the <code>Graphics</code> object to paint with.
	 * @see #paint(Graphics)
	 */
	protected void paintBackground (Graphics g)
	{
		UIManager.getTheme().paintBackground( g );
	}
	
	/**
	 * Paints the main section of the screen.  This includes everything except
	 * for the title bar at the top and menu bar at the bottom.  However,
	 * if this canvas is in full screen mode, then this method paints the entire
	 * screen.
	 * <p>
	 * Before this method is called, the <code>paintBackground</code> method will be.
	 * Any painting done here will go over the background.
	 * <p>
	 * Override this method to paint the main area of the screen.
	 * 
	 * @param g is the <code>Graphics</code> object to paint with.
	 * @see #paintBackground(Graphics)
	 */
	protected abstract void paint (Graphics g);

	/**
	 * Paints the title bar of the canvas.  This method is called only
	 * when the title has been set through <code>setTitle</code> and the canvas
	 * is not in full screen mode.
	 * <p>
	 * Override this method to change the appearance of the title bar
	 * for just this canvas.  To change them for the entire application,
	 * override <code>Theme.paintTitleBar</code>.
	 * 
	 * @param g is the <code>Graphics</code> object to paint with.
	 * @param title is the text for the title bar as defined by the
	 *  canvas class.
	 * @param width is the width of the title bar in pixels.
	 * @param height is the height of the title bar in pixels.
	 */
	protected void paintTitleBar (Graphics g, String title, int width, int height)
	{
		UIManager.getTheme().paintTitleBar( g, title, width, height );
	}

	/**
	 * Paints the menu bar at the bottom of the canvas.  This method is
	 * not called if the canvas is in full screen mode.
	 * <p>
	 * Override this method to change the appearance or functionality of
	 * the menu for just this canvas.  To change them for the entire
	 * application, override <code>Theme.paintMenuBar</code>.  Be careful not
	 * to write strings that are too long and will not fit on the menu bar.
	 * 
	 * @param g is the <code>Graphics</code> object to paint with.
	 * @param left is the text to write on the left side of the menu bar.
	 *  The left side is associated with dimissing input such as a
	 *  "Cancel" button.
	 * @param highlightLeft is <code>true</code> if the menu text <code>left</code>
	 *  should be highlighted to indicate the left menu button is currently
	 *  pressed.
	 * @param right is the text to write on the right side of the menu bar.
	 *  The right side is associated with accepting input such as an
	 *  "OK" button.
	 * @param highlightRight is <code>true</code> if the menu text <code>right</code>
	 *  should be highlighted to indicate the right menu button is currently
	 *  pressed.
	 * @param width is the width of the menu bar in pixels.
	 * @param height is the height of the menu bar in pixels.
	 */
	protected void paintMenuBar (Graphics g,
			String left, boolean highlightLeft,
			String right, boolean highlightRight, 
			int width, int height)
	{
		UIManager.getTheme().paintMenuBar( g, left, highlightLeft, right, highlightRight, width, height );
	}
	
	/**
	 * Returns if the clip area of <code>g</code> intersects the given rectangle.
	 * 
	 * @param g is the current <code>Graphics</code> object for a <code>paint</code>
	 *  operation.
	 * @param x is the pixel at the left edge of the rectangle.
	 * @param y is the pixel at the top edge of the rectangle.
	 * @param w is width of the rectangle in pixels.
	 * @param h is height of the rectangle in pixels.
	 * @return <code>true</code> if the rectangle is to be painted by <code>g</code>;
	 *  <code>false</code> otherwise.
	 */
	public static boolean intersects (Graphics g, int x, int y, int w, int h)
	{
		// Get the graphic's clip dimensions.
		int gx = g.getClipX();
		int gy = g.getClipY();
		int gw = g.getClipWidth();
		int gh = g.getClipHeight();
		
		// Make the width/height into the right/bottom.
		gw += gx;
		gh += gy;
		w += x;
		h += y;
		
		// Check for intersections.
		//  (overflow || intersect)
		boolean intersects =
			(w < x || w > gx) &&
			(h < y || h > gy) &&
			(gw < gx || gw > x) &&
			(gh < gy || gh > y);
		return intersects;
	}
}

/**
 * Wraps the LCDUI's <code>Canvas</code> class.  It masks differences between
 * platforms.  It is used as the actual <code>Screen</code> for all J4ME screens.
 */
final class CanvasWrapper
	extends javax.microedition.lcdui.Canvas
	implements CommandListener
{
	/**
	 * The interval, in milliseconds, between signaling repeat events.
	 * It can be thought of as events raised per second by taking 1000
	 * and dividing it by this number (e.g. 1000 / 250 = 4 events per second).
	 */
	private static final short REPEAT_PERIOD = 200;

	/**
	 * When <code>true</code> this is running on a BlackBerry device.  When
	 * <code>false</code> it is not.
	 * <p>
	 * BlackBerry phones have limited native MIDlet support.  The biggest
	 * issue is they do not have the left and right menu buttons on other
	 * MIDP 2.0 phones.  Instead they have a "Menu" key and a "Return"
	 * key that are big parts of the overall BlackBerry experience.  To
	 * capture these key events, and make the application consistent with
	 * the BlackBerry experience, we need to create a standard LCDUI menu
	 * which the BlackBerry framework ties to these buttons.
	 */
	private static boolean blackberry;
	
	/**
	 * When <code>true</code> this is running in IBM's J9 JVM (also known as
	 * WEME or WebSphere Everyplace Micro Edition).  When <code>false</code>
	 * it is not.
	 * <p>
	 * For Windows Mobile and Palm phones the only reliable J2ME JVM is
	 * IBM's J9 JVM.  There are some other good implementations that
	 * come with some phones, such as the Motorola Q which uses Motorola's
	 * JVM.
	 * <p>
	 * J9 does not support detecting soft menu buttons.  We need to use
	 * its title area and menu bar instead of our own to capture these
	 * events.  This also hooks into the design of these devices more
	 * closely so the application resembles others on the phone.
	 */
	private static boolean ibmJ9;
	
	/**
	 * When <code>true</code> this is running on Tao's JVM.  When <code>false</code>
	 * it is not.
	 * <p>
	 * The Tao JVM runs on Windows Mobile and behaves similarly to IBM's J9.
	 */
	private static boolean tao;
	
	/**
	 * The screen that uses this object for screen operations.
	 * <p>
	 * This should only be <code>null</code> when using a dummy screen to get
	 * the dimensions.  For more information see the <code>sizeChanged</code>
	 * method's comments for more information.
	 */
	private final DeviceScreen master;
	
	/**
	 * Executes the <code>keyRepeated</code> job every <code>REPEAT_PERIOD</code>.
	 * This value will be <code>null</code> if the user is not holding down
	 * any keys.
	 * <p>
	 * Note we have to create new <code>Timer</code> objects every time we
	 * repeat keys.  It does not work to create a timer once here and
	 * then try to reuse it.  <i>To prevent multiple timer objects
	 * synchronize access on <code>this</code> when dealing with
	 * <code>keyRepeatTimer</code>.</i>
	 */
	private Timer keyRepeatTimer = null;
	
	/**
	 * If <code>true</code> the left menu button text should be highlighted.  This
	 * happens when the user presses the left menu button to indicate the
	 * event was received.  Normally this will be <code>false</code> showing no
	 * menu work is in progress.
	 */
	private boolean highlightLeftMenu;
	
	/**
	 * If <code>true</code> the right menu button text should be highlighted.  This
	 * happens when the user presses the right menu button to indicate the
	 * event was received.  Normally this will be <code>false</code> showing no
	 * menu work is in progress.
	 */
	private boolean highlightRightMenu;

	/**
	 * When we do not paint the menu bar, such as with BlackBerries and IBM's
	 * J9 JVM, this contains the left menu choice.  Otherwise this will be
	 * <code>null</code>.
	 * 
	 * @see #lcduiRightMenuCommand
	 */
	private Command lcduiLeftMenuCommand;
	
	/**
	 * When we do not paint the menu bar, such as with BlackBerries and IBM's
	 * J9 JVM, this contains the right menu choice.  Otherwise this will be
	 * <code>null</code>.
	 * 
	 * @see #lcduiLeftMenuCommand
	 */
	private Command lcduiRightMenuCommand;
		
	/**
	 * Static initializer for data that is shared by all screens.
	 */
	static
	{
        String platform = System.getProperty( "microedition.platform" );
        platform = platform.toLowerCase();
        
		// Check if running on a BlackBerry.
		try
		{
			Class.forName( "net.rim.device.api.ui.UiApplication" );
			blackberry = true;
		}
		catch (Throwable e)  // ClassNotFoundException, NoClassDefFoundError
		{
			blackberry = false;
		}
		
		// Check if running on IBM's J9 JVM.
		try
		{
			Class.forName( "java.lang.J9VMInternals" );
			ibmJ9 = true;
		}
		catch (Throwable e)  // ClassNotFoundException, NoClassDefFoundError
		{
			ibmJ9 = false;
		}
		
		// Check if running on Tao's JVM.
		if ( platform.indexOf("intent") > -1 )
		{
			tao = true;
		}
		else
		{
			tao = false;
		}
	}
	
	/**
	 * Constructs a wrapper for a <code>Canvas</code>.
	 * 
	 * @param master is the J4ME screen that uses this object. 
	 */
	public CanvasWrapper (DeviceScreen master)
	{
		this.master = master;
		
		// Always remove the menu bar and replace with our own.
		//  There are special cases, like BlackBerry's and IBM's J9
		//  on Windows Mobile, where this is not true.  These are
		//  handled by setMenuText() and other methods.
		setFullScreenMode( true );
		
		// Register for getting LCDUI menu commands.
		setCommandListener( this );
	}

	/**
	 * Causes the <code>keyRepeated</code> method to fire on devices that do
	 * not natively support it.
	 */
	private final class KeyRepeater
		extends TimerTask
	{
		private int key;
		
		public KeyRepeater (int key)
		{
			this.key = key;
		}
		
		public void run ()
		{
			if ( master.isShown() )
			{
				try
				{
					master.keyRepeated( key );
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * If <code>keyRepeatTimer</code> is running, this method stops it.
	 */
	private synchronized void stopRepeatTimer ()
	{
		if ( keyRepeatTimer != null )
		{
			keyRepeatTimer.cancel();
			keyRepeatTimer = null;
		}
	}
	
	/**
	 * Called when the user presses any key.  This method checks
	 * for joystick movements which work the Yardage Anywhere
	 * cursor.
	 * 
	 * @param key is code of the key that was pressed.
	 */
	protected void keyPressed (int key)
	{
		int translatedKey = translateKeyCode( key );

		// Stop simulating key repeated events.
		//   Holding a key down, then pressing another key, will stop
		//   the first key's keyReleased() method from being called on
		//   some phones like Sony Ericssons.  Kill the repeat timer
		//   here so that key no longer receives keyRepeat() events.
		stopRepeatTimer();
		
		// Notify the master.
		master.keyPressed( translatedKey );
		
		// If this is a menu key raise an event.
		if ( translatedKey == DeviceScreen.MENU_LEFT )
		{
			// Highlight the menu option immediately.
			if ( master.hasMenuBar() )
			{
				highlightLeftMenu = true;
				repaintMenuBar( true );
			}
			
			// Raise a menu event.
			master.declineNotify();
		}
		else if ( translatedKey == DeviceScreen.MENU_RIGHT )
		{
			// Highlight the menu option immediately.
			if ( master.hasMenuBar() )
			{
				highlightRightMenu = true;
				repaintMenuBar( true );
			}
			
			// Raise a menu event.
			master.acceptNotify();
		}
		
		// Do not forward the key event!
		//  super.keyPressed and .keyReleased can cause platform-specific
		//  and often undesirable behavior.  For example on the Sony Ericsson
		//  w810i they can turn on the music player or browser.
		
		// Start a timer for generating key repeat events.
		synchronized ( this )  // synchronize so we don't ever create more than one timer
		{
			keyRepeatTimer = new Timer();
			keyRepeatTimer.schedule( new KeyRepeater(translatedKey), REPEAT_PERIOD, REPEAT_PERIOD );
		}
	}
	
	/**
	 * Called when the user releases any key.
	 * 
	 * @param key is code of the key that was released.
	 */
	protected void keyReleased (int key)
	{
		// Stop simulating key repeated events.
		stopRepeatTimer();
		
		// Notify the master.
		int translatedKey = translateKeyCode( key );
		master.keyReleased( translatedKey );
		
		// If this is a menu key stop highlighting it.
		if ( master.hasMenuBar() )
		{
			if ( translatedKey == DeviceScreen.MENU_LEFT )
			{
				highlightLeftMenu = false;
				repaintMenuBar( false );
			}
			else if ( translatedKey == DeviceScreen.MENU_RIGHT )
			{
				highlightRightMenu = false;
				repaintMenuBar( false );
			}
		}
		
		// Do not forward the key event!
		//  super.keyPressed and .keyReleased can cause platform-specific
		//  and often undesirable behavior.  For example on the Sony Ericsson
		//  w810i they can turn on the music player or browser.
	}

	/**
	 * Maps key values to the constants defined in the outter class.
	 * 
	 * @param key is code of the button pressed.
	 * @return The integer value for the key.
	 */
	private int translateKeyCode (int key)
	{
		// Some phones have a bug where they treat the FIRE key as "Enter". 
		if ( tao && (key == 13) )
		{
			return DeviceScreen.FIRE;
		}
		
		// Is it a normal key?
		//   BlackBerry devices give the trackwheel and trackball movements
		//   as values through 6 and getGameAction does not translate them.
		//   There are no ASCII values used below 8 (backspace) so this is
		//   fine.
		if ( key > 6 )
		{
			return key;
		}
		
		// Is it a well defined game key such as a joystick movement?
		int action;
		
		try
		{
			action = getGameAction( key );
		}
		catch (Exception e)
		{
			// Some phones throw an exception for unsupported keys.
			// For example the Sony Ericsson K700.
			return key;
		}

		if ( action != 0 )
		{
			// We make all action keys negative.  This allows code to
			// check for special keys by seeing if the value is less
			// than 0.
			return -1 * action;
		}
			
		// Is it the left menu button?
		if ( (key == -6) || (key == -21) || (key == -1) )
		{
			// -6:   The Sun WTK emulator and Sony Ericcson phones
			// -21:  Motorola phones such as the SLVR
			// -1:   Siemens
			return DeviceScreen.MENU_LEFT;
		}
		
		// Is it the right menu button?
		if ( (key == -7) || (key == -22) || (key == -4) )
		{
			// -7:   The Sun WTK emulator and Sony Ericcson phones
			// -22:  Motorola phones such as the SLVR
			// -4:   Siemens
			return DeviceScreen.MENU_RIGHT;
		}
		
		// Otherwise it is undefined such as:
		//   Motorola center "Menu" soft key:  -23
		//   Sony Ericsson "Return" soft key under the left soft key:  -11
		//   Sony Ericsson "Clear" soft key under the right soft key:  -8
		//   Sony Ericsson "Camera" key on side of phone:  -25
		//   Sony Ericsson "Volume Up" key on side of phone:  -36
		//   Sony Ericsson "Volume Down" key on side of phone:  -37
		//   Sony Ericsson "Play/Pause Music" key on side of phone:  -23 (Note this is the same as Motorola's center menu button)
		//   Sony Ericsson "Music Player" key:  -22 (Note this is the same as Motorola's right menu button)
		//   Sony Ericsson "Internet Browser" key:  (Unavailable)
		return key;
	}
	
	/**
	 * Called when a stylus presses the screen.
	 *
	 * @param x is the horizontal location where the pointer was pressed
	 * @param y is the vertical location where the pointer was pressed
	 */
	protected void pointerPressed (int x, int y)
	{
		Theme theme = UIManager.getTheme();
		boolean processed = false;
		
		// Was the stylus pressed over a menu item?
		if ( master.hasMenuBar() )
		{
			int menuHeight = theme.getMenuHeight();
			int menuStart = super.getHeight() - menuHeight;
			
			if ( y > menuStart )
			{
				// The user clicked on the menu.
				int width = super.getWidth();
				
				if ( x < (width / 2) )
				{
					// The left menu item was clicked.
					master.declineNotify();
				}
				else
				{
					// The right menu item was clicked.
					master.acceptNotify();
				}
				
				processed = true; 
			}
		}
		
		// Was the stylus pressed over the title bar?
		boolean hasTitle = master.hasTitleBar();
		
		if ( (processed == false) && (hasTitle == true) )
		{
			int titleHeight = theme.getTitleHeight();
			
			if ( y < titleHeight )
			{
				// Ignore clicks on the title bar.
				processed = true;
			}
		}

		// Notify the master.
		if ( processed == false )
		{
			// Adjust the press location to fit in the canvas area.
			int py = y;
			
			if ( hasTitle )
			{
				py -= theme.getTitleHeight();
			}
			
			// Forward the event.
			master.pointerPressed( x, py );
		}
		
		// Continue processing the pointer event.
		super.pointerPressed( x, y );
	}
	
	/**
	 * Called when a stylus drags across the screen.
	 *
	 * @param x is the horizontal location where the pointer was pressed
	 * @param y is the vertical location where the pointer was pressed
	 */
	protected void pointerDragged (int x, int y)
	{
		// Adjust the press location to fit in the canvas area.
		int py = y;
		
		if ( master.hasTitleBar() )
		{
			Theme theme = UIManager.getTheme();
			py -= theme.getTitleHeight();
		}
		
		// Notify the master.
		master.pointerDragged( x, py );
		
		// Forward the pointer event.
		super.pointerDragged( x, y );
	}

	/**
	 * Called when a stylus is lifted off the screen.
	 *
	 * @param x is the horizontal location where the pointer was pressed
	 * @param y is the vertical location where the pointer was pressed
	 */
	protected void pointerReleased (int x, int y)
	{
		// Adjust the press location to fit in the canvas area.
		int py = y;
		
		if ( master.hasTitleBar() )
		{
			Theme theme = UIManager.getTheme();
			py -= theme.getTitleHeight();
		}
		
		// Notify the master.
		master.pointerReleased( x, py );
		
		// Forward the pointer event.
		super.pointerReleased( x, y );
	}
	
	/**
	 * Sets the title bar text.  This is called by the <code>master</code> when
	 * its title is changed.
	 * 
	 * @param title is the text that appears in the title bar across the
	 *  top of the screen.
	 *  
	 * @see DeviceScreen#setTitle(String)
	 */
	public void setTitle (String title)
	{
		// Does this JVM support our title bar feature?
		if ( supportsTitleBar() == false )
		{
			// These JVMs always shows a title bar.  We might as well use that
			// instead of painting a duplicate on our own.
			super.setTitle( title );
		}
		
		// For other JVMs we'll display the title on our own.  There is no
		// need to have the LCDUI do it for us.
	}
	
	/**
	 * Sets the menu bar text.  This is called by the <code>master</code> when
	 * its menu is changed.
	 * 
	 * @param left is the text for the negative menu option or <code>null</code>
	 *  to remove the button.  Negative menu options are things like canceling
	 *  a form and moving back to a previous screen.
	 * @param right is the text for the positive menu option or <code>null</code>
	 *  to remove the button.  Positive menu options are things like accepting
	 *  a form, advancing to the next screen, or displaying a menu.
	 *  
	 * @see DeviceScreen#setMenuText(String, String)
	 */
	public void setMenuText (String left, String right)
	{
		// Does this JVM support our own menu bar?
		if ( supportsMenuBar() == false )
		{
			// BlackBerry phones do not have MIDP 2.0 left and right menu buttons.
			// We must capture the BlackBerry phone's "Menu" and "Return" buttons
			// using the LCDUI menu functionality.
			//
			// IBM's J9 does not forward the left and right menu button codes to
			// us.  We have to use LCDUI menu functionality.
			
			// Remove the existing menu commands.
			if ( lcduiLeftMenuCommand != null )
			{
				removeCommand( lcduiLeftMenuCommand );
				lcduiLeftMenuCommand = null;
			}
			
			if ( lcduiRightMenuCommand != null )
			{
				removeCommand( lcduiRightMenuCommand );
				lcduiRightMenuCommand = null;
			}
			
			// Register new LCDUI menu commands.
			if ( left != null )
			{
				int position;
				
				if ( blackberry )
				{
					// This will be hooked up to the "Return" key and appear
					// second on the BlackBerry menu.
					position = 2;
				}
				else  // ibmJ9
				{
					// This will make the button appear on the left side of the menu.
					position = 1;
				}
				
				lcduiLeftMenuCommand = new Command( left, Command.CANCEL, position );
				addCommand( lcduiLeftMenuCommand );
			}
			
			if ( right != null )
			{
				int position;
				
				if ( blackberry )
				{
					// This will appear first on the BlackBerry menu.
					position = 1;
				}
				else  // ibmJ9
				{
					// This will make the button appear on the right side of the menu.
					position = 2;
				}
				
				lcduiRightMenuCommand = new Command( right, Command.OK, position );
				addCommand( lcduiRightMenuCommand );
				
				// Add a dummy left menu for IBM's J9 JVM.  Otherwise the right
				// menu would actually be on the left.
				if ( (ibmJ9 || tao) && (left == null) )
				{
					lcduiLeftMenuCommand = new Command( "", Command.CANCEL, 1 );
					addCommand( lcduiLeftMenuCommand );
				}
			}
		}
	}

	/**
	 * Indicates that a command event <code>c</code> has occurred on
	 * <code>Displayable<code> d.
	 * 
	 * @param c is a <code>Command</code> object identifying the command.
	 * @param d is the <code>Displayable</code> on which this event has occurred.
	 *  It will always be <code>this</code> screen.
	 *  
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction (Command c, Displayable d)
	{
		// Was it our left menu command?
		if ( (lcduiLeftMenuCommand != null) && (c == lcduiLeftMenuCommand) )
		{
			keyPressed( DeviceScreen.MENU_LEFT );
			keyReleased( DeviceScreen.MENU_LEFT );
		}
		
		// Was it our right menu command?
		if ( (lcduiRightMenuCommand != null) && (c == lcduiRightMenuCommand) )
		{
			keyPressed( DeviceScreen.MENU_RIGHT );
			keyReleased( DeviceScreen.MENU_RIGHT );
		}
	}
	
	/**
	 * Returns if the device supports having a menu bar at the bottom of the
	 * screen.  If not then no menu bar will painted at the bottom of the
	 * screen and a standard LCDUI menu will be used instead.
	 * 
	 * @return <code>true</code> if the device supports a menu bar or <code>false</code>
	 *  if it does not.
	 */
	public boolean supportsMenuBar ()
	{
		if ( blackberry || ibmJ9 || tao )
		{
			// These JVMs do not show our menu bar at the bottom of the
			// screen.  Instead they use the LCDUI menu system.
			return false;
		}
		else
		{
			// This phone can have a menu bar at the bottom of the screen.
			return true;
		}
	}
	
	/**
	 * Returns if the device supports having a title bar at the top of the
	 * screen.  If not then no title bar will painted at the top of the
	 * screen and a standard LCDUI title will be used instead.
	 * 
	 * @return <code>true</code> if the device supports a title bar or <code>false</code>
	 *  if it does not.
	 */
	public boolean supportsTitleBar ()
	{
		if ( ibmJ9 || tao )
		{
			// These JVMs always show a title bar.  We might as well use it.
			return false;
		}
		else
		{
			// This phone can have a title bar at the top of the screen.
			return true;
		}
	}
	
	/**
	 * Called when this screen is no longer going to be displayed.
	 * 
	 * @see javax.microedition.lcdui.Canvas#hideNotify()
	 */
	protected void hideNotify ()
	{
		// If the key repeat timer is running, stop it.
		stopRepeatTimer();

		// Don't highlight the menu options (in case we return to this screen).
		highlightLeftMenu = false;
		highlightRightMenu = false;
		
		// Continue to hide the screen.
		super.hideNotify();
	}

	/**
	 * Forces a repaint of the menu bar.
	 * 
	 * @param immediate when <code>true</code> finishes the painting before
	 *  this method returns; <code>false</code> does it in the normal paint
	 *  cycle.
	 */
	private void repaintMenuBar (boolean immediate)
	{
		Theme theme = UIManager.getTheme();
		int menuHeight = theme.getMenuHeight();
		int y = getHeight() - menuHeight;

		this.repaint( 0, y, getWidth(), menuHeight );
		
		if ( immediate )
		{
			this.serviceRepaints();
		}
	}
	
	/**
	 * Paints the screen.  If not in full screen mode, this includes the
	 * title bar and menu bar.
	 * <p>
	 * Typically derviced classes will only override the <code>paintCanvas</code>
	 * method.
	 * 
	 * @param g is the <code>Graphics</code> object to paint with.
	 * @see #paintTitleBar(Graphics, String, int, int, int, int)
	 * @see #paintBackground(Graphics, int, int, int, int)
	 * @see #paintCanvas(Graphics, int, int, int, int)
	 * @see #paintMenuBar(Graphics, String, String, int, int, int, int)
	 */
	protected void paint (Graphics g)
	{
		try
		{
			// Get some painting attributes.
			Theme theme = UIManager.getTheme();
			
			int width = getWidth();
			int height = getHeight();
	
			int titleHeight = 0;
			int menuHeight = 0;
			
			String title = null;
			String leftMenuText = null;
			String rightMenuText = null;
	
			// Get the original clip.
			int clipX = g.getClipX();
			int clipY = g.getClipY();
			int clipWidth = g.getClipWidth();
			int clipHeight = g.getClipHeight();
			
			// Paint the title bar and/or menu bar?
			if ( master.isFullScreenMode() == false )
			{
				// Paint the title bar at the top of the screen.
				title = master.getTitle();
				
				if ( master.hasTitleBar() )
				{
					// We'll paint the title after the canvas area.
					titleHeight = theme.getTitleHeight();
				}
				
				// Paint the menu bar at the bottom of the screen.
				if ( master.hasMenuBar() )
				{
					// Set the menu text.
					leftMenuText = master.getLeftMenuText();
					rightMenuText = master.getRightMenuText();
				
					if ( leftMenuText == null )
					{
						leftMenuText = "";
					}
					
					if ( rightMenuText == null )
					{
						rightMenuText = "";
					}
					
					// Set the height of the menu.
					menuHeight = theme.getMenuHeight();
				}
	
				// Set the graphics object for painting the canvas area.
				height = height - titleHeight - menuHeight;
				
				g.translate( 0, titleHeight );
				g.clipRect( 0, 0, width, height );
			}
			
			// Paint the canvas area.
			if ( DeviceScreen.intersects(g, 0, 0, width, height) )
			{
				master.paintBackground( g );
			
				g.setFont( theme.getFont() );
				g.setColor( theme.getFontColor() );
				master.paint( g );
			}
	
			// Restore the original graphics object.
			g.translate( 0, -titleHeight );
			g.setClip( clipX, clipY, clipWidth, clipHeight );
	
			// Paint the title bar.
			//  We do this after the canvas so it will paint over any
			//  canvas spillage.
			if ( titleHeight > 0 )
			{
				if ( DeviceScreen.intersects(g, 0, 0, width, titleHeight) )
				{
					// Set the graphics object for painting the title bar.
					g.clipRect( 0, 0, width, titleHeight );
					
					// Actually paint the title bar.
					master.paintTitleBar( g, title, width, titleHeight );
		
					// Restore the original graphics object.
					g.setClip( clipX, clipY, clipWidth, clipHeight );
				}
			}
	
			// Paint the menu bar.
			if ( menuHeight > 0 )
			{
				int y = getHeight() - menuHeight;
				
				if ( DeviceScreen.intersects(g, 0, y, width, menuHeight) )
				{
					// Set the graphics object for painting the menu bar.
					g.translate( 0, y );
					g.clipRect( 0, 0, width, menuHeight );
	
					// Clear the background first.
					//   On the Sony Ericsson w810i things drawn accidentally
					//   over the menu space can show through gradient backgrounds.
					//   Clear the area completely first.
					int menuBackgroundColor = theme.getMenuBarBackgroundColor();
					g.setColor( menuBackgroundColor );
					g.fillRect( 0, 0, width, menuHeight );
					
					// Actually paint the menu bar.
					master.paintMenuBar( g,
							leftMenuText, highlightLeftMenu,
							rightMenuText, highlightRightMenu, 
							width, menuHeight );
					
					// Restore the original graphics object.
					g.translate( 0, -y );
					g.setClip( clipX, clipY, clipWidth, clipHeight );
				}
			}
		}
		catch (Throwable t)
		{
			// Unhandled exception in paint() will crash an application and not
			// tell you why.  This lets the programmer know what caused the problem.
			Log.warn("Unhandled exception in paint for " + master, t);
		}
	}
}
