package org.safermobile.clear.micro.ui;

import javax.microedition.lcdui.Graphics;

import org.j4me.logging.Log;
import org.j4me.ui.DeviceScreen;
import org.j4me.ui.Dialog;
import org.j4me.ui.Theme;
import org.j4me.ui.UIManager;
import org.j4me.ui.components.Label;
import org.j4me.ui.components.ProgressBar;

/**
 * This is a base class for alert screens.  It provides a background thread for
 * doing lengthy tasks such as retrieving data from the network.  While the task
 * runs this screen shows an indefinate progress bar (a spinner) with some text
 * about what operation is going on.  When the background thread completes the
 * screen dismisses itself and goes to the next screen.
 * <p>
 * Alerts have a "Cancel" button on them if the user wants to stop the operation.
 */
public abstract class ProgressAlert
	extends Dialog
	implements Runnable
{
	/**
	 * The label that displays the alert's text.
	 */
	private Label label = new Label();
	
	/**
	 * An indefinate progress bar that informs the user the application is
	 * working.
	 */
	private ProgressBar spinner;
	
	/**
	 * A flag to indicate the work done by this thread should be canceled.  If
	 * canceled, the screen returned by the worker thread will by discarded.
	 */
	private boolean canceled = false;
	
	/**
	 * Constructs an alert screen.
	 * 
	 * @param title is the alert's title.
	 * @param text is the alert message.
	 */
	public ProgressAlert (String title, String text)
	{
		// Set the title and text.
		setTitle( title );
		setText( text );
		
		// Add the label to the form.
		label.setHorizontalAlignment( Graphics.HCENTER );
		append( label );
		
		// Add an indefinate progress bar.
		spinner = new ProgressBar();
		spinner.setHorizontalAlignment( Graphics.HCENTER );
		append( spinner );
		
		// Add the menu buttons.
		Theme theme = UIManager.getTheme();
		String cancel = theme.getMenuTextForCancel(); 
		setMenuText( cancel, null );
	}
	
	/**
	 * Sets the text displayed by this alert.
	 * 
	 * @param text is the message to the user.
	 */
	public void setText (String text)
	{
		label.setLabel( text );
	}
	
	/**
	 * Returns the text displayed by this alert.
	 * 
	 * @return The message to the user.
	 */
	public String getText ()
	{
		return label.getLabel();
	}
	
	/**
	 * Launches the worker thread when the screen is shown on the device.
	 * 
	 * @see DeviceScreen#showNotify()
	 */
	public void showNotify ()
	{
		// Start the worker thread.
		Thread worker = new Thread( this );
		worker.start();
		
		// Continue processing the event.
		super.showNotify();
	}
	
	/**
	 * Goes to the next screen after the user hits the cancel button.
	 */
	protected void declineNotify ()
	{
		// Stop the background worker thread.
		cancel();
		
		// Cancel this screen.
		onCancel();

		// Continue processing the event.
		super.declineNotify();
	}

	/**
	 * Called when the user presses the alert's cancel button.
	 * <p>
	 * Implementation should override this to return to a previous screen,
	 * exit the application, etc.
	 */
	public abstract void onCancel ();
	
	/**
	 * Cancels the thread.
	 */
	public void cancel ()
	{
		canceled = true;
	}
	
	/**
	 * Executes the worker thread.  This method synchronizes with the main
	 * UI thread to avoid any race conditions involved with the order screens
	 * are set.
	 */
	public void run ()
	{
		try
		{
			// Do the background thread work.
			DeviceScreen next = doWork();
			
			// Go to the next screen.
			if ( (canceled == false) && (next != null) )
			{
				next.show();
			}
		}
		catch (Throwable t)
		{
			Log.error("Unhandled exception in UI worker thread for " + getTitle(), t);
			
			// Display the error.
			ErrorAlert error = new ErrorAlert( "Unhandled Exception", t.toString(), this, null );
			error.show();
		}
	}
	
	/**
	 * Performs the work done by the background thread.
	 * 
	 * @return The next screen to go to.  It should not return <code>null</code>.
	 */
	protected abstract DeviceScreen doWork ();
}

