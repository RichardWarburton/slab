package com.insightfullogic.tuples.implementation;

import static com.insightfullogic.tuples.implementation.Types.LONG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.insightfullogic.tuples.examples.GameEvent;

public class TypeInspectionTest {
    
    private static final TypeInspector inspector = new TypeInspector(GameEvent.class);

    @Test
    public void findsGetters() {
        assertEquals(3, inspector.getters.size());
        
        List<String> methods = getNames(inspector.getters);
        assertTrue(methods.contains("getStrength"));
        assertTrue(methods.contains("getTarget"));
        assertTrue(methods.contains("getId"));
    }
    
    @Test
    public void findsSetters() {
        assertEquals(3, inspector.setters.size());
        
        List<String> methods = getNames(inspector.setters);
        assertTrue(methods.contains("setStrength"));
        assertTrue(methods.contains("setTarget"));
        assertTrue(methods.contains("setId"));
    }

    private List<String> getNames(List<Method> methods) {
    	List<String> names = new ArrayList<String>();
    	for (Method getter : methods) {
			names.add(getter.getName());
		}
    	return names;
    }

    @Test
    public void correctFieldSize() throws Exception {
        Method getStrength = GameEvent.class.getMethod("getStrength");
        assertEquals(LONG.sizeOf, inspector.primitiveSize(getStrength));
    }

    @Test
    public void tupleSize() throws Exception {
        assertEquals(20, inspector.getSizeInBytes());
    }

    @Test
    public void fieldCount() throws Exception {
        assertEquals(3, inspector.getFieldCount());
    }

}
