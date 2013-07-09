package com.insightfullogic.slab.implementation;

import com.insightfullogic.slab.SlabOptions;

public class MemoryCalculation {
    
    public static long calculateAllocation(int numberOfObjects, int sizeInBytes, SlabOptions options) {
        long baseSize = calculateBaseSize(numberOfObjects, sizeInBytes);
        
        return realignSizeOrAddress(options, baseSize);
    }

    private static long realignSizeOrAddress(SlabOptions options, long baseSize) {
        if(!options.hasSlabAlignment()) {
            return baseSize;
        }

        long misalignment = calculateMisAlignment(options, baseSize);
        if (misalignment == 0)
            return baseSize;
        
        return calculateRealignedSize(options, baseSize, misalignment);
    }

    private static long calculateRealignedSize(SlabOptions options, long baseSize, long misalignment) {
        long overrun = options.getSlabAlignment() - misalignment;
        return baseSize + overrun;
    }

    private static long calculateMisAlignment(SlabOptions options, long baseSize) {
        return baseSize % options.getSlabAlignment();
    }

    private static long calculateBaseSize(int numberOfObjects, long sizeInBytes) {
        return sizeInBytes * numberOfObjects;
    }

    public static long calculateAddress(long allocatedAddress, SlabOptions options) {
        return realignSizeOrAddress(options, allocatedAddress);
//        if (!options.hasSlabAlignment())
//            return allocatedAddress;
//        
//        int slabAlignment = options.getSlabAlignment();
//        long misalignment = allocatedAddress % slabAlignment;
//        if (misalignment == 0)
//            return allocatedAddress;
//        
//        long overrun = slabAlignment - misalignment;
//        return allocatedAddress + overrun;
    }

}
