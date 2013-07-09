package com.insightfullogic.slab;

/**
 * Immutable and threadsafe
 */
public final class SlabOptions {
    
    public static final SlabOptions DEFAULT = builder().build();
    
    public static final int UNALIGNED = -1;
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        
        private int objectAlignment;
        private int slabAlignment;
        private boolean debug;
        
        private Builder() {
            objectAlignment = UNALIGNED;
            slabAlignment = UNALIGNED;
            debug = false;
        }
        
        public Builder setObjectAlignment(int alignment) {
            this.objectAlignment = alignment;
            return this;
        }
        
        public Builder setSlabAlignment(int alignment) {
            this.slabAlignment = alignment;
            return this;
        }

        public Builder setDebugEnabled(boolean debug) {
            this.debug = debug;
            return this;
        }
        
        public SlabOptions build() {
            return new SlabOptions(objectAlignment, slabAlignment, debug);
        }
        
    }

    private final int objectAlignment;
    private final int slabAlignment;
    private final boolean debug;

    private SlabOptions(int alignment, int slabAlignment, boolean debug) {
        this.objectAlignment = alignment;
        this.slabAlignment = slabAlignment;
        this.debug = debug;
    }

    public boolean hasObjectAlignment() {
        return objectAlignment != UNALIGNED;
    }

    public int getObjectAlignment() {
        return objectAlignment;
    }
    
    public boolean hasSlabAlignment() {
        return slabAlignment != UNALIGNED;
    }
    
    public int getSlabAlignment() {
        return slabAlignment;
    }
    
    public boolean isDebugEnabled() {
        return debug;
    }

}
