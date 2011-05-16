package org.j4me.ui;

/**
 * Implementing classes can be used as menu choices in a <code>Menu</code>.
 */
public interface MenuItem
{
	/**
	 * @return The text displayed by the menu.
	 */
	public String getText ();
	
	/**
	 * Called when the user selects this choice from the menu.
	 */
	public void onSelection ();
}
