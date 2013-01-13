package com.insightfullogic.slab.lib;

import com.insightfullogic.slab.Cursor;

public class SlabIterable<T extends Cursor> implements Iterable<T> {
    
    public static <T extends Cursor> SlabIterable<T> of(T cursor) {
        return new SlabIterable<T>(cursor);
    }
    
    private final T cursor;

    public SlabIterable(T cursor) {
        this.cursor = cursor;
    }

    public SlabIterator<T> iterator() {
        return SlabIterator.of(cursor);
    }

}
