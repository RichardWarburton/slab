package com.insightfullogic.slab;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

import com.insightfullogic.slab.implementation.AllocationHandler;

@SuppressWarnings("restriction")
public abstract class ConcreteCursor implements Cursor {

	private static final int NOTHING = 0;

	public static final String BOUNDS_CHECKING_PROPERTY = "slab.checkbounds";

	private static final boolean boundsChecking = "true".equals(System.getProperty(BOUNDS_CHECKING_PROPERTY));

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
	private final AllocationHandler handler;


	private int count;
	private int index;
	protected long startAddress;
	protected long pointer;

	public ConcreteCursor(int count, int sizeInBytes, AllocationHandler handler) {
		this.count = count;
		this.sizeInBytes = sizeInBytes;
		this.handler = handler;
		startAddress = unsafe.allocateMemory(sizeInBytes * count);

		move(0);
	}

	public void close() {
		if (startAddress == NOTHING)
			return;

		handler.free();
		unsafe.freeMemory(startAddress);
		startAddress = NOTHING;
	}

	public void move(int index) {
		if (boundsChecking && index >= count)
			throw new ArrayIndexOutOfBoundsException(index);
		this.index = index;
		pointer = startAddress + (sizeInBytes * index);
	}

	public int getIndex() {
		return index;
	}

	public void resize(int newCount) {
		if (newCount <= index)
			throw new InvalidSizeException("You can't resize a slab to below the index currently pointed at");

		count = newCount;
		int newSizeInBytes = sizeInBytes * newCount;
		handler.resize(newCount, newSizeInBytes);

		startAddress = unsafe.reallocateMemory(startAddress, newSizeInBytes);
		move(index);
	}

}
