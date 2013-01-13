package com.insightfullogic.slab.performance;

import java.util.HashMap;
import java.util.Map;

import com.insightfullogic.slab.Allocator;
import com.insightfullogic.slab.Cursor;
import com.insightfullogic.slab.lib.HashSlab;

public class HashSlabPerformanceRun {

    public interface FloatWrapper extends Cursor {
        public float getValue();
        public void setValue(float value);
    }

    public static void main(String[] args) {
        Allocator<FloatWrapper> floats = Allocator.of(FloatWrapper.class);
        int N = 10000000;

        for (int j = 0; j < 10; ++j) {
            long startTime = System.currentTimeMillis();
            HashSlab<Integer, FloatWrapper> hashtable = HashSlab.of(N, floats);

            for (int i = 1; i <= N; i++) {
                hashtable.lookup(i).setValue(1.0f / i);
            }

            System.out.println("m[100] = " + hashtable.lookup(100).getValue());

            long time = System.currentTimeMillis() - startTime;
            System.out.println("Slab Took: " + time / 1e3 + "s");
        }
        

        for (int j = 0; j < 10; ++j) {
            long startTime = System.currentTimeMillis();
            Map<Integer, Float> hashtable = new HashMap<Integer, Float>();

            for (int i = 1; i <= N; i++) {
                hashtable.put(i, 1.0f / i);
            }

            System.out.println("m[100] = " + hashtable.get(100));
            long time = System.currentTimeMillis() - startTime;
            System.out.println("Hashmap Took: " + time / 1e3 + "s");
        }
    }

}
