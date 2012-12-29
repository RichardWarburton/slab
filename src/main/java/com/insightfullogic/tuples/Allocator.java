package com.insightfullogic.tuples;

import com.insightfullogic.tuples.implementation.CodeGen;
import com.insightfullogic.tuples.implementation.TypeInspector;

/**
 * An array of Tuples of <T>.
 */
public final class Allocator<T extends Cursor> {

	private final Class<T> representingKlass;
	private final TypeInspector inspector;
    private final Class<T> implementation;

    public static <T extends Cursor> Allocator<T> of(Class<T> representingKlass) throws InvalidInterfaceException {
        return new Allocator<T>(representingKlass);
    }

    private Allocator(Class<T> representingKlass) {
        this.representingKlass = representingKlass;
		inspector = new TypeInspector(representingKlass);
        implementation = new CodeGen<T>(inspector, representingKlass).generate();
    }

	public T allocate(int count) {
		try {
			return implementation.getConstructor(Integer.TYPE).newInstance(count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
