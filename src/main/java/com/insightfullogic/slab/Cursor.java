package com.insightfullogic.slab;

import java.io.Closeable;

/**
 * A cursor defines the API to access a slab.
 * Its a flyweight pointing at a specific element of the slab.
 */
public interface Cursor extends Closeable {

	/**
	 * Move to the specified index.
	 * 
	 * @param index the index to move to.
	 */
	public void move(int index);
	
	/**
	 * Frees memory allocated to this slab.
	 */
	public void close();

}
