package com.insightfullogic.slab.lib;

import com.insightfullogic.slab.Allocator;
import com.insightfullogic.slab.Cursor;

public class HashSlab<K, V extends Cursor> {

    public static <K, V extends Cursor> HashSlab<K, V> of(int size, Allocator<V> values) {
        return new HashSlab<K, V>(size, values);
    }

    private final V values;
    private final int size;

    public HashSlab(int size, Allocator<V> values) {
        this.size = size;
        this.values = values.allocate(size);
    }

    // TODO: determine best collision avoidance strategy
    public V lookup(K key) {
        int index = key.hashCode() % size;
        values.move(index);
        return values;
    }

    public int getSize() {
        return size;
    }

}
