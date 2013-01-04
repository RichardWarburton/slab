package com.insightfullogic.slab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.insightfullogic.slab.Allocator;

public class GameEventScenarioTest {

	@Test
	public void eventScenario() {
		Allocator<GameEvent> eventAllocator = Allocator.of(GameEvent.class);

		GameEvent event = eventAllocator.allocate(100);
		assertNotNull(event);
		assertEquals(0, event.getIndex());

		event.setId(5);
		event.setStrength(100);
		event.setTarget(2);

		event.move(1);
		assertEquals(1, event.getIndex());

		event.setId(6);
		event.setStrength(101);
		event.setTarget(3);

		assertEquals(6, event.getId());
		assertEquals(101, event.getStrength());
		assertEquals(3, event.getTarget());

		event.move(0);
		assertEquals(0, event.getIndex());

		assertEquals(5, event.getId());
		assertEquals(100, event.getStrength());
		assertEquals(2, event.getTarget());
	}
	
	@Test
	public void resizing() {
		Allocator<GameEvent> eventAllocator = Allocator.of(GameEvent.class);
		GameEvent event = eventAllocator.allocate(1);
		event.move(0);
		event.resize(2);
		event.move(1);
	}

}
