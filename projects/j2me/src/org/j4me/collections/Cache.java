package org.j4me.collections;

import java.util.*;

/**
 * A least recently used (LRU) cache.  Data is
 * stored internally in a hashtable, which maps keys to values.  Once
 * the cache is full and a new entry is added, the least recently used
 * entry is discarded.  Therefore a cache is like a hashtable except
 * it stops growing at a certain point.
 * <p>
 * Any non-null object can be used as a key or as a value.
 * To successfully store and retrieve objects from a cache, the objects
 * used as keys must implement the hashCode method and the equals method.
 * 
 * @see java.util.Hashtable
 */
public class Cache
{
	/**
	 * The maximum number of objects that can be stored in the cache.
	 * When adding a new item and the cache already has this many items,
	 * the least recently used will be removed from the cache.
	 */
	private int max;

	/**
	 * The LRU cache.  The key is always a <code>Terrain</code> object and the
	 * data is an <code>Item</code>.  The <code>Item</code> data structure maintains
	 * a list of the order in which they are used.
	 */
	private Hashtable cache;

	/**
	 * The most recently used cached <code>Item</code>.  This will be <code>null</code>
	 * only when the cache is empty.
	 */
	private Item mru;

	/**
	 * The least recently used cached <code>Item</code>.  This will be <code>null</code>
	 * only when the cache is empty.
	 */
	private Item lru;

	/**
	 * A data structure for each object stored in <code>cache</code>.  It contains
	 * the <code>key</code> and <code>data</code> used in any map.  It also has pointers
	 * to keep a list in order of how recently each <code>Item</code> object has
	 * been accessed.
	 */
	private static final class Item
	{
		/**
		 * The key used to lookup this <code>Item</code> in the hash table.
		 */
		public Object key;

		/**
		 * The cached data associated with the <code>key</code>.
		 */
		public Object data;

		/**
		 * The next in the list which is less recently used than this.
		 */
		public Item next;

		/**
		 * The previous in the list which is more recently used than this.
		 */
		public Item previous;
	}

	/**
	 * Constructs the cache.
	 * 
	 * @param maxCapacity is the number of key/value pairs that can be stored
	 *  before adding new entries ejects the least recently used ones.
	 */
	public Cache (int maxCapacity)
	{
		cache = new Hashtable( maxCapacity * 2 );  // Adjust for load factor
		mru = null;
		lru = null;

		setMaxCapacity( maxCapacity );
	}

	/**
	 * Clears this cache so that it contains no keys.
	 */
	public void clear ()
	{
		cache.clear();
		mru = null;
		lru = null;
	}

	/**
	 * Returns the number of keys in this cache.
	 * 
	 * @return The number of keys in this cache.
	 */
	public int size ()
	{
		return cache.size();
	}

	/**
	 * Returns the maximum number of keys that can be stored in this
	 * cache.  The value of <code>size</code> can never be greater than this
	 * number.
	 * 
	 * @return The maximum number of keys that can be stored in this
	 *  cache.
	 */
	public int getMaxCapacity ()
	{
		return max;
	}

	/**
	 * Sets the maximum number of keys that can be stored in this
	 * cache.
	 * <p>
	 * The value of <code>size</code> can never be greater than this
	 * number.  If the maximum capicity is shrinking and too many
	 * elements are already in the cache, the least recently used
	 * ones will be discarded until <code>size</code> is the same as
	 * <code>maxCapacity</code>.
	 * 
	 * @param maxCapacity is the total number of keys that can be
	 *  stored in the cache.
	 */
	public void setMaxCapacity (int maxCapacity)
	{
		if ( maxCapacity < 0 )
		{
			// The cache cannot contain a negative number of elements.
			throw new IllegalArgumentException();
		}

		// Remove entries so the cache size is no more than its capacity.
		for ( int i = cache.size() - maxCapacity; i > 0; i-- )
		{
			// Kick out the least recently used element.
			cache.remove( lru.key );

			lru.previous.next = null;
			lru = lru.previous;
		}

		// Record the new maximum cache size.
		max = maxCapacity;
	}

	/**
	 * Adds an <code>Object</code> to the cache that is associated with <code>key</code>.
	 * The new item will become the most recently used.  If the cache is full it
	 * will replace the least recently used entry.
	 * 
	 * @param key is the indexing object.
	 * @param data is the object to cache.
	 */
	public void add (Object key, Object data)
	{
		if ( key == null )
		{
			// The key cannot be null.
			throw new IllegalArgumentException();
		}

		if ( max > 0 )
		{
			Item item = new Item();
			int cacheSize = cache.size();

			// Sanity check.
			if ( cacheSize > max )
			{
				// This can only happen if access to the cache was not synchronized.
				// The cache itself does not synchronization to improve performance.
				// If you see this application you should add syncronized() blocks
				// around the cache.
				throw new IllegalStateException();
			}
			
			// Is the item being added already cached?
			Object existing = get( key );
			
			if ( existing != null )
			{
				// The key has already been used.  By calling get() we already promoted
				// it to the MRU spot.  However, if the data has changed, we need to
				// update it in the hash table.
				if ( existing != data )
				{
					Item i = (Item)cache.get( key );
					i.data = data;
				}
			}
			else  // cache miss
			{
				// Add the new data.
				
				// Is the cache is full?
				if ( cacheSize == max )
				{
					// Kick out the least recently used element.
					cache.remove( lru.key );
		
					if ( lru.previous != null )
					{
						lru.previous.next = null;
					}
					
					lru = lru.previous;
				}
				
				// Store the new item as the most recently used.
				item.key = key;
				item.data = data;
				item.next = mru;
				item.previous = null;
		
				if ( cache.size() == 0 )  // then cache is empty
				{
					lru = item;
				}
				else
				{
					mru.previous = item;
				}
		
				mru = item;
				cache.put( key, item );
			}
		}
	}

	/**
	 * Gets a cached <code>Object</code> associated with <code>key</code>.
	 * 
	 * @param key is the indexing object.
	 * @return The <code>Object</code> associated with <code>key</code>; <code>null</code> if
	 *  <code>key</code> is not in the cache.
	 */
	public Object get (Object key)
	{
		if ( key == null )
		{
			// The key cannot be null.
			throw new IllegalArgumentException();
		}

		// Get the cached item.
		Object o = cache.get( key );

		if ( o == null )  // Cache miss
		{
			return null;
		}
		else  // Cache hit
		{
			// Make this the most recently used entry.
			Item item = (Item)o;

			if ( mru != item )  // then not already the MRU 
			{
				if ( lru == item )  // I'm the least recently used
				{
					lru = item.previous;
				}

				// Remove myself from the LRU list.
				if ( item.next != null )
				{
					item.next.previous = item.previous;
				}

				item.previous.next = item.next;

				// Add myself back in to the front.
				mru.previous = item;
				item.previous = null;
				item.next = mru;
				mru = item;
			}

			// Return the cached data.
			return item.data;
		}
	}
}
