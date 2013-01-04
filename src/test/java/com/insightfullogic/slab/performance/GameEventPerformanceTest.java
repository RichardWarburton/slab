package com.insightfullogic.slab.performance;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.insightfullogic.slab.Allocator;
import com.insightfullogic.slab.GameEvent;

/**
 * TODO: make this test more scientific:
 * (1) Introduce warmup to remove JIT Effects
 * (2) add memory usage measurement
 */
public class GameEventPerformanceTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameEventPerformanceTest.class);
	
	private static final Allocator<GameEvent> allocator = Allocator.of(GameEvent.class);
	private static final int OBJECTS_ALLOCATED = 5 * 1000 * 1000;

	private static List<GameEventPOJO> pojos;
	private static GameEvent flyweighted;

	enum Accessor {
		POJO {
			@Override
			public GameEvent get(int i) {
				return pojos.get(i);
			}

			@Override
			public void allocate() {
				pojos = new ArrayList<GameEventPOJO>(OBJECTS_ALLOCATED);
				for (int i = 0; i < OBJECTS_ALLOCATED; i++) {
					pojos.add(new GameEventPOJO());
				}
			}
		},
		SLAB {
			@Override
			public GameEvent get(int i) {
				flyweighted.move(i);
				return flyweighted;
			}

			@Override
			public void allocate() {
				flyweighted = allocator.allocate(OBJECTS_ALLOCATED);
			}
		}
		;
		
		public abstract void allocate();
		public abstract GameEvent get(int i);
	}
 
	@Test
	public void runPerformanceTest() {
		try {
			long pojoTime = touchAllObjects(Accessor.POJO);
			long slabTime = touchAllObjects(Accessor.SLAB);
			assertTrue(slabTime < pojoTime);
		} finally {
			flyweighted.close();
		}
	}

	private long touchAllObjects(Accessor values) {
        final long start = System.nanoTime();
        values.allocate();

        final long[] lives = { 5000, 4000, 3000, 2000, 1000 };

        // Create objects
        for (int i = 0; i < OBJECTS_ALLOCATED; i++) {
			GameEvent event = values.get(i);
			event.setId(i);
			event.setStrength(i % 50);
			event.setTarget(i % 5);
		}

        final long middle = System.nanoTime();

        // Use objects
        for (int i = 0; i < OBJECTS_ALLOCATED; i++) {
			GameEvent event = values.get(i);
			int target = event.getTarget();
			long lifeRemaining = lives[target];
			if (lifeRemaining > 0) {
				lives[target] = lifeRemaining - event.getStrength();
			}
        }

        final long reads = System.nanoTime() - middle;
        final long writes = middle - start;
        LOGGER.info("Read durection of {} run is {}\n", values.name(), reads);
        LOGGER.info("Write durection of {} run is {}\n", values.name(), writes);
        return reads + writes;
	}

}
