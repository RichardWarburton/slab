package com.insightfullogic.slab.classes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.insightfullogic.slab.Allocator;
import com.insightfullogic.slab.InvalidInterfaceException;

public class AbstractClassesTest {

	@Test
	public void runScenario() {
		Allocator<GameEventWithLogic> allocator = Allocator.of(GameEventWithLogic.class);
		GameEventWithLogic event = allocator.allocate(3);
		event.setStrength(100);
		event.plusStrength(5);
		assertEquals(105, event.getStrength());
	}

	@Test(expected=InvalidInterfaceException.class)
	public void missingParentClass() {
		Allocator.of(ClassWithoutInherit.class);
	}

}
