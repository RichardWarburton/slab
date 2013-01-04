package com.insightfullogic.slab;

import com.insightfullogic.slab.Cursor;

public interface GameEvent extends Cursor {

    public int getId();

    public void setId(int value);

    public long getStrength();

    public void setStrength(long value);

    public int getTarget();
    
    public void setTarget(int value);

}
