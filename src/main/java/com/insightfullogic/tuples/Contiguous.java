package com.insightfullogic.tuples;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public class Contiguous<T> implements AutoCloseable {

	private static final Unsafe unsafe;

	static {
		try {
			Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
			singleoneInstanceField.setAccessible(true);
			unsafe = (Unsafe) singleoneInstanceField.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
    private final int count;
    private final long start;
    private final int sizeInBytes;
	
	Contiguous(int count, int sizeInBytes, Class<T> representing) {
		this.count = count;
		this.sizeInBytes = sizeInBytes;
		start = unsafe.allocateMemory(sizeInBytes * count);
	}

	public T get(int index) {
		if (index >= count)
			throw new IndexOutOfBoundsException("This tuple block has only " + count + " elements, cannot access index " + index);

		long objectAddress = start + (index * sizeInBytes);
		
		throw new UnsupportedOperationException();
	}

	/**
	 * Free the allocated memory
	 */
	public void close() {
		unsafe.freeMemory(start);
	}

}
