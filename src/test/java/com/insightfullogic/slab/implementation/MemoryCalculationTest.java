package com.insightfullogic.slab.implementation;

import static com.insightfullogic.slab.implementation.MemoryCalculation.calculateAddress;
import static com.insightfullogic.slab.implementation.MemoryCalculation.calculateAllocation;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.insightfullogic.slab.SlabOptions;

public class MemoryCalculationTest {
    
    private final SlabOptions slabAlignedTo32Bytes = SlabOptions.builder()
                                                                .setSlabAlignment(32)
                                                                .build();

    @Test
    public void defaultCase() {
        long allocation = calculateAllocation(2, 8, SlabOptions.DEFAULT);
        assertEquals(16, allocation);
    }

    @Test
    public void slabAllocationOversizes() {
        long allocation = calculateAllocation(6, 8, slabAlignedTo32Bytes);
        assertEquals(64, allocation);
    }
    
    @Test
    public void slabAllocationDoesntOversizeWhenUnnecessary() {
        long allocation = calculateAllocation(4, 8, slabAlignedTo32Bytes);
        assertEquals(32, allocation);
    }
    
    @Test
    public void defaultAddressCase() {
        long address = calculateAddress(30, SlabOptions.DEFAULT);
        assertEquals(30, address);
    }
    
    @Test
    public void slabAllocationOversizingMovesAddress() {
        long address = calculateAddress(30, slabAlignedTo32Bytes);
        assertEquals(32, address);
    }
    
    @Test
    public void slabAllocationDoesntOversizeAddress() {
        long address = calculateAddress(32, slabAlignedTo32Bytes);
        assertEquals(32, address);
    }

}
