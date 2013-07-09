package com.insightfullogic.slab;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SlabOptionsTest {

    @Test
    public void builderBuildsFromFields() {
        SlabOptions options = SlabOptions.builder()
                                     .setDebugEnabled(true)
                                     .setObjectAlignment(64)
                                     .setSlabAlignment(8)
                                     .build();
        
        assertEquals(64, options.getObjectAlignment());
        assertEquals(8, options.getSlabAlignment());
        assertEquals(true, options.isDebugEnabled());
        assertEquals(true, options.hasObjectAlignment());
        assertEquals(true, options.hasSlabAlignment());
    }

}
