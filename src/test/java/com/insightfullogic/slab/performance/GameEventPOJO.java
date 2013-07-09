package com.insightfullogic.slab.performance;

import com.insightfullogic.slab.GameEvent;


public class GameEventPOJO implements GameEvent {
	
	private int id;
	private long strength;
	private int target;
	
	public GameEventPOJO() {}

	public void move(int index) {
		throw new UnsupportedOperationException();
	}
	
	public int getIndex() {
		throw new UnsupportedOperationException();
	}

	public void close() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getStrength() {
		return strength;
	}

	public void setStrength(long strength) {
		this.strength = strength;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public void resize(int size) {
		throw new UnsupportedOperationException();
	}

    public int getNumberOfObjects() {
        // TODO Auto-generated method stub
        return 0;
    }

}
