package com.insightfullogic.slab;

import java.lang.reflect.Constructor;

import com.insightfullogic.slab.implementation.BytecodeGenerator;
import com.insightfullogic.slab.implementation.TypeInspector;

public final class Allocator<T extends Cursor> {

	private final TypeInspector inspector;
    private final Class<T> implementation;
	private Constructor<T> constructor;

    public static <T extends Cursor> Allocator<T> of(Class<T> representingKlass) throws InvalidInterfaceException {
        return new Allocator<T>(representingKlass);
    }

    private Allocator(Class<T> representingKlass) {
		inspector = new TypeInspector(representingKlass);
        implementation = new BytecodeGenerator<T>(inspector, representingKlass).generate();
        try {
        	constructor = implementation.getConstructor(Integer.TYPE);
        } catch (RuntimeException e) {
        	throw e;
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	public T allocate(int count) throws InvalidSizeException {
		if (count < 1)
			throw new InvalidSizeException("You must provide a count >= 1 when allocating a slab, received " + count);
		try {
			return constructor.newInstance(count);
		} catch (RuntimeException e) {
        	throw e;
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
