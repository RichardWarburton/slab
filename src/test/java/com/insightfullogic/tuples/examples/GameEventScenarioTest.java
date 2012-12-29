package com.insightfullogic.tuples.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.insightfullogic.tuples.Contiguous;
import com.insightfullogic.tuples.BlockAllocator;

public class GameEventScenarioTest {

	@Test
	public void eventScenario() {
		BlockAllocator<GameEvent> tuples = BlockAllocator.of(GameEvent.class);

		Contiguous<GameEvent> events = tuples.allocate(100);
		assertNotNull(events);

		GameEvent event = events.get(0);
		event.setId(5);
		event.setStrength(100);
		event.setTarget(2);

		GameEvent theSameEvent = events.get(0);
		assertEquals(5, theSameEvent.getId());
		assertEquals(100, theSameEvent.getStrength());
		assertEquals(2, theSameEvent.getTarget());
	}

}
