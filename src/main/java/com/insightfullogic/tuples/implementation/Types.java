package com.insightfullogic.tuples.implementation;

public enum Types {
    
    INT(4),
    LONG(8);

    final int sizeOf;
    
    private Types(int size) {
        this.sizeOf = size;
    }

}
