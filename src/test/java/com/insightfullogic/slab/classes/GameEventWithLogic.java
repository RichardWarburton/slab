package com.insightfullogic.slab.classes;

import com.insightfullogic.slab.ConcreteCursor;
import com.insightfullogic.slab.implementation.AllocationHandler;

public abstract class GameEventWithLogic extends ConcreteCursor {

	public GameEventWithLogic(int count, int sizeInBytes, AllocationHandler handler) {
		super(count, sizeInBytes, handler);
	}

	public abstract int getId();

	public abstract void setId(int value);

	public abstract long getStrength();

	public abstract void setStrength(long value);

	public void plusStrength(long value) {
		setStrength(getStrength() + value);
	}

	public abstract int getTarget();

	public abstract void setTarget(int value);

}
