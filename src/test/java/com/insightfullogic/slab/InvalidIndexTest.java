package com.insightfullogic.slab;

import org.junit.Test;

import com.insightfullogic.slab.Allocator;
import com.insightfullogic.slab.InvalidSizeException;

public class InvalidIndexTest {

	@Test(expected=InvalidSizeException.class)
	public void countIsNaturalNumber() {
		Allocator.of(GameEvent.class).allocate(0);
	}

	@Test(expected=InvalidSizeException.class)
	public void cantResizeBelowIndex() {
		GameEvent event = Allocator.of(GameEvent.class).allocate(5);
		event.move(4);
		event.resize(1);
	}

}
