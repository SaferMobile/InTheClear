package org.j4me.ui;

import java.util.*;
import javax.microedition.lcdui.*;
import org.j4me.ui.components.*;
import org.j4me.util.*;

/**
 * The <code>Dialog</code> class is a base class for any screen that accepts user input
 * using standard components like the text box.  It is similar to the MIDP <code>Form</code> class
 * except that it uses the J4ME control flow and has an OK and Cancel button built in.
 * <p>
 * Form classes can only have component objects, like the text box or label, placed on them.
 * The class handles layout, painting, and management of the components.
 * If you need direct control over the appearance of the screen use the <code>DeviceScreen</code>
 * class.
 * 
 * @see javax.microedition.lcdui.Form
 */
public abstract class Dialog
	extends DeviceScreen
{
	/**
	 * The collection of all components on this form.  Components are displayed
	 * as ordered from top to bottom.
	 */
	private Vector components = new Vector();
	
	/**
	 * The index of <code>component</code> the user currently has highlighted.
	 * If this is not an index of <code>component</code> nothing is highlighted.
	 * Any component can be highlighted; it does not have to accept user input
	 * (<code>Component.acceptsInput()</code>).  It is up the component itself
	 * to decide if it should be painted differently when highlighted.
	 */
	private int highlightedComponent = -1;
	
	/**
	 * The number of pixels between the left and right of the screen and the
	 * components.  This also pads the top and bottom of the form; however,
	 * scrolling can make the text appear at the very top and bottom of the
	 * screen.
	 */
	private int margin = 5;

	/**
	 * The number of pixels above and below components.
	 */
	private int spacing = margin;
	
	/**
	 * The offset into the form that has been scrolled down.  0 indicates
	 * the very top of the form is shown at the top of the screen.  100
	 * would indicate that the current top of the screen (i.e. y == 0)
	 * shows the 100th pixel down in the form.  If the first component
	 * were 80 pixels tall and the second 40, 100 would mean the bottom
	 * half of the second component would be at the top of the screen.
	 */
	private int topOfScreen = 0;
	
	/**
	 * The number of pixels wide each component is.
	 * <p>
	 * When the form is painted for the first time this number will be
	 * calculated.  If the components on the form are changed, this will be
	 * reset back to <code>null</code>.
	 * <p>
	 * The last element in this array is a dummy element.
	 */
	private int[] componentWidths = null;
	
	/**
	 * The number of pixels high the entire form is with all the components
	 * on it.  If this number is greater than the height of the screen, a
	 * vertical scrollbar will be added to the side.
	 * <p>
	 * When the form is painted for the first time this number will be
	 * calculated.  If the components on the form are changed, this will be
	 * reset back to <code>null</code>.
	 * <p>
	 * The last element in this array is a dummy element.  Its value helps
	 * determine the bottom of the last component.
	 */
	private int[] absoluteHeights = null;
	
	/**
	 * A flag indicating if the screens layout is no longer valid.  It will
	 * be calculated on the next <code>paint</code>.
	 * <p>
	 * This flag is an optimization.  Many invalidations can happen between
	 * paintings.  Recalculating the layout is expensive so we just set this
	 * flag to <code>true</code> many times instead.  Then do just a single
	 * layout calculation at the end.
	 */
	boolean invalidated;
	
	/**
	 * Implicitly called by derived classes to setup a new J4ME form.
	 */
	public Dialog ()
	{
		super();
		
		// Set the default menu options.
		Theme theme = UIManager.getTheme();
		String cancel = theme.getMenuTextForCancel();
		String ok = theme.getMenuTextForOK();
		setMenuText( cancel, ok );
	}

	/**
	 * Called immediately before this screen is replaced by another screen.
	 * <p>
	 * Notifies all the components they are hidden.  Classes that override
	 * this method should be sure to call <code>super.onDeselection</code>.
	 * 
	 * @see DeviceScreen#hideNotify()
	 */
	public void hideNotify ()
	{
		// Hide all the components on the screen.
		Enumeration e = components.elements();
		
		while ( e.hasMoreElements() )
		{
			Component c = (Component)e.nextElement();
			c.visible( false );
		}
		
		// Continue deselection.
		super.hideNotify();
	}

	/**
	 * Adds the <code>component</code> to the end of this form.
	 * 
	 * @param component is the UI component to add to the bottom of the form.
	 */
	public void append (Component component)
	{
		invalidate();
		components.addElement( component );
	}
	
	/**
	 * Inserts the <code>component</code> in this form at the specified <code>index</code>.
	 * Each component on this form with an index greater or equal to the
	 * specified <code>index</code> is shifted upward to have an index one greater
	 * than the value it had previously.
	 * 
	 * @param component is the UI component to insert.
	 * @param index is where to insert <code>component</code> on the form.  It must
	 *  greater than or equal to 0 and less than or equal to the number of
	 *  components already on the form.
	 * @throws ArrayIndexOutOfBoundsException if the index was invalid.
	 */
	public void insert (Component component, int index)
	{
		invalidate();
		components.insertElementAt( component, index );
	}
	
	/**
	 * Sets the <code>component</code> in this form at the specified <code>index</code>.
	 * The previous component at that position is discarded.
	 * 
	 * @param component is the UI component to set to.
	 * @param index is where to set <code>component</code> on the form.  It must
	 *  greater than or equal to 0 and less than the number of
	 *  components already on the form.
	 * @throws ArrayIndexOutOfBoundsException if the index was invalid.
	 */
	public void set (Component component, int index)
	{
		invalidate();
		components.setElementAt( component, index );
	}
	
	/**
	 * Removes the first occurrence of the <code>component</code> from this form.
	 * If <code>component</code> is found on this form, each component on the form
	 * with an index greater or equal to the <code>component</code>'s index is
	 * shifted downward to have an index one smaller than the value it had
	 * previously.
	 * 
	 * @param component is the UI component to remove.
	 */
	public void delete (Component component)
	{
		int index = components.indexOf( component );
		delete( index );
	}
	
	/**
	 * Deletes the <code>component</code> at the specified <code>index</code>.  Each
	 * component in this vector with an index greater or equal to the
	 * specified index is shifted downward to have an index one smaller
	 * than the value it had previously.
	 *  
	 * @param index is the index of the component to remove.  It must be
	 *  a value greater than or equal to 0 and less than the current
	 *  number of components on the form.
	 * @throws ArrayIndexOutOfBoundsException if the index was invalid.
	 */
	public void delete (int index)
	{
		if ( index >= 0 )
		{
			components.removeElementAt( index );
			
			if ( highlightedComponent == index )
			{
				highlightedComponent = -1;
			}
			else if ( highlightedComponent > index )
			{
				highlightedComponent--;
			}
			
			invalidate();
		}
	}
	
	/**
	 * Removes all of the components from this form.
	 */
	public void deleteAll ()
	{
		components.removeAllElements();
		highlightedComponent = -1;
		invalidate();
	}
	
	/**
	 * Returns an enumeration of the components on this form.
	 * 
	 * @return An enumeration of the components on this form.
	 */
	public Enumeration components ()
	{
		return components.elements();
	}
	
	/**
	 * Returns the number of components on this form.
	 * 
	 * @return The number of components on this form.
	 */
	public int size ()
	{
		return components.size();
	}
	
	/**
	 * Returns the component at the specified index.
	 * 
	 * @param index is the value into the component list to get.
	 * @return The component at <code>index</code> or <code>null</code> if the
	 *  index is invalid.
	 */
	public Component get (int index)
	{
		Component c = null;
		
		if ( (index >= 0) && (index < components.size()) )
		{
			c = (Component)components.elementAt( index );
		}
		
		return c; 
	}
	
	/**
	 * Returns the index of the currently selected component.  It is
	 * the one the user can currently enter data into.
	 * 
	 * @return The index of the component on the form that is currently
	 *  selected.
	 */
	public int getSelected ()
	{
		if ( highlightedComponent < 0 )
		{
			return 0;
		}
		else
		{
			return highlightedComponent;
		}
	}
	
	/**
	 * Returns the index of the component that contains the given pixel.
	 * If no component is at that location, for example it is in the
	 * spacing between components, then <code>-1</code> is returned.
	 * <p>
	 * This method assumes that the click is relative to the form
	 * area.  It may not address the title bar, menu bar, or a
	 * scroll bar.
	 * 
	 * @param x is the X-coordinate of the pixel on the form.
	 * @param y is the Y-coordinate of the pixel on the form.
	 * @return The index of the component containing the pixel
	 *  (<code>x</code>, <code>y</code>) or <code>-1</code> if no component contains it.
	 */
	private int getAt (int x, int y)
	{
		int matched = -1;
		
		// Get the absolute position of y on the form.
		int absY = topOfScreen + y;
		
		// Walk the list of component positions until absY is found.
		for ( int i = 0; i < absoluteHeights.length - 1; i++ )
		{
			// Get the dimensions of this component.
			int top = absoluteHeights[i];
			int topOfNext = absoluteHeights[i + 1];
			int bottom = topOfNext - spacing;
			
			int left = margin;
			int right = left + componentWidths[i];
			
			// Does the point fall within this component?
			if ( (absY >= top) && (absY < bottom) &&
				 (x >= left) && (x < right) )
			{
				// This is the component that (x, y) falls within.
				matched = i;
			}
			
			// Stop processing if point is above the top of the component.
			if ( absY < topOfNext )
			{
				break;
			}
		}
		
		return matched;
	}
	
	/**
	 * Sets the selected component.  It is the one the user can input
	 * data into and that the screen is scrolled to.
	 * 
	 * @param index is the new selected component.
	 */
	public void setSelected (int index)
	{
		if ( (index < 0) || (index >= components.size()) )
		{
			throw new IndexOutOfBoundsException( String.valueOf(index) );
		}
		
		highlightedComponent = index;

		// Scroll screen to this component.
		if ( absoluteHeights == null )
		{
			calculateLayout( UIManager.getTheme(), getWidth(), getHeight() );
		}
		
		// Set the top of the screen. 
		if ( index == 0 )
		{
			// The top of the screen will be the very start.
			topOfScreen = 0;
		}
		else
		{
			// The top of the screen will be the top of the component.
			topOfScreen = absoluteHeights[index] - spacing;  
		}
		
		// Adjust the top of the screen for scrolling.
		int maxScroll = absoluteHeights[absoluteHeights.length - 1] - getHeight();
		
		if ( maxScroll <= 0 )
		{
			// All the components fit on one form.
			topOfScreen = 0;
		}
		else if ( topOfScreen > maxScroll )
		{
			// Scroll all the way to the bottom.  The highlighted 
			// component will be visible, but not at the top of the page.
			topOfScreen = maxScroll;
		}
	}
	
	/**
	 * Sets the selected component.  It is the one the user can input
	 * data into and that the screen is scrolled to.
	 * 
	 * @param component is the new selected component.  If it is not
	 *  on the form this has no effect.
	 */
	public void setSelected (Component component)
	{
		int index = 0;
		
		// Walk the list of components until we find it.
		Enumeration e = components.elements();
		
		while ( e.hasMoreElements() )
		{
			Component c = (Component)e.nextElement();

			if ( c == component )
			{
				// This is the component.
				break;
			}
			
			index++;
		}
		
		// Set the component as the selected one.
		if ( index < size() )
		{
			setSelected( index );
		}
	}

	/**
	 * Paints the form and its components.  The layout is calculated from the
	 * components and their order.
	 * 
	 * @param g is the <code>Graphics</code> object to paint with.
	 */
	protected synchronized void paint (Graphics g)
	{
		// Have we determined the layout?
		if ( invalidated )
		{
			layout();
			invalidated = false;
		}
		
		// Get the height of the screen.
		Theme theme = UIManager.getTheme();
		int height = getHeight();
		
		// Add a vertical scrollbar?
		if ( hasVerticalScrollbar() )
		{
			// Paint the scrollbar.
			int width = super.getWidth();  // Exclude the margins and scrollbar
			int heightOfAllComponents = absoluteHeights[absoluteHeights.length - 1];
			paintVerticalScrollbar( g, 0, 0, width, height, topOfScreen, heightOfAllComponents );
		}

		// Get the rest of the form dimensions.
		int formWidth = getWidth();
		int bottomOfScreen = topOfScreen + height;
		
		// Walk the list of components and paint them.
		Enumeration list = components.elements();
		
		for ( int i = 0; i < absoluteHeights.length - 1; i++ )
		{
			Component c = (Component)list.nextElement();
			int componentTop = absoluteHeights[i];
			int componentBottom = absoluteHeights[i + 1] - spacing; 
			
			// Is the component visible on the screen?
			if ( componentTop >= bottomOfScreen )
			{
				// Skip drawing components below the screen.
				c.visible( false );
			}
			else if ( componentBottom <= topOfScreen )
			{
				// Skip drawing components above the screen.
				c.visible( false );
			}
			else  // visible
			{
				c.visible( true );
				
				// Calculate the position of the component.
				int componentX = margin;
				int componentY = componentTop - topOfScreen;
				int componentWidth = formWidth;
				int componentHeight = componentBottom - componentTop;
				
				// Paint this component.
				if ( intersects(g, componentX, componentY, componentWidth, componentHeight) )
				{
					boolean selected = (i == highlightedComponent);
					
					c.paint( g, theme, this,
							 componentX, componentY,
							 componentWidth, componentHeight,
							 selected );
				}
			}
		}
	}
	
	/**
	 * Paints the vertical scrollbar.  The scrollbar must go on the right
	 * side of the form and span from the top to the bottom.  Its width
	 * is returned from this method and used to calculate the width of
	 * the remaining form area to draw components in.
	 *
	 * @param g is the <code>Graphics</code> object to paint with.
	 * @param x is the top-left X-coordinate pixel of the form area.
	 * @param y is the top-left Y-coordinate pixel of the form area.
	 * @param width is the width of the form area in pixels.
	 * @param height is the height of the form area in pixels.
	 * @param offset is the vertical scrolling position of the top pixel
	 *  to show on the form area.
	 * @param formHeight is the total height of all the components on the
	 *  form.  This is bigger than <code>height</code>.
	 */
	protected void paintVerticalScrollbar (Graphics g, int x, int y, int width, int height, int offset, int formHeight)
	{
		UIManager.getTheme().paintVerticalScrollbar( g, x, y, width, height, offset, formHeight );
	}
	
	/**
	 * Returns the margin for the left and right of the screen.  This
	 * also pads the top and bottom of the form; however, scrolling can
	 * make the text appear at the very top and bottom of the screen.
	 * 
	 * @return The number of pixels between components and the edges
	 *  of the screen.
	 */
	public int getMargin ()
	{
		return margin;
	}
	
	/**
	 * Sets the margin for the left and right of the screen.  This
	 * also pads the top and bottom of the form; however, scrolling can
	 * make the text appear at the very top and bottom of the screen.
	 * 
	 * @param margin is the number of pixels between components and
	 *  the edges of the screen.  Values less than 0 are ignored.
	 */
	public void setMargin (int margin)
	{
		if ( (margin >= 0) && (this.margin != margin) )
		{
			this.margin = margin;
			
			// Recalculate the layout with the new margins later.
			invalidate();
		}
	}
	
	/**
	 * Returns the vertical spacing between components.
	 * 
	 * @return The number of pixels that vertically separate components.
	 */
	public int getSpacing ()
	{
		return spacing;
	}
	
	/**
	 * Sets the vertical spacing between components. 
	 * 
	 * @param spacing is the number of pixels that vertically separates
	 *  components.  Values less than 0 are be ignored.
	 */
	public void setSpacing (int spacing)
	{
		if ( (spacing >= 0) && (this.spacing != spacing) )
		{
			this.spacing = spacing;
			
			// Recalculate the layout with the new spacings later.
			invalidate();
		}
	}
	
	/**
	 * Returns the width of the usuable portion of this form.  The usable
	 * portion excludes the vertical scrollbar on the right of the screen
	 * (if it exists).
	 * 
	 * @return The number of pixels wide the usable portion of the form is.
	 */
	public int getWidth ()
	{
		int canvasWidth = super.getWidth();
		int formWidth = canvasWidth - 2 * margin;
		
		if ( hasVerticalScrollbar() )
		{
			int scrollbarWidth = UIManager.getTheme().getVerticalScrollbarWidth();
			formWidth -= scrollbarWidth;
		}

		return formWidth;
	}
	
	/**
	 * Determines if the height of all the components is greater than the height
	 * of the screen.  If so a veritical scrollbar will be drawn on the form.
	 * 
	 * @return <code>true</code> if the form has a vertical scrollbar and <code>false</code>
	 *  if it does not.
	 */
	public synchronized boolean hasVerticalScrollbar ()
	{
		int screenHeight = getHeight();
		int formWidth = super.getWidth() - 2 * margin;
		
		boolean layoutJustCalculated = false;
		Theme theme = UIManager.getTheme();
		
		// Have we determined the layout?
		if ( invalidated || (absoluteHeights == null) )
		{
			calculateLayout( theme, formWidth, screenHeight );
			layoutJustCalculated = true;
		}
		
		// Are all the components taller than the screen?
		if ( absoluteHeights[absoluteHeights.length - 1] > screenHeight )
		{
			if ( layoutJustCalculated )
			{
				// Recalculate the layout now that we're adding a vertical scrollbar.
				formWidth -= theme.getVerticalScrollbarWidth();
				calculateLayout( theme, formWidth, screenHeight );
			}
			
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Forces the layout of all components to be recalculated.  This should
	 * be called whenever this screen is altered.
	 */
	public void invalidate ()
	{
		// We must calculate the layout before the next paint.
		invalidated = true;
	}
	
	/**
	 * Lays out the components on the screen.  This also keeps the
	 * scroll position so the user sees the same components after
	 * being called.
	 */
	private synchronized void layout ()
	{
		// Record the current screen position.
		int component = 0, delta = 0;
		
		if ( absoluteHeights != null )
		{
			if ( highlightedComponent >= 0 )
			{
				component = highlightedComponent;
			}

			delta = absoluteHeights[component] - topOfScreen;
		}
		
		// Remove all component location calculations.
		componentWidths = null;
		absoluteHeights = null;
		topOfScreen = 0;
		
		// Recalculate the position of all components.
		hasVerticalScrollbar();
		
		// Reset the screen to its original position.
		if ( component != 0 )
		{
			highlightedComponent = component;
			topOfScreen = absoluteHeights[component] - delta;
		}
	}
	
	/**
	 * Goes through all of the components and determines their vertical
	 * positioning.  The form's overall height, the sum of all the
	 * components, is set in the <code>height</code> member variable.
	 * 
	 * @param theme is the application's current <code>Theme</code>.
	 * @param widht is the number of pixels wide the screen is.
	 * @param height is the number of pixels high the screen is.
	 */
	private synchronized void calculateLayout (Theme theme, int width, int height)
	{
		componentWidths = new int[components.size() + 1];  // Dummy element at bottom for end of components
		absoluteHeights = new int[componentWidths.length];
		int componentY = margin;  // First component is "margin" from the top
		
		// Scroll through the components determining the vertical position of each one.
		Enumeration list = components.elements();
		
		for ( int i = 0; list.hasMoreElements(); i++ )
		{
			absoluteHeights[i] = componentY;
			
			// Calculate the position of the next component.
			Component c = (Component)list.nextElement();
			
			int[] dimensions = c.getPreferredSize( theme, width, height );
			
			componentWidths[i] = dimensions[0];
			componentY += dimensions[1] + spacing;
		}
		
		// Set the first component that can be highlighted.
		if ( highlightedComponent < 0 )
		{
			highlightedComponent = 0;
		}
		
		// Add a dummy last element to record the bottom of all components.
		absoluteHeights[absoluteHeights.length - 1] = componentY;
	}

	/**
	 * Returns the next component that <code>highlightedComponent</code>
	 * should be set to.  The way it is selected is:
	 * <ol>
	 *  <li>If two adjacent components accept user input, then that next 
	 *      component would be returned.
	 *  <li>If two components that accept user input are separated by ones
	 *      that do not, then we'd still skip to that next one that does.
	 *  <li>The exception is when there are enough components that accept
	 *      user input between the two that more than a screen's height
	 *      is between them.  In that case we need to set one of the
	 *      intermediate components so the screen does not scroll too far.
	 * </ol>
	 * 
	 * @param down when <code>true</code> means the next component with a
	 *  higher index; when <code>false</code> means the next with a lower.
	 * @param maxScroll is the maximum number of pixels that can be scrolled
	 *  to get to the next component.  For example it may be 90% the height
	 *  of the screen.
	 * @return The index of the next component to highlight.
	 */
	private int nextHighlightableComponent (boolean down, int maxScroll)
	{
		if ( down )
		{
			// How far down can the component be?
			int maxBottom;
			
			if ( highlightedComponent < 0 )
			{
				maxBottom = topOfScreen;
			}
			else
			{
				maxBottom = topOfScreen + getHeight();
			}

			maxBottom += maxScroll;
			
			// Walk through the components until we find the next one to highlight.
			int components = size();
			
			for ( int next = highlightedComponent + 1; next < components; next++ )
			{
				// Otherwise have we walked off the end of the screen?
				int nextBottom = absoluteHeights[next + 1];
				
				if ( nextBottom > maxBottom )
				{
					if ( next - 1 == highlightedComponent )
					{
						// Move at least to the next component, even if it cannot
						// be completely shown because it is bigger than the screen.
						return next;
					}
					else
					{
						// Show the one before next so it can be completely
						// shown on the screen.
						return next - 1;
					}
				}
				else
				{
					// If this component accepts input stop on it.
					Component c = get( next );
					
					if ( c.acceptsInput() )
					{
						return next;
					}
				}
			}
			
			// If we made it here highlight the last component.
			return components - 1;
		}
		else  // up
		{
			if ( highlightedComponent > 0 )
			{
				// How far up can the component be?
				int maxTop = topOfScreen - maxScroll;

				// Walk through the components until we find the next one to highlight.
				for ( int next = highlightedComponent - 1; next >= 0; next-- )
				{
					// Otherwise have we walked off the end of the screen?
					int nextTop = absoluteHeights[next];
					
					if ( nextTop < maxTop )
					{
						if ( next + 1 == highlightedComponent )
						{
							// Move at least to the next component, even if it cannot
							// be shown because it is bigger than the screen.
							return next;
						}
						else
						{
							// Show the one before next so it can be completely
							// shown on the screen.
							return next + 1;
						}
					}
					else
					{
						// If this component accepts input stop on it.
						Component c = get( next );
						
						if ( c.acceptsInput() )
						{
							return next;
						}
					}
				}
			}
			
			// If we made it here highlight the first component.
			return 0;
		}
	}
	
	/**
	 * Moves the viewport of the form up or down and calls <code>repaint</code>.
	 * <p>
	 * The amount scrolled is dependent on the components shown.  Typically
	 * one scroll increment advances to the next component.  However, if
	 * components are taller than the screen they will be partially scrolled.
	 * 
	 * @param down is <code>true</code> when the form should scroll down and 
	 *  <code>false</code> when it should scroll up.
	 */
	private void scroll (boolean down)
	{
		// Safety checks.
		if ( absoluteHeights == null )
		{
			// Calculated the layout.
			hasVerticalScrollbar();
		}
		
		if ( components.size() == 0 )
		{
			// Can't scroll with no components.
			return;
		}
		
		// Get the dimensions of the form.
		int topOfForm = 0;
		int screenHeight = getHeight();
		int bottomOfForm = absoluteHeights[absoluteHeights.length - 1] - screenHeight;
		int bottomOfScreen = topOfScreen + screenHeight;
		
		// We scroll 90% of the screen unless there is another highlightable
		// component within that 90%.  In which case we scroll only to the
		// highlightable component, not the full 90%.
		int max = screenHeight * 9 / 10;

		// Get the next component that can be highlighted.
		int current = highlightedComponent;
		highlightedComponent = nextHighlightableComponent( down, max );

		// Scroll.
		if ( hasVerticalScrollbar() )
		{
			// Calculate the number of pixels to scroll the form.
			int scroll;

			// Calculate how far to scroll to get to the next highlighted component.
			if ( down )
			{
				int currentBottom = absoluteHeights[current + 1];
				
				if ( currentBottom > bottomOfScreen )
				{
					// The current component actually is clipped by the bottom of
					// the screen (because it is too big to show all at once).
					// Just scroll down we can see more of it.
					scroll = max;
				}
				else
				{
					// Get the screen position of the next highlightable component.
					int nextTop = absoluteHeights[highlightedComponent];
					int nextBottom = absoluteHeights[highlightedComponent + 1];
					
					if ( (nextTop > topOfScreen) && (nextBottom < bottomOfScreen) )
					{
						// Don't scroll if the next highlighted component fits
						// completely on the screen already.
						scroll = 0;
					}
					else
					{
						scroll = nextBottom - currentBottom;
					}
				}
			}
			else  // up
			{
				int currentTop = absoluteHeights[current];
				
				if ( currentTop < topOfScreen )
				{
					// The current component actually is clipped by the top of
					// the screen (because it is too big to show all at once).
					// Just scroll up we can see more of it.
					scroll = max;
				}
				else
				{
					if ( highlightedComponent == 0 )
					{
						// Scroll to the very top of the dialog.
						scroll = topOfScreen;
					}
					else
					{
						// Get the bottom of the component above the next highlighted one.
						int previousBottom = absoluteHeights[highlightedComponent] - spacing;
						
						scroll = topOfScreen - previousBottom;
						
						// Don't scroll if the next highlighted component fits
						// completely on the screen already.
						if ( previousBottom >= topOfScreen )
						{
							scroll = 0;
						}
					}
				}
			}
			
			// Set the position of the form.
			if ( scroll > max )
			{
				scroll = max;
			}
	
			if ( down == false )
			{
				// If scrolling up, set the scroll to a negative number.
				scroll *= -1;
			}
			
			topOfScreen += scroll;
			
			if ( topOfScreen < topOfForm )
			{
				topOfScreen = topOfForm;
			}
			else if ( topOfScreen > bottomOfForm )
			{
				topOfScreen = bottomOfForm;
			}
		}
		
		// Redraw the screen at the scrolled position.
		repaint();
	}
	
	/**
	 * Shows or hides the menu bar at the bottom of the screen.
	 * 
	 * @param mode is <code>true</code> if the <code>DeviceScreen</code> is to be in full
	 *  screen mode, <code>false</code> otherwise.
	 */
	public void setFullScreenMode (boolean mode)
	{
		super.setFullScreenMode( mode );
		invalidate();
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
		super.setMenuText( left, right );
		invalidate();
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
		super.setTitle( title );
		invalidate();
	}

	/**
	 * Called when a key is pressed.  It can be identified using the
	 * constants defined in this class.
	 * 
	 * @param keyCode is the key code of the key that was pressed.
	 */
	protected void keyPressed (int keyCode)
	{
		// Forward the event to the current component.
		Component c = get( highlightedComponent );
		
		if ( c != null )
		{
			c.keyPressed( keyCode );
		}
		
		// Scrolling vertically?
		if ( keyCode == UP )
		{
			scroll( false );
		}
		else if ( keyCode == DOWN )
		{
			scroll( true );
		}

		// Continue processing the event.
		super.keyPressed( keyCode );
	}

	/**
	 * Called when a key is repeated (held down).  It can be identified using the
	 * constants defined in this class.
	 * 
	 * @param keyCode is the key code of the key that was held down.
	 */
	protected void keyRepeated (int keyCode)
	{
		// Forward the event to the current component.
		Component c = get( highlightedComponent );
		
		if ( c != null )
		{
			c.keyRepeated( keyCode );
		}
				
		// Continue processing the event.
		super.keyRepeated( keyCode );
	}

	/**
	 * Called when a key is released.  It can be identified using the
	 * constants defined in this class.
	 * 
	 * @param keyCode is the key code of the key that was released.
	 */
	protected void keyReleased (int keyCode)
	{
		// Forward the event to the current component.
		Component c = get( highlightedComponent );
		
		if ( c != null )
		{
			c.keyReleased( keyCode );
		}
		
		// Continue processing the event.
		super.keyReleased( keyCode );
	}

	/**
	 * Called when the pointer is pressed.
	 * 
	 * @param x is the horizontal location where the pointer was pressed.
	 * @param y is the vertical location where the pointer was pressed.
	 */
	protected void pointerPressed (int x, int y)
	{
		// Is this event moving the scrollbar?
		boolean movedScrollbar = false;
		
		if ( hasVerticalScrollbar() )
		{
			// Was the click over the scrollbar?
			int screenWidth = getScreenWidth();
			int scrollbarWidth = UIManager.getTheme().getVerticalScrollbarWidth();
			int scrollbarX = screenWidth - scrollbarWidth; 
			
			if ( x >= scrollbarX )
			{
				// The user clicked on the scrollbar.
				movedScrollbar = true;
				
				// Calculate the height of the trackbar.
				int height = getHeight();
				int formHeight = absoluteHeights[absoluteHeights.length - 1];
				
				int scrollableHeight = formHeight - height;
				double trackbarPercentage = (double)height / (double)formHeight;
				int trackbarHeight = (int)MathFunc.round( height * trackbarPercentage );
				trackbarHeight = Math.max( trackbarHeight, 2 * scrollbarWidth );

				// Calculate the range and location of the trackbar.
				//   The scrollbar doesn't actually go from 0% to 100%.  The top
				//   is actually 1/2 the height of the trackbar from the top of
				//   the screen.  The bottom is 1/2 the height from the bottom.
				int rangeStart = trackbarHeight / 2;
				int range = height - 2 * rangeStart;
				
				double offsetPercentage = (double)topOfScreen / (double)scrollableHeight;
				int center = rangeStart + (int)MathFunc.round( offsetPercentage * range );
				
				if ( y < center )
				{
					// Move the scrollbar up one component.
					scroll( false );
				}
				else
				{
					// Move the scrollbar down one component.
					scroll( true );
				}
			}
		}
		
		// Forward the event to the component clicked on.
		if ( movedScrollbar == false )
		{
			// Highlight the component.
			highlightedComponent = getAt( x, y );
			
			// Forward the event to the component for processing.
			Component c = get( highlightedComponent );
			
			if ( c != null )
			{
				// Adjust the click position relative to the component.
				int px = x - c.getX();
				int py = y - c.getY();
				
				c.pointerPressed( px, py );
			}
		}
		
		// Continue processing the event.
		super.pointerPressed( x, y );
	}
	
	/**
	 * Called when the pointer is dragged.
	 * 
	 * @param x is the horizontal location where the pointer was dragged.
	 * @param y is the vertical location where the pointer was dragged.
	 */
	protected void pointerDragged (int x, int y)
	{
		// Forward the event to the current component.
		Component c = get( highlightedComponent );
		
		if ( c != null )
		{
			c.pointerDragged( x, y );
		}
		
		// Continue processing the event.
		super.pointerDragged( x, y );
	}

	/**
	 * Called when the pointer is released.
	 * 
	 * @param x is the horizontal location where the pointer was released.
	 * @param y is the vertical location where the pointer was released.
	 */
	protected void pointerReleased (int x, int y)
	{
		// Forward the event to the current component.
		Component c = get( highlightedComponent );
		
		if ( c != null )
		{
			c.pointerReleased( x, y );
		}
		
		// Continue processing the event.
		super.pointerReleased( x, y );
	}
}
