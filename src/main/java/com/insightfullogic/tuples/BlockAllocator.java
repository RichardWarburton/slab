package com.insightfullogic.tuples;

import com.insightfullogic.tuples.implementation.CodeGen;
import com.insightfullogic.tuples.implementation.Constructor;
import com.insightfullogic.tuples.implementation.TypeInspector;


/**
 * An array of Tuples of <T>.
 */
public final class BlockAllocator<T> {

	private final Class<T> representingKlass;
	private final TypeInspector inspector;
    private final Class<T> implementation;
    private final Constructor<T> constructor;

    public static <T> BlockAllocator<T> of(Class<T> representingKlass) throws InvalidInterfaceException {
        return new BlockAllocator<T>(representingKlass);
    }

    private BlockAllocator(Class<T> representingKlass) {
        this.representingKlass = representingKlass;
		inspector = new TypeInspector(representingKlass);
        implementation = new CodeGen<T>(inspector, representingKlass).generate();
        constructor = null; // TODO
    }

	public Contiguous<T> allocate(int count) {
		return new Contiguous<T>(count, inspector.getSizeInBytes(), representingKlass);
	}

}
