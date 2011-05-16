package org.j4me.ui.components;

import javax.microedition.lcdui.*;
import org.j4me.ui.*;

/**
 * The <code>Whitespace</code> component adds whitespace to a form.
 */
public class Whitespace
	extends Component
{
	/**
	 * The number of pixels on the Y-axis to space.
	 */
	private int vertical;
	
	/**
	 * Constructs a <code>Whitespace</code> component.
	 * 
	 * @param vertical is the number of pixels to space.  It must be
	 *  0 or greater.
	 */
	public Whitespace (int vertical)
	{
		setSpacing( vertical );
	}

	/**
	 * Sets the vertical amount of whitespace this component occupies.
	 * This does not include the form's spacing above and below this
	 * component.
	 * 
	 * @param vertical is the number of pixels to space.  It must be
	 *  0 or greater.
	 * @see Dialog#getSpacing()
	 */
	public void setSpacing (int vertical)
	{
		if ( vertical < 0 )
		{
			vertical = 0;
		}
		
		this.vertical = vertical;
		
		invalidate();
	}
	
	/**
	 * Paints the whitespace.
	 * 
	 * @see org.j4me.ui.components.Component#paintComponent(javax.microedition.lcdui.Graphics, org.j4me.ui.Theme, int, int, boolean)
	 */
	protected void paintComponent (Graphics g, Theme theme, int width, int height, boolean selected)
	{
		// Empty space requires no painting.
	}

	/**
	 * Returns the dimensions of the whitespace.
	 * 
	 * @see org.j4me.ui.components.Component#getPreferredComponentSize(org.j4me.ui.Theme, int, int)
	 */
	protected int[] getPreferredComponentSize (Theme theme, int viewportWidth, int viewportHeight)
	{
		return new int[] { viewportWidth, vertical };
	}
}
