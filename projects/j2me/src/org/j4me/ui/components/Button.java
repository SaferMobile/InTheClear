package org.j4me.ui.components;

import java.awt.Color;

import javax.microedition.lcdui.*;

import org.j4me.ui.*;

/**
 * The <code>CheckBox</code> component lets a user turn an option on or off.
 * Multiple check box components can be placed sequentially to create
 * a list of choices where zero or more can be chosen.
 */
public class Button
	extends Component
{
	
	/**
	 * The text that appears to the right of the check box.
	 */
	private String strLabel = new String ();

	private Font fontButton = Font.getFont(Font.FACE_MONOSPACE, 
            Font.STYLE_BOLD, Font.SIZE_LARGE);
	
	private javax.microedition.lcdui.CommandListener cl;
	/**
	 * 
	 * Constructs a <code>CheckBox</code> component.
	 */
	public Button ()
	{
	}


	
	/**
	 * Sets the text that appears next to the check box.
	 * 
	 * @param value is the text that appears to the right of the check box.
	 *  A <code>null</code> is treated as the empty string "".
	 */
	public void setLabel (String value)
	{
		strLabel = value;
		
		// The label may change the size requirements of this component.
		invalidate();
	}
	
	
	/**
	 * An event raised whenever the component is made visible on the screen.
	 * This is called before the <code>paintComponent</code> method.
	 */
	protected void showNotify ()
	{
		super.showNotify();
	}

	/**
	 * An event raised whenever the component is removed from the screen.
	 */
	protected void hideNotify ()
	{
		super.hideNotify();
	}

	/**
	 * Paints the check box component.
	 * 
	 * @see org.j4me.ui.components.Component#paintComponent(javax.microedition.lcdui.Graphics, org.j4me.ui.Theme, int, int, boolean)
	 */
	protected void paintComponent (Graphics g, Theme theme, int width, int height, boolean selected)
	{
		int widthOffset = 10;
		int buttonWidth = width-(widthOffset*2);
		
		
		if (selected)
		{
			g.setColor(theme.getMenuFontHighlightColor());
			g.fillRoundRect(widthOffset, 0, buttonWidth, height, 15, 15);
			
			g.setColor(theme.getFontColor());
			g.drawRoundRect(widthOffset, 0, buttonWidth, height, 15, 15);
			
		}
		else
		{
			g.setColor(theme.getMenuBarBackgroundColor());
			g.fillRoundRect(widthOffset, 0, buttonWidth, height, 15, 15);
			
		}
		
		// Paint the text to the side of the box.
		int lx = width/2;
		int ly = height/2-fontButton.getHeight()/2;
		
		
		g.setColor(theme.getScrollbarBorderColor());
		g.setFont(fontButton);
		g.drawString(strLabel, lx, ly, Graphics.HCENTER | Graphics.TOP);
	}

	
	/**
	 * Returns the dimensions of the check box.
	 * 
	 * @see org.j4me.ui.components.Component#getPreferredComponentSize(org.j4me.ui.Theme, int, int)
	 */
	protected int[] getPreferredComponentSize (Theme theme, int viewportWidth, int viewportHeight)
	{

		
		return new int[] { viewportWidth, 30 };
	}
	
	/**
	 * @return <code>true</code> because this component accepts user input.
	 */
	public boolean acceptsInput ()
	{
		return true;
	}
	
	/**
	 * Called when a key is pressed.
	 * 
	 * @param keyCode is the key code of the key that was pressed.
	 */
	public void keyPressed (int keyCode)
	{
		// Was it an input character?
		//   Otherwise it was a menu button of special character.
		if ( (keyCode > 0) || (keyCode == DeviceScreen.FIRE) )
		{
			repaint();
		}
		
		
		
		// Continue processing the key event.
		super.keyPressed( keyCode );
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
		repaint();
		
		// Continue processing the pointer event.
		super.pointerPressed( x, y );
	}
}
