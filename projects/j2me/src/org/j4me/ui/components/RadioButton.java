package org.j4me.ui.components;

import java.util.*;
import javax.microedition.lcdui.*;
import org.j4me.ui.*;

/**
 * A <code>RadioButton</code> component lets a user select exactly one choice
 * from a list.  An example of where radio buttons are useful is to select
 * a logging level.  There are a known set of choices and the level is
 * set to one of them.
 * <p>
 * Traditionally radio buttons are displayed next to each other in a box.
 * This component forgoes that model to save screen space.  Instead the
 * selected option is displayed.  If the user pressed a button while the
 * component is highlighted it takes them to a screen, similar to a menu,
 * where the user can choose a different value.
 * 
 * @see javax.microedition.lcdui.ChoiceGroup
 */
public class RadioButton
	extends Component
{
	/**
	 * The label that appears above the radio button.  For example it might
	 * say "Log Levels".  If this is <code>null</code> then no label will appear.
	 */
	private Label label;
	
	/**
	 * The ordered list of radio button choices.
	 */
	protected Vector choices = new Vector();
	
	/**
	 * The index of the currently selected choice.
	 */
	protected int selected;
	
	/**
	 * This component appears as a <code>TextBox</code>.  Only editing is done
	 * differently.  This component takes the user to another screen based
	 * on <code>Menu</code>.
	 */
	private TextBox box = new TextBox();
	
	/**
	 * Constructs a radio button component.
	 */
	public RadioButton ()
	{
	}

	/**
	 * @return The text that appears above the radio button.  If
	 *  <code>null</code> there is no text.
	 */
	public String getLabel ()
	{
		if ( label == null )
		{
			return null;
		}
		else
		{
			return label.getLabel();
		}
	}
	
	/**
	 * @param label is the text that appears above the radio button.
	 *  If <code>null</code> there will be no text.
	 */
	public void setLabel (String label)
	{
		if ( label == null )
		{
			this.label = null;
		}
		else
		{
			if ( this.label == null )
			{
				this.label = new Label();
			}
			
			this.label.setLabel( label );
		}
		
		invalidate();
	}
	
	/**
	 * Returns the number of elements to choose from.
	 * 
	 * @return The number of elements to choose from.
	 */
	public int size ()
	{
		return choices.size();
	}

	/**
	 * Gets the <code>String</code> for the element referenced by
	 * <code>elementNum</code>.
	 * 
	 * @param elementNum is the index of the element to be queried.
	 * @return The string for the element.
	 */
	public String getString (int elementNum)
	{
		String value = (String)choices.elementAt( elementNum );
		return value;
	}
	
	/**
	 * Appends an element to the list of choices.
	 * 
	 * @param choice is the string to be added.
	 * @return The assigned index of the element.
	 */
	public int append (String choice)
	{
		if ( choice == null )
		{
			throw new IllegalArgumentException("Cannot append null choice");
		}
		
		choices.addElement( choice );
		return choices.size() - 1;
	}

	/**
	 * Inserts an element into the list of choices just prior to the
	 * element specified.
	 * 
	 * @param elementNum is the index of the element where insertion
	 *  is to occur.
	 * @param choice is the string to be inserted.
	 */
	public void insert (int elementNum, String choice)
	{
		if ( choice == null )
		{
			throw new IllegalArgumentException("Cannot insert null choice");
		}
		
		choices.insertElementAt( choice, elementNum );
	}
	
	/**
	 * Deletes the element referenced by <code>elementNum</code>.
	 * 
	 * @param elementNum is the index of the element to be deleted.
	 */
	public void delete (int elementNum)
	{
		choices.removeElementAt( elementNum );
	}

	/**
	 * Deletes all elements from the list of choices.
	 */
	public void deleteAll ()
	{
		choices.removeAllElements();
	}

	/**
	 * Sets the element referenced by <code>elementNum</code>, replacing the
	 * previous contents of the element.
	 * 
	 * @param elementNum the index of the element to be set.
	 * @param choice is the string to be set.
	 */
	public void set (int elementNum, String choice)
	{
		if ( choice == null )
		{
			throw new IllegalArgumentException("Cannot set null choice");
		}
		
		choices.setElementAt( choice, elementNum );
	}

	/**
	 * Returns the index number of the element that is selected.
	 * 
	 * @return The index of selected element.
	 */
	public int getSelectedIndex ()
	{
		return selected;
	}
	
	/**
	 * Returns the value of the element that is selected.
	 * 
	 * @return The value of the selected element.
	 */
	public String getSelectedValue ()
	{
		return getString( selected );
	}

	/**
	 * Sets the index of the selected element.
	 * 
	 * @param elementNum is the currently selected element.
	 */
	public void setSelectedIndex (int elementNum)
	{
		if ( (elementNum < 0) || (elementNum >= choices.size()) )
		{
			throw new IndexOutOfBoundsException("elementNum not a possible choice");
		}
		
		this.selected = elementNum;
	}
	
	/**
	 * An event raised whenever the component is made visible on the screen.
	 * This is called before the <code>paintComponent</code> method.
	 * 
	 * @see Component#showNotify()
	 */
	protected void showNotify ()
	{
		// Pass the event to contained components.
		if ( label != null )
		{
			label.visible( true );
		}
		
		box.visible( true );
		
		// Continue processing the event.
		super.showNotify();
	}

	/**
	 * An event raised whenever the component is removed from the screen.
	 * 
	 * @see Component#hideNotify()
	 */
	protected void hideNotify ()
	{
		// Pass the event to contained components.
		if ( label != null )
		{
			label.visible( false );
		}
		
		box.visible( false );
		
		// Continue processing the event.
		super.hideNotify();
	}

	/**
	 * Paints the radio button component.  It is shown as a text box with the
	 * currently selected value in it.
	 * <p>
	 * Changing the selected value is done on a different screen.  Therefore it
	 * is not painted here.
	 * 
	 * @see org.j4me.ui.components.Component#paintComponent(javax.microedition.lcdui.Graphics, org.j4me.ui.Theme, int, int, boolean)
	 */
	protected void paintComponent (Graphics g, Theme theme, int width, int height, boolean selected)
	{
		if ( size() == 0 )
		{
			throw new IllegalStateException("RadioButton has no values");
		}
		
		int y = 0;
		
		// Paint the label above this component.
		if ( label != null )
		{
			// Make the justification the same as for this component.
			label.setHorizontalAlignment( this.getHorizontalAlignment() );
			
			// Paint the label.
			label.paint( g, theme, getScreen(), 0, 0, width, height, selected );
			
			// The top of the progress bar is below the label.
			int labelHeight = label.getHeight();
			y = labelHeight;
			height -= labelHeight;
		}
		
		// Set the value in the text box.
		String value = getSelectedValue();
		box.setString( value );
		
		// Paint the text box.
		box.paint( g, theme, getScreen(), 0, y, width, height, selected );

		// We no longer paint the arrow in the radio button's text box.
		// The text box is sufficiently highlighted without it.
		//
		//		// Paint an arrow to indicate the user can change the value.
		//		if ( selected )
		//		{
		//			// Get the dimensions of the arrow.
		//			int arrowHeight = height / 2;
		//			
		//			if ( arrowHeight % 2 == 0 )
		//			{
		//				// The arrow height must odd to make a good looking triangle.
		//				arrowHeight--;
		//			}
		//			
		//			int arrowWidth = arrowHeight / 2;
		//			int arrowX = width - box.getOffset() - arrowWidth;
		//			int arrowY = y + (height - arrowHeight) / 2;
		//
		//			// Clear the background.
		//			g.setColor( theme.getBackgroundColor() );
		//			g.fillRect( arrowX, arrowY, arrowWidth, arrowHeight );
		//			
		//			// Draw the triangle.
		//			g.setColor( theme.getHighlightColor() );
		//			g.fillTriangle(
		//					arrowX, arrowY,
		//					arrowX, arrowY + arrowHeight,
		//					arrowX + arrowWidth, arrowY + arrowHeight / 2 + 1 );
		//		}
	}

	/**
	 * Returns the ideal size for a radio button component.
	 * 
	 * @see org.j4me.ui.components.Component#getPreferredComponentSize(org.j4me.ui.Theme, int, int)
	 */
	protected int[] getPreferredComponentSize (Theme theme, int viewportWidth, int viewportHeight)
	{
		int[] dimensions = box.getPreferredSize( theme, viewportWidth, viewportHeight );
		
		// Add the height of the label above the component.
		if ( label != null )
		{
			int[] labelDimensions = label.getPreferredComponentSize( theme, viewportWidth, viewportHeight );
			dimensions[1] += labelDimensions[1];
		}
		
		return dimensions;
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
		if ( (keyCode == DeviceScreen.UP) || (keyCode == DeviceScreen.DOWN) )
		{
			// Continue processing the key event.
			super.keyPressed( keyCode );
		}
		else
		{
			select();
		}
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
		// If anywhere on the radio button is pressed it has been selected.
		select();
		
		// Stop processing the pointer event.
		//   i.e. do not call super.pointerPressed( x, y );
	}
	
	/**
	 * Called when the radio button's value is being edited.  This method
	 * replaces the current screen with a menu of all the available options.
	 * When the user selects a new option the original screen returns and
	 * is updated with the new value.
	 */
	protected void select ()
	{
		// Create a menu for selecting the choice.
		Menu list = new Menu( getLabel(), getScreen() );
		
		Enumeration e = choices.elements();
		int index = 0;
		
		while ( e.hasMoreElements() )
		{
			String name = (String)e.nextElement();
			
			RadioItem item = new RadioItem( name, index );
			list.appendMenuOption( item );
			
			index++;
		}
		
		// Highlight the currently selected value.
		int highlight = getSelectedIndex();
		list.setSelected( highlight );
		
		// Replace this screen with the menu. 
		list.show();
	}
	
	/**
	 * Objects are used by the input screen to show the list of choices
	 * and set the value of the one the user selected.
	 */
	private final class RadioItem
		implements MenuItem
	{
		private final String text;
		private final int index;

		/**
		 * Constructs a radio item to place in a <code>Menu</code>.
		 * 
		 * @param choice is the name for this menu item.
		 * @param index is the element number in <code>choices</code> this
		 *  menu item represents.
		 */
		public RadioItem (String choice, int index)
		{
			this.text = choice;
			this.index = index;
		}
		
		/**
		 * The text for this choice.
		 */
		public String getText ()
		{
			return text;
		}

		/**
		 * Called when the user selects this radio item.  It sets the
		 * radio button component's selected value to this item. 
		 */
		public void onSelection ()
		{
			// Select this item in the radio button component.
			RadioButton.this.setSelectedIndex( index );
			
			// Show the screen with the radio button component on it.
			DeviceScreen screen = RadioButton.this.getScreen(); 
			screen.show();
			screen.repaint();
		}
	}
}
