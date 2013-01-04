package com.insightfullogic.slab.implementation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insightfullogic.slab.InvalidInterfaceException;

public class TypeInspector {
    
    private final Class<?> klass;
    
    final List<Method> getters;
    final Map<String, Method> setters;
    
    public TypeInspector(Class<?> klass) {
        this.klass = klass;
        getters = findGetters();
        setters = findSetters();
        if ((getters.size() + setters.size()) != klass.getDeclaredMethods().length)
			throw new InvalidInterfaceException(klass.getName() + " has methods that are neither getters nor setters");
    }

	private List<Method> findGetters() {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : klass.getDeclaredMethods()) {
            String name = method.getName();
			if (!name.startsWith("get"))
                continue;

			doesntUseIndex(name);
            returnsPrimitive(method);
            hasNoParameters(method);
            methods.add(method);
        }
        return methods;
    }
	
    private void doesntUseIndex(String name) {
		if ("getIndex".equals(name))
			throw new InvalidInterfaceException("You can't declare an index field, since that name is used by Slab");
	}

	private void hasNoParameters(Method method) {
        if (method.getParameterTypes().length != 0)
            throw new InvalidInterfaceException(method.getName() + " is a getter with one or more parameters");
    }

    private void returnsPrimitive(Method method) {
        if (!method.getReturnType().isPrimitive())
        	throw new InvalidInterfaceException(method.getName() + " is a getter that doesn't return a primitive");
    }

    Primitive getReturn(Method method) {
        return Primitive.of(method.getReturnType());
    }

	private Map<String, Method> findSetters() {
		Map<String, Method> methods = new HashMap<String, Method>();
        for (Method method : klass.getDeclaredMethods()) {
            if (!method.getName().startsWith("set"))
                continue;

            returnsVoid(method);
            hasOnePrimitiveParameter(method);
            methods.put(method.getName(), method);
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
            total += getReturn(getter).sizeInBytes;
        }
        return total;
    }

    public int getFieldCount() {
        return getters.size();
    }

	public Method setterFor(Method getter) {
		String name = getter.getName().replaceFirst("get", "set");
		Method method = setters.get(name);
		if (method == null)
			throw new InvalidInterfaceException("Unable to find setter with name: " + name);
		return method;
	}

}
