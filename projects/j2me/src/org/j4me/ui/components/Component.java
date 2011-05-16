package org.j4me.ui.components;

import javax.microedition.lcdui.*;
import org.j4me.ui.*;

/**
 * Components are UI widgets that appear on forms.  Examples of components include
 * labels, text boxes, and check boxes.  The <code>Dialog</code> class calls methods on
 * this interface to layout, paint, and manage components.
 */
public abstract class Component
{
	/**
	 * Components that highlight entry boxes should use this value as a width for
	 * their borders.  For example an unselected text box appears as a rectangle
	 * but a selected text box has a border around it that is this thick and has
	 * rounded edges.
	 */
	protected static final int HIGHLIGHTED_BORDER_WIDTH = 2;
	
	/**
	 * The horizontal justification of the text in this label.  It must be one of
	 * <code>Graphics.LEFT</code>, <code>Graphics.HCENTER</code>, and <code>Graphics.RIGHT</code>.
	 */
	private int horizontalAlignment = Graphics.LEFT;
	
	/**
	 * Whether the component is currently visible on the screen or not.
	 */
	private boolean visible;
	
	/**
	 * The screen this component is placed on.
	 */
	private DeviceScreen screen;
	
	/**
	 * A component this one is embedded within.  Typically components are not
	 * embedded within each other so this will be <code>null</code>.
	 */
	protected Component container;
	
	/**
	 * The left corner pixel of this component.  This value is specified by the
	 * last call to <code>paint</code>.
	 */
	private int x;
	
	/**
	 * The top corner pixel of this component.  This value is specified by the
	 * last call to <code>paint</code>.
	 */
	private int y;
	
	/**
	 * The width of this component in pixels.  This value is specified by the
	 * last call to <code>paint</code>.
	 */
	private int width;
	
	/**
	 * The height of this component in pixels.  This value is specified by the
	 * last call to <code>paint</code>.
	 */
	private int height;
	
	/**
	 * Constructs a component and attaches it to a screen.
	 */
	public Component ()
	{
	}

	/**
	 * Paints the component using <code>g</code>.  The top-left corner is at (0,0)
	 * and the component fills the rectangle bounded by <code>width</code> and
	 * <code>height</code>. 
	 * 
	 * @param g is the <code>Graphics</code> object to be used for rendering the item.
	 * @param theme is the application's theme.  Use it to get fonts and colors.
	 * @param screen is the screen object displaying this component.
	 * @param x is the left corner pixel of the component.
	 * @param y is the top corner pixel of the component.
	 * @param width is the width, in pixels, to paint the component.
	 * @param height is the height, in pixels, to paint the component.
	 * @param selected is <code>true</code> when this components is currently selected
	 *  and <code>false</code> when it is not.
	 */
	public final void paint (Graphics g, Theme theme, DeviceScreen screen,
			int x, int y, int width, int height,
			boolean selected)
	{
		if ( isShown() )
		{
			// Record the position of this component.
			this.screen = screen;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			
			// Set the graphics properties for painting the component.
			int originalClipX = g.getClipX();
			int originalClipY = g.getClipY();
			int originalClipWidth = g.getClipWidth();
			int originalClipHeight = g.getClipHeight();
	
			// Workaround a bug by doubling the clip height.
			//  The Sun WTK clips the bottom of graphics operations for
			//  rounded rectangle drawing and filling.  This can be
			//  avoided by doubling the clip height.
			g.setClip( originalClipX, originalClipY, originalClipWidth, originalClipHeight * 2 );
			
			g.translate( x, y );
	
			g.clipRect( 0, 0, width, height * 2 );  // *2 to workaround clipping bug
			
			int originalColor = g.getColor();
			g.setColor( theme.getFontColor() );
			
			Font originalFont = g.getFont();
			g.setFont( theme.getFont() );
			
			int originalStroke = g.getStrokeStyle();
			g.setStrokeStyle( Graphics.SOLID );
			
			// Actually paint the component.
			paintComponent( g, theme, width, height, selected );
			
			// Reset the graphics properties.
			g.translate( -x, -y );
			g.setClip( originalClipX, originalClipY, originalClipWidth, originalClipHeight );
			g.setColor( originalColor );
			g.setFont( originalFont );
			g.setStrokeStyle( originalStroke );
		}
	}
	
	/**
	 * Implemented by the subclass to render the item within its container.  At the
	 * time of the call, the <code>Graphic</code>s context's destination is the content area of
	 * this <code>Component</code> (or back buffer for it).  The translation is set so that
	 * the upper left corner of the content area is at (0,0), and the clip is set
	 * to the area to be painted.  The application must paint every pixel within
	 * the given clip area.  The item is allowed to modify the clip area, but the 
	 * system must not allow any modification to result in drawing outside the 
	 * bounds of the item's content area.  The <code>w</code> and <code>h</code> passed in are the width
	 * and height of the content area of the item.  These values will always be
	 * set to the clip width and height and are passed here for convenience.
	 * <p>
	 * Other values of the <code>Graphics</code> object are as follows:
	 * <ul>
	 *  <li>the current color is <code>Theme.getFontColor()</code>;
	 *  <li>the font is <code>Theme.getFont()</code>;
	 *  <li>the stroke style is <code>SOLID</code>;
	 * </ul>
	 * <p>
	 * The <code>paint()</code> method will be called only when at least a portion of the
	 * item is actually visible on the display.
	 * 
	 * @param g is the <code>Graphics</code> object to be used for rendering the item.
	 * @param theme is the application's theme.  Use it to get fonts and colors.
	 * @param width is the width, in pixels, to paint the component.
	 * @param height is the height, in pixels, to paint the component.
	 * @param selected is <code>true</code> when this components is currently selected
	 *  and <code>false</code> when it is not.
	 */
	protected abstract void paintComponent (Graphics g, Theme theme, int width, int height, boolean selected);
	
	/**
	 * Returns the desired width and height of this component in pixels.
	 * 
	 * @param theme is the application's <code>Theme</code>.
	 * @param viewportWidth is the width of the screen in pixels.
	 * @param viewportHeight is the height of the screen in pixels.
	 * @return A array with two elements where the first is the width of the
	 *  component in pixels and the second is the height.
	 */
	public final int[] getPreferredSize (Theme theme, int viewportWidth, int viewportHeight)
	{
		// Get the component's dimensions.
		int[] dimensions = getPreferredComponentSize( theme, viewportWidth, viewportHeight );
		
		if ( (dimensions == null) || (dimensions.length != 2) )
		{
			throw new RuntimeException(getClass().getName() + ".getPreferredComponentSize must return an array of length 2");
		}
		
		return dimensions;
	}
	
	/**
	 * Returns the desired width and height of this component in pixels.
	 * It cannot be wider than the screen or it will be cropped.  However, it can
	 * be taller than the screen, in which case a scroll bar will be added to
	 * the form this component resides on.
	 * 
	 * @param theme is the application's <code>Theme</code>.
	 * @param viewportWidth is the width of the viewable area, in pixels,
	 *  the component can use.
	 * @param viewportHeight is the height of the viewable area, in pixels,
	 *  the component can use.
	 * @return A array with two elements where the first is the width of the
	 *  component in pixels and the second is the height.
	 */
	protected abstract int[] getPreferredComponentSize (Theme theme, int viewportWidth, int viewportHeight);
	
	/**
	 * Tells if this component accepts user input or not.  If it does then
	 * it can be scrolled to by the user.  If it does not, it will be displayed,
	 * but can be skipped over by scrolling.
	 * <p>
	 * The default implementation returns <code>false</code>.  Override this method
	 * to return <code>true</code> if the component accepts input.
	 * 
	 * @return <code>true</code> if the component accepts user input; <code>false</code> if
	 *  it does not.
	 */
	public boolean acceptsInput ()
	{
		return false;
	}
	
	/**
	 * @return The screen displaying this component.
	 */
	public DeviceScreen getScreen ()
	{
		return screen;
	}
	
	/**
	 * @return The pixel for the left side of this component.
	 */
	public int getX ()
	{
		return x;
	}
	
	/**
	 * @return The pixel for the top of this component.
	 */
	public int getY ()
	{
		return y;
	}
	
	/**
	 * @return The width, in pixels, for this component.
	 */
	public int getWidth ()
	{
		return width;
	}
	
	/**
	 * @return The height, in pixels, for this component.
	 */
	public int getHeight ()
	{
		return height;		
	}
		
	/**
	 * @return The horizontalAlignment of the text in the label.  It is one of
	 *  <code>Graphics.LEFT</code>, <code>Graphics.HCENTER</code>, and <code>Graphics.RIGHT</code>. 
	 */
	public int getHorizontalAlignment ()
	{
		return horizontalAlignment;
	}
	
	/**
	 * @param alignment is how the text in the label is justified.  It is one of
	 *  <code>Graphics.LEFT</code>, <code>Graphics.HCENTER</code>, and <code>Graphics.RIGHT</code>. 
	 */
	public void setHorizontalAlignment (int alignment)
	{
		if ( (alignment != Graphics.LEFT) && (alignment != Graphics.HCENTER) && (alignment != Graphics.RIGHT) )
		{
			throw new IllegalArgumentException("setHorizontalAlignment only takes Graphics.LEFT, HCENTER, or RIGHT");
		}
		
		this.horizontalAlignment = alignment;
	}
	
	/**
	 * Sets if the component is currently shown on the screen or not.
	 * A call to <code>show(true)</code> must be made before <code>paint</code>.
	 * When the component is no longer visible call <code>show(false)</code>
	 * so that the component may clean up any resources.
	 * 
	 * @param visible when <code>true</code> indicates the component is painted (or
	 *  about to be) on the screen.
	 */
	public void visible (boolean visible)
	{
		if ( this.visible != visible )
		{
			this.visible = visible;
			
			// Raise an event for the component.
			if ( visible )
			{
				showNotify();
			}
			else
			{
				hideNotify();
			}
		}
	}
	
	/**
	 * Returns if this component is shown on the screen now.
	 * 
	 * @return <code>true</code> if this component is currently visible on the
	 *  screen; <code>false</code> if not. 
	 */
	public boolean isShown ()
	{
		return visible;
	}
	
	/**
	 * An event raised whenever the component is made visible on the screen.
	 * This is called before the <code>paintComponent</code> method.
	 * <p>
	 * The default implementation does nothing.  Override it to initialize
	 * any resources required by the component.
	 */
	protected void showNotify ()
	{
	}

	/**
	 * An event raised whenever the component is removed from the screen.
	 * <p>
	 * The default implementation does nothing.  Override it to clean up
	 * any resources required by the component.
	 */
	protected void hideNotify ()
	{
	}
	
	/**
	 * Signals that the Component's size needs to be updated.  This method
	 * is intended to be called by subclassed components to force layout
	 * of the component to change.  A call to this method will return immediately,
	 * and it will cause the container's layout algorithm to run at some point
	 * in the future.
	 * 
	 * @see Dialog#invalidate()
	 */
	protected void invalidate ()
	{
		// Invalidate the whole dialog and have it reposition all components.
		if ( screen != null )
		{
			if ( screen instanceof Dialog )
			{
				Dialog d = (Dialog)screen;
				d.invalidate();
			}
		}
		
		// Erase any knowledge this component has of its position.
		x = y = width = height = 0;
	}
	
	/**
	 * Forces this component to repaint itself.
	 */
	public void repaint ()
	{
		if ( isShown() && (screen != null) )
		{
			if ( height != 0 )
			{
				// Repaint just the component area since we know it.
				screen.repaint( x, y, width, height );
			}
			else
			{
				// Repaint the entire screen since we aren't sure where
				// the component is on it.  An invalidate() was likely
				// just called so the component may move anyway.
				screen.repaint();
			}
		}
	}

	/**
	 * Called when a key is pressed.  It can be identified using the
	 * constants defined in the <code>DeviceScreen</code> class.  Note to
	 * receive key events the component must override <code>acceptsInput</code>
	 * to return <code>true</code>.
	 * <p>
	 * The default implementation does nothing.  If a component requires
	 * keypad interaction, such as to enter text, it should override this
	 * method.
	 * 
	 * @param keyCode is the key code of the key that was pressed.
	 */
	public void keyPressed (int keyCode)
	{
	}

	/**
	 * Called when a key is repeated (held down).  It can be identified using the
	 * constants defined in the <code>DeviceScreen</code> class.  Note to
	 * receive key events the component must override <code>acceptsInput</code>
	 * to return <code>true</code>.
	 * <p>
	 * The default implementation does nothing.  If a component requires
	 * keypad interaction, such as to enter text, it should override this
	 * method.
	 * 
	 * @param keyCode is the key code of the key that was held down.
	 */
	public void keyRepeated (int keyCode)
	{
	}
	
	/**
	 * Called when a key is released.  It can be identified using the
	 * constants defined in the <code>DeviceScreen</code> class.  Note to
	 * receive key events the component must override <code>acceptsInput</code>
	 * to return <code>true</code>.
	 * <p>
	 * The default implementation does nothing.  If a component requires
	 * keypad interaction, such as to enter text, it should override this
	 * method.
	 * 
	 * @param keyCode is the key code of the key that was released.
	 */
	public void keyReleased (int keyCode)
	{
	}
	
	/**
	 * Called when the pointer is pressed.
	 * 
	 * @param x is the horizontal location where the pointer was pressed
	 *  relative to the top-left corner of the component.
	 * @param y is the vertical location where the pointer was pressed
	 *  relative to the top-left corner of the component.
	 */
	public void pointerPressed (int x, int y)
	{
	}
	
	/**
	 * Called when the pointer is released.
	 * 
	 * @param x is the horizontal location where the pointer was released
	 *  relative to the top-left corner of the component.
	 * @param y is the vertical location where the pointer was released
	 *  relative to the top-left corner of the component.
	 */
	public void pointerReleased (int x, int y)
	{
	}
	
	/**
	 * Called when the pointer is dragged.
	 * 
	 * @param x is the horizontal location where the pointer was dragged
	 *  relative to the top-left corner of the component.
	 * @param y is the vertical location where the pointer was dragged
	 *  relative to the top-left corner of the component.
	 */
	public void pointerDragged (int x, int y)
	{
	}
	
	/**
	 * Paints a rectangle used within a component.  If the rectangle is
	 * <code>selected</code> it will have rounded edges and be highlighted.  If it
	 * is not it will have square edges and be slightly inset from
	 * (<code>x</code>, <code>y</code>).  The border color, highlight color, and inner
	 * part of the rectangle (background color) all come from the <code>theme</code>.
	 * 
	 * @param g is the <code>Graphics</code> object to be used for rendering the item.
	 * @param theme is the application's theme.  Use it to get fonts and colors.
	 * @param x is the left side of the box.
	 * @param y is the top of the box.
	 * @param width is the width, in pixels, to paint the component.
	 * @param height is the height, in pixels, to paint the component.
	 * @param selected is <code>true</code> when this components is currently selected
	 *  and <code>false</code> when it is not.
	 * @return The offset, in pixels, of the interior of the box.  This is the
	 *  usuable area by a component:  <code>(x + offset, y + offset,
	 *  width - 2 * offset, height - 2 * offset)</code>.
	 */
	protected static int paintRect (Graphics g, Theme theme, int x, int y, int width, int height, boolean selected)
	{
		// Calculate how much to round the edges.  This makes the component
		// look better.  However, we don't want to make this so big it makes
		// the component's border crazily thick.
		int rounding = Math.min( height / 4, 5 );
		
		// Paint the border.
		if ( selected )
		{
			// Draw a thick, highlighted border.
			int border = theme.getHighlightColor();
			g.setColor( border );
			
			g.drawRoundRect( 0, y, width - 1, height - 1, rounding, rounding );
		}

		// Draw a normal border.
		int border = theme.getBorderColor();
		g.setColor( border );
		
		int bo = HIGHLIGHTED_BORDER_WIDTH - 1;
		int bx = bo;
		int by = y + bo;
		
		int bs = 2 * bo;
		int bw = width - bs;
		int bh = height - bs;
		
		g.drawRect( bx, by, bw - 1, bh - 1 );
		
		// Return the offset from the edges of the component to the inside.
		int offset = Math.max( HIGHLIGHTED_BORDER_WIDTH, rounding / 2 );
		return offset;
	}
}
