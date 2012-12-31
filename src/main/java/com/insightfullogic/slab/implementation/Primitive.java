package com.insightfullogic.slab.implementation;

import org.objectweb.asm.Opcodes;

// TODO: add sizes for other types
enum Primitive implements Opcodes {

    INT(4, Integer.TYPE, ILOAD, IRETURN),
    LONG(8, Long.TYPE, LLOAD, LRETURN);

    final int sizeInBytes;
    final Class<?> javaEquivalent;
	int loadOpcode;
	int returnOpcode;

    private Primitive(int size, Class<?> javaType, int loadOpcode, int returnOpcode) {
        this.sizeInBytes = size;
		this.javaEquivalent = javaType;
		this.loadOpcode = loadOpcode;
		this.returnOpcode = returnOpcode;
    }
    
    String camelCaseName() {
    	String name = name();
    	return name.charAt(0) + name.substring(1).toLowerCase();
    }

	static Primitive of(Class<?> type) {
		String name = type.getName().toUpperCase();
		return Primitive.valueOf(name);
	}

}
