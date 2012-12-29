package com.insightfullogic.tuples.implementation;

public class CodeGen<T> {
    
    private final TypeInspector inspector;
    private final Class<T> interfase;
    
    public CodeGen(TypeInspector inspector, Class<T> interfase) {
        this.inspector = inspector;
        this.interfase = interfase;
    }

    public Class<T> generate() {
        return null;
    }

}
