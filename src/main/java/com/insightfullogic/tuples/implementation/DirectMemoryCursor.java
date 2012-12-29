package com.insightfullogic.tuples.implementation;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

import com.insightfullogic.tuples.Cursor;

@SuppressWarnings("restriction")
public abstract class DirectMemoryCursor implements Cursor {
	
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
	
	private final int sizeInBytes;
	protected final long startAddress;
	
	protected long pointer;

	public DirectMemoryCursor(int count, int sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
		startAddress = unsafe.allocateMemory(sizeInBytes * count);
		
		// Initially point at object index 0
		pointer = startAddress;
	}

	public void close() throws Exception {
		unsafe.freeMemory(startAddress);
	}

	public void move(int index) {
		pointer = startAddress + (sizeInBytes * index);
	}

}
