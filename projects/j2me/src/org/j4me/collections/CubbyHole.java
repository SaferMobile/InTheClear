package org.j4me.collections;

/**
 * Stores a single object for the producer/consumer pattern and takes care
 * of thread synchronization.  A first thread, the producer, can put an object
 * into the cubby hole using <code>set</code>.  If there already is an object
 * in the cubby hole then it is discarded.  Meanwhile a second thread, the
 * consumer, can get the object using <code>get</code>.  If no object is in
 * the cubby hole the consumer will block until one is available.
 * <p>
 * <code>CubbyHole</code> is valuable for several situations including where
 * a lot of information is produced and consumption is time consuming.  For
 * example an application that does expensive rendering based on location
 * events could only render based on the very latest location.   
 */
public class CubbyHole
{
	/**
	 * The object stored by the producer.  If this is <code>null</code>
	 * there is nothing to consume.
	 */
	private Object cubby;

	/**
	 * Called by the producer to put <code>data</code> into the cubby hole.
	 * If there was another object stored in the cubby hole it will be
	 * removed and returned.
	 * <p>
	 * This is a thread-safe method that returns immediately.  If another
	 * thread, acting as the consumer, is blocking on <code>get</code> it
	 * will start running so long as <code>data</code> is not <code>null</code>.
	 *  
	 * @param data is the information to store in the cubby hole.  If
	 *  <code>null</code> then there is no job and any calls to <code>get</code>
	 *  will block until <code>set</code> is called again with a non-
	 *  <code>null</code> object.
	 * @return The object in the cubby hole replaced by <code>data</code>
	 *  or <code>null</code> if nothing was stored.
	 */
	public synchronized Object set (Object data)
	{
		Object ret = cubby;
		cubby = data;

		// Unblock a consumer waiting on get().
		notifyAll();
		
		return ret;
	}

	/**
	 * Called by the consumer to get an object stored in the cubby hole.
	 * If nothing is stored this thread will block until <code>set</code>
	 * is called by a different thread.
	 * 
	 * @return The object stored in the cubby hole by a call to <code>set</code>.
	 *  This will never return <code>null</code>.
	 * @throws InterruptedException if the program is exiting.
	 */
	public synchronized Object get ()
		throws InterruptedException
	{
		// Block until a job is available.
		while ( cubby == null )
		{
			wait();  // Releases the lock on this when waiting and re-acquires when awaken
		}

		// Get the data in the cubby hole.
		Object ret = cubby;
		cubby = null;

		return ret;
	}

	/**
	 * Looks at the cubby hole without removing the object from it.  This
	 * is a non-blocking method.
	 *  
	 * @return The object in the cubby hole which will be <code>null</code>
	 *  if nothing is being stored.
	 */
	public synchronized Object peek ()
	{
		return cubby;
	}

	/**
	 * Test is the cubby hole is empty.  This is a non-blocking method.
	 * 
	 * @return <code>true</code> if nothing is in the cubby hole or <code>
	 *  false</code> if it has an object.
	 */
	public synchronized boolean empty ()
	{
		return (cubby == null);
	}
}
