package com.insightfullogic.slab.lib;

import java.util.Iterator;

import com.insightfullogic.slab.Cursor;

public class SlabIterator<T extends Cursor> implements Iterator<T> {

    public static <T extends Cursor> SlabIterator<T> of(T cursor) {
        return new SlabIterator<T>(cursor);
    }

    private final T cursor;

    private int index;

    public SlabIterator(T cursor) {
        this.cursor = cursor;
        index = cursor.getIndex();
    }

    public boolean hasNext() {
        return index < cursor.getCount();
    }

    public T next() {
        cursor.move(++index);
        return cursor;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

}
