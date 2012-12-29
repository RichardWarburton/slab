package com.insightfullogic.tuples.implementation;

// TODO: add sizes for other types
public enum Types {

    INT(4),
    LONG(8);

    final int sizeOf;

    private Types(int size) {
        this.sizeOf = size;
    }

}
