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
	 * @throws ArrayIndexOutOfBoundsException if you're trying to move to an index beyond the end of the array.
	 * NB: this is only thrown if bounds checking is enabled
	 */
	public void move(int index) throws ArrayIndexOutOfBoundsException;

	/**
	 * Return the current index of the flyweight.
	 * 
	 * @return the index that the Cursor instance is pointing at.
	 */
	public int getIndex();

	/**
	 * Resizes the slab that this cursor refers to.
	 * 
	 * @param newSize the new size of the slab
	 */
	public void resize(int newSize);

	/**
	 * Frees memory allocated to this slab.
	 */
	public void close();

}
