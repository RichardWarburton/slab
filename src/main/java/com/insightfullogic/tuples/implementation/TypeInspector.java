package com.insightfullogic.tuples.implementation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.insightfullogic.tuples.InvalidInterfaceException;


public class TypeInspector {
    
    private final Class<?> klass;
    
    final List<Method> getters;
    final List<Method> setters;
    
    public TypeInspector(Class<?> klass) {
        this.klass = klass;
        getters = findGetters();
        setters = findSetters();
    }
    
	private List<Method> findGetters() {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : klass.getDeclaredMethods()) {
            if (!method.getName().startsWith("get"))
                continue;

            returnsPrimitive(method);
            hasNoParameters(method);
            methods.add(method);
        }
        return methods;
    }
	
    private void hasNoParameters(Method method) {
        if (method.getParameterTypes().length != 0)
            throw new InvalidInterfaceException(method.getName() + " is a getter with one or more parameters");
    }

    private void returnsPrimitive(Method method) {
        if (!method.getReturnType().isPrimitive())
        	throw new InvalidInterfaceException(method.getName() + " is a getter that doesn't return a primitive");
    }

    public int primitiveSize(Method method) {
        String returnType = method.getReturnType().getName();
        Types type = Types.valueOf(returnType.toUpperCase());
        return type.sizeOf;
    }
    
	private List<Method> findSetters() {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : klass.getDeclaredMethods()) {
            if (!method.getName().startsWith("set"))
                continue;

            returnsVoid(method);
            hasOnePrimitiveParameter(method);
            methods.add(method);
        }
        return methods;
	}

    private void hasOnePrimitiveParameter(Method method) {
		Class<?>[] parameters = method.getParameterTypes();
		if (parameters.length != 1)
			throw new InvalidInterfaceException(method.getName() + " is a setter with more than one parameter");
		
		if (!parameters[0].isPrimitive())
			throw new InvalidInterfaceException(method.getName() + " is a setter with a non-primitive parameter");
	}

	private void returnsVoid(Method method) {
		if (method.getReturnType() != Void.TYPE)
			throw new InvalidInterfaceException(method.getName() + " is a setter that doesn't return void");
	}

	public int getSizeInBytes() {
        int total = 0;
        for (Method getter : getters) {
            total += primitiveSize(getter);
        }
        return total;
    }

    public int getFieldCount() {
        return getters.size();
    }

}
