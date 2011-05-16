package org.j4me.util;

import java.io.*;
import javax.microedition.io.*;
import org.j4me.logging.*;

/**
 * Substitute for <code>Connector.open</code> that forces a timeout on all platforms.
 * <p>
 * The <code>(StreamConnection) Connector.open(url, Connector.READ, true)</code> call
 * is not guaranteed to timeout.  On some phones this will cause indefinate
 * blocking and make the application unresponsive.  To get around this the
 * connection must be made on a secondary thread that is killed after some
 * timeout period has expired.
 * 
 * @see javax.microedition.io.Connector#open(java.lang.String, int, boolean)
 */
public class ConnectorHelper
	extends Thread
{
	/**
	 * The URL to connect to.
	 */
	private final String url;

	/**
	 * The access mode.  It is either <code>Connector.READ</code>,
	 * <code>Connector.WRITE</code>, or <code>Connector.READ_WRITE</code>.
	 */
	private final int mode;

	/**
	 * The timeout, in milliseconds, for the connection.
	 */
	private final int timeout;

	/**
	 * The returned connection object. This is <code>null</code> until a
	 * successful connection is established.
	 */
	private StreamConnection connection;

	/**
	 * Any exception encountered during the connection process. This could mean
	 * the device is turned off (<code>ConnectionNotFoundException</code>) or a
	 * simple time out (<code>IOException</code>). It could also mean we were not
	 * allowed access to the API (<code>SecurityException</code>).
	 */
	private Exception exception;

	/**
	 * Constructs a thread that tries to establish a connection to a
	 * device.
	 * 
	 * @param url is the URL to try connecting to.
	 * @param mode is the access mode.  It is either <code>Connector.READ</code>,
	 *  <code>Connector.WRITE</code>, or <code>Connector.READ_WRITE</code>.
	 * @param timeout is how long, in milliseconds, to try connecting before
	 *  giving up. On Sun's emulator this is 10,000 milliseconds.
	 */
	private ConnectorHelper (String url, int mode, int timeout)
	{
		super( "ConnectorHelper" );
		
		this.url = url;
		this.mode = mode;
		this.timeout = timeout;
	}

	/**
	 * Executed by thread to establish the connection when the owner
	 * calls <code>start</code>.
	 */
	public synchronized void run ()
	{
		// Try connecting to the URL.
		//   This may not ever return depending on the JVM. It is
		//   killed by the parent who will unblock after the timeout
		//   interval and is responsible for calling the <code>interrupt</code>
		//   method on this thread.
		try
		{
			connection = (StreamConnection) Connector.open( url, mode, true );
		}
		catch (IOException e)
		{
			// IOException usually means it timed out.
			//   Could be a ConnectionNotFoundException meaning the device is
			//   turned off.  Sometimes the platform throws this instead of a
			//   SecurityException.
			Log.warn("Problem opening connection to " + url, e);
			exception = e;
		}
		catch (SecurityException e)
		{
			Log.error("Not allowed to open connection", e);
			exception = e;
		}
		catch (Exception e)
		{
			Log.error("Unknown connection exception", e);
			exception = e;
		}
		
		// Notify the owning thread we are done.
		notifyAll();
	}

	/**
	 * The owner should call this method to block until the connection is either
	 * established, has timed out, or encountered some other connection
	 * exception.
	 * 
	 * @return A connection to the device; <code>null</code> if it timed
	 *  out and was unsuccessful.
	 * @throws IOException - If some other kind of I/O error occurs.
	 * @throws SecurityException - May be thrown if access to the protocol
	 *  handler is prohibited.
	 */
	public synchronized StreamConnection blockUntilConnected ()
		throws IOException, SecurityException
	{
		// Block for the timeout period or until the connection attempt returns.
		try
		{
			wait( timeout );
		}
		catch (InterruptedException e)
		{
			// The program is exiting.
		}
		
		// If an exception was encountered throw it back to the main thread.
		if ( exception != null )
		{
			if ( exception instanceof ConnectionNotFoundException )
			{
				throw (ConnectionNotFoundException) exception;
			}
			else if ( exception instanceof SecurityException )
			{
				throw (SecurityException) exception;
			}
			else if ( exception instanceof IOException )
			{
				throw (IOException) exception;
			}
			else if ( exception instanceof IllegalArgumentException )
			{
				throw (IllegalArgumentException) exception;
			}
			else
			{
				throw (RuntimeException) exception;
			}
		}
		
		// Return the connection object. This can be null if the
		// connection timed out.
		return connection;
	}
	
	/**
	 * Create and open a <code>Connection</code>.
	 * 
	 * @param url is the URL for the connection.
	 * @param mode is the access mode.  It is either <code>Connector.READ</code>,
	 *  <code>Connector.WRITE</code>, or <code>Connector.READ_WRITE</code>.
	 * @param timeout is the the number of milliseconds before the open
	 *  attempt times out.
	 * @return A new <code>Connection</code> object.
	 * @throws IllegalArgumentException if a parameter is invalid.
	 * @throws ConnectionNotFoundException if the target of the name cannot be
	 *  found, or if the requested protocol type is not supported.
	 * @throws IOException if some other kind of I/O error occurs.
	 * @throws SecurityException may be thrown if access to the protocol
	 *  handler is prohibited.  Some platforms throw <code>IOException</code> instead
	 *  to try to be MIDP 1.0 compatible.
	 * 
	 * @see javax.microedition.io.Connector#open(java.lang.String, int, boolean)
	 */
	public static Connection open (String url, int mode, int timeout)
		throws IllegalArgumentException, ConnectionNotFoundException, IOException, SecurityException
	{
		// Create a worker thread that establishes the connection.
		ConnectorHelper thread = new ConnectorHelper( url, mode, timeout );
		thread.start();  

		// Connect.  Exceptions will be thrown from here.
		StreamConnection connection = thread.blockUntilConnected();
		
		if ( connection == null )
		{
			// The connection thread timed out.
			thread.interrupt();
            throw new ConnectionNotFoundException("Connection attempt timed out to " + url);
		}
		
		return connection;
	}
}
