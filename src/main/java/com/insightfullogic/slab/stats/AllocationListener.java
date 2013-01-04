package com.insightfullogic.slab.stats;

import com.insightfullogic.slab.Allocator;

public interface AllocationListener {

	public void onAllocation(Allocator<?> by, int size, long sizeInBytes);
	
	public void onResize(Allocator<?> by, int size, long sizeInBytes);
	
	public void onFree(Allocator<?> by, int size, long sizeInBytes);

}
