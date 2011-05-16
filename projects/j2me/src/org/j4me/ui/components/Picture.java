package org.j4me.ui.components;

import java.io.*;
import javax.microedition.lcdui.*;
import org.j4me.ui.*;

/**
 * The <code>Picture</code> component shows an <code>Image</code>.  Typically this
 * is a PNG resource.  It can also be an image created manually using
 * the <code>Image.createImage</code> methods.
 * 
 * @see javax.microedition.lcdui.Image
 */
public class Picture
	extends Component
{
	/**
	 * The image object displayed by this <code>Picture</code> component.
	 */
	private Image image;
	
	/**
	 * Constructs a <code>Picture</code> component.
	 */
	public Picture ()
	{
	}
	
	/**
	 * @return The <code>Image</code> displayed by this component.
	 */
	public Image getImage ()
	{
		return image;
	}
	
	/**
	 * @param image is the <code>Image</code> displayed by this component.
	 */
	public void setImage (Image image)
	{
		this.image = image;
		invalidate();
	}
	
	/**
	 * Sets the image displayed by this component to a PNG resource. 
	 * 
	 * @param location is place in the Jar file the PNG resource is located.
	 *  For example if it were in a directory called <code>img</code> this would
	 *  be <code>"/img/filename.png"</code>.
	 * @throws IOException if the PNG could not be loaded from <code>location</code>.
	 */
	public void setImage (String location)
		throws IOException
	{
		this.image = Image.createImage( location );
		invalidate();
	}
	
	/**
	 * Paints the picture component.
	 * 
	 * @see org.j4me.ui.components.Component#paintComponent(javax.microedition.lcdui.Graphics, org.j4me.ui.Theme, int, int, boolean)
	 */
	protected void paintComponent (Graphics g, Theme theme, int width, int height, boolean selected)
	{
		if ( image != null )
		{
			// Determine the screen position.
			int horizontalAlignment = getHorizontalAlignment();
			int anchor = horizontalAlignment | Graphics.TOP;

			int y = (height - image.getHeight()) / 2;
			
			int x;
			
			if ( horizontalAlignment == Graphics.LEFT )
			{
				x = 0;
			}
			else if ( horizontalAlignment == Graphics.HCENTER )
			{
				x = width / 2;
			}
			else  // horizontalAlignment == Graphics.RIGHT
			{
				x = width;
			}

			// Paint image.
			g.drawImage( image, x, y, anchor ); 
		}
	}

	/**
	 * Returns the dimensions of the check box.
	 * 
	 * @see org.j4me.ui.components.Component#getPreferredComponentSize(org.j4me.ui.Theme, int, int)
	 */
	protected int[] getPreferredComponentSize (Theme theme, int viewportWidth, int viewportHeight)
	{
		if ( image == null )
		{
			throw new IllegalStateException();
		}
		
		return new int[] { image.getWidth(), image.getHeight() };
	}
}
