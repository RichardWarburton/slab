package com.insightfullogic.slab.implementation;

import java.lang.reflect.Field;

import org.objectweb.asm.Type;

import sun.misc.Unsafe;

import com.insightfullogic.slab.Cursor;
import com.insightfullogic.slab.InvalidSizeException;

@SuppressWarnings("restriction")
public abstract class DirectMemoryCursor implements Cursor {
	
	static final String INTERNAL_NAME = Type.getInternalName(DirectMemoryCursor.class);
	
	protected static final Unsafe unsafe;

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
	private int index;
	protected final long startAddress;
	
	protected long pointer;

	public DirectMemoryCursor(int count, int sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
		startAddress = unsafe.allocateMemory(sizeInBytes * count);

		move(0);
	}

	public void close() {
		unsafe.freeMemory(startAddress);
	}

	public void move(int index) {
		this.index = index;
		pointer = startAddress + (sizeInBytes * index);
	}

	public int getIndex() {
		return index;
	}

	public void resize(int newSize) {
		if (newSize <= index)
			throw new InvalidSizeException("You can't resize a slab to below the index currently pointed at");

		unsafe.reallocateMemory(startAddress, sizeInBytes * newSize);
	}

}
