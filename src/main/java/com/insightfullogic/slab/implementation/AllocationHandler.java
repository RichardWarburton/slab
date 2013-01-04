package com.insightfullogic.slab.implementation;


public interface AllocationHandler {

	public void free();

	public void resize(int size, long sizeInBytes);

}
