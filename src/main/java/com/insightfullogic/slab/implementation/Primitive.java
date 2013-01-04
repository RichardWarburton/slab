package com.insightfullogic.slab.implementation;

import org.objectweb.asm.Opcodes;

enum Primitive implements Opcodes {

	BYTE(1, Byte.TYPE, ILOAD, IRETURN),
	SHORT(2, Short.TYPE, ILOAD, IRETURN),
    INT(4, Integer.TYPE, ILOAD, IRETURN),
    LONG(8, Long.TYPE, LLOAD, LRETURN),
	FLOAT(4, Float.TYPE, FLOAD, FRETURN),
    DOUBLE(8, Double.TYPE, DLOAD, DRETURN),
    BOOLEAN(1, Byte.TYPE, ILOAD, IRETURN),
    CHAR(2, Character.TYPE, ILOAD, IRETURN);

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

    String unsafeMethodSuffix() {
    	if (this == BOOLEAN) {
    		return "Byte";
    	}

    	String name = name();
    	return name.charAt(0) + name.substring(1).toLowerCase();
    }

	static Primitive of(Class<?> type) {
		String name = type.getName().toUpperCase();
		return Primitive.valueOf(name);
	}

}
