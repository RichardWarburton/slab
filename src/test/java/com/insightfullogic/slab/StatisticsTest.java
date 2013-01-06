package com.insightfullogic.slab;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.insightfullogic.slab.stats.AllocationListener;

public class StatisticsTest {

	private static long allocated = 0;
	private static long resized = 0;
	private static long freed = 0;

	private static final AllocationListener COUNTER = new AllocationListener() {
		public void onResize(Allocator<?> by, int size, long sizeInBytes) {
			resized += sizeInBytes;
		}

		public void onFree(Allocator<?> by, int size, long sizeInBytes) {
			freed += sizeInBytes;
		}

		public void onAllocation(Allocator<?> by, int size, long sizeInBytes) {
			allocated += sizeInBytes;
		}
	};

	@Test
	public void stats() {
		Allocator<GameEvent> eventAllocator = Allocator.of(GameEvent.class, COUNTER);
		GameEvent event = eventAllocator.allocate(1);
		try {
			assertEquals(16, allocated);

			event.resize(2);
			assertEquals(16, resized);
		} finally {
			event.close();
		}
		assertEquals(32, freed);
	}

}
