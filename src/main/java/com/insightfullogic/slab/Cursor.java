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
