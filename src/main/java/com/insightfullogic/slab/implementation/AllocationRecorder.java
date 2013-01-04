package com.insightfullogic.slab.implementation;

import com.insightfullogic.slab.Allocator;
import com.insightfullogic.slab.stats.AllocationListener;

public class AllocationRecorder implements AllocationHandler {

	private final AllocationListener listener;
	private final Allocator<?> by;

	private int size;
	private long sizeInBytes;

	public AllocationRecorder(Allocator<?> by, AllocationListener listener, int size, long sizeInBytes) {
		this.by = by;
		this.listener = listener;
		this.size = size;
		this.sizeInBytes = sizeInBytes;
		listener.onAllocation(by, size, sizeInBytes);
	}

	public void free() {
		listener.onFree(by, size, sizeInBytes);
	}

	public void resize(int size, long sizeInBytes) {
		int sizeDelta = size - this.size;
		long sizeInBytesDelta = sizeInBytes - this.sizeInBytes;
		this.size = size;
		this.sizeInBytes = sizeInBytes;
		listener.onResize(by, sizeDelta, sizeInBytesDelta);
	}

}
