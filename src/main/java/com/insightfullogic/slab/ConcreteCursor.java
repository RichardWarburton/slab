package com.insightfullogic.slab;

import static com.insightfullogic.slab.implementation.MemoryCalculation.calculateAddress;
import static com.insightfullogic.slab.implementation.MemoryCalculation.calculateAllocation;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

import com.insightfullogic.slab.implementation.AllocationHandler;
import com.insightfullogic.slab.implementation.MemoryCalculation;

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
	private final SlabOptions options;

	private int numberOfObjects;
	private int index;
	
	protected long allocatedAddress;
	protected long startAddress;
	protected long pointer;


	public ConcreteCursor(int numberOfObjects, int sizeInBytes, AllocationHandler handler, SlabOptions options) {
		this.numberOfObjects = numberOfObjects;
		this.sizeInBytes = sizeInBytes;
		this.handler = handler;
        this.options = options;
        allocatedAddress = unsafe.allocateMemory(calculateAllocation(numberOfObjects, sizeInBytes, options));
        startAddress = calculateAddress(allocatedAddress, options);

		move(0);
	}

	public void close() {
		if (allocatedAddress == NOTHING)
			return;

		handler.free();
		unsafe.freeMemory(allocatedAddress);
		allocatedAddress = NOTHING;
		startAddress = NOTHING;
	}

	public void move(int index) {
		if (boundsChecking && index >= numberOfObjects)
			throw new ArrayIndexOutOfBoundsException(index);
		this.index = index;
		pointer = startAddress + (sizeInBytes * index);
	}

	public int getIndex() {
		return index;
	}

	public void resize(int newNumberOfObjects) {
		if (newNumberOfObjects <= index)
			throw new InvalidSizeException("You can't resize a slab to below the index currently pointed at");

		numberOfObjects = newNumberOfObjects;
		long newSizeInBytes = calculateAllocation(newNumberOfObjects, sizeInBytes, options);
		handler.resize(newNumberOfObjects, newSizeInBytes);

		allocatedAddress = unsafe.reallocateMemory(startAddress, newSizeInBytes);
		startAddress = calculateAddress(allocatedAddress, options);
		move(index);
	}

	public int getNumberOfObjects() {
	    return numberOfObjects;
	}

}
