package com.insightfullogic.slab.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insightfullogic.slab.Allocator;


public class AllocationLogger implements AllocationListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(AllocationLogger.class);

	public void onAllocation(Allocator<?> by, int size, long sizeInBytes) {
		LOGGER.info(sizeInBytes + " allocated");
	}

	public void onResize(Allocator<?> by, int size, long sizeInBytes) {
		LOGGER.info("resize to " + sizeInBytes);
	}

	public void onFree(Allocator<?> by, int size, long sizeInBytes) {
		LOGGER.info(sizeInBytes + " freed");
	}

}
