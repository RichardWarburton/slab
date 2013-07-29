package com.insightfullogic.slab.issue13;

import org.junit.Test;

import com.insightfullogic.slab.Allocator;

public class Issue13Test {

    @SuppressWarnings("rawtypes")
    @Test
    public void reproduceIssue() {
        int reps = 100;
        Allocator eventAllocator = Allocator.of(SlabOperation.class);
        final SlabOperation op = (SlabOperation) eventAllocator.allocate(reps);
        for (int i = 0; i < reps; i++) {
          op.move(1);

          op.setMagic((byte) 0x80);
          op.setOpCode((byte) 0x09);
          op.setKeyLength((short) 3);
          op.setExtraLength((byte) 0);
          op.setDataType((byte) 0);
          op.setReserved((short) 0);
          op.setBodySize(3);
          op.setOpaque(0);
          op.setCas(0);
        }
    }

}
