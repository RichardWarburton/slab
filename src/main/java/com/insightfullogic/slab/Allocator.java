package com.insightfullogic.slab;

import java.lang.reflect.Constructor;

import com.insightfullogic.slab.implementation.AllocationHandler;
import com.insightfullogic.slab.implementation.AllocationRecorder;
import com.insightfullogic.slab.implementation.BytecodeGenerator;
import com.insightfullogic.slab.implementation.NullAllocationHandler;
import com.insightfullogic.slab.implementation.TypeInspector;
import com.insightfullogic.slab.stats.AllocationListener;

public final class Allocator<T extends Cursor> {

	private static final AllocationHandler NO_LISTENER = new NullAllocationHandler();
	
	private final TypeInspector inspector;
    private final Class<T> implementation;
	private final Constructor<T> constructor;

	private AllocationListener listener;

    private SlabOptions options;

    public static <T extends Cursor> Allocator<T> of(Class<T> representingKlass) throws InvalidInterfaceException {
        return of(representingKlass, (AllocationListener) null);
    }

    public static <T extends Cursor> Allocator<T> of(Class<T> representingKlass, AllocationListener listener) throws InvalidInterfaceException {
        return of(representingKlass, listener, SlabOptions.DEFAULT);
    }
    
    public static <T extends Cursor> Allocator<T> of(Class<T> representingKlass, SlabOptions options) throws InvalidInterfaceException {
        return new Allocator<T>(representingKlass, null, options);
    }
    
    public static <T extends Cursor> Allocator<T> of(Class<T> representingKlass, AllocationListener listener, SlabOptions options) throws InvalidInterfaceException {
        return new Allocator<T>(representingKlass, listener, options);
    }

    private Allocator(Class<T> representingKlass, AllocationListener listener, SlabOptions options) {
		this.listener = listener;
        this.options = options;
		inspector = new TypeInspector(representingKlass);
        implementation = new BytecodeGenerator<T>(inspector, representingKlass, options).generate();
        try {
        	constructor = implementation.getConstructor(Integer.TYPE, AllocationHandler.class, SlabOptions.class);
        } catch (RuntimeException e) {
        	throw e;
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	public T allocate(int count) throws InvalidSizeException {
		if (count < 1)
			throw new InvalidSizeException("You must provide a count >= 1 when allocating a slab, received " + count);

		AllocationHandler handler = listener == null ? NO_LISTENER : makeRecorder(count);

		try {
			return constructor.newInstance(count, handler, options);
		} catch (RuntimeException e) {
        	throw e;
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private AllocationRecorder makeRecorder(int count) {
		return new AllocationRecorder(this, listener, count, count * inspector.getSizeInBytes());
	}

}
