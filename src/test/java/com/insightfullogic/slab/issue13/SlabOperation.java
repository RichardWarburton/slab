package com.insightfullogic.slab.issue13;

import com.insightfullogic.slab.Cursor;


public interface SlabOperation extends Cursor {

    public byte getMagic();

    public void setMagic(byte magic);

    public byte getOpCode();

    public void setOpCode(byte opCode);

    public short getKeyLength();

    public void setKeyLength(short keyLength);

    public byte getExtraLength();

    public void setExtraLength(byte extraLength);

    public byte getDataType();

    public void setDataType(byte dataType);

    public short getReserved();

    public void setReserved(short reserved);

    public int getBodySize();

    public void setBodySize(int bodySize);

    public int getOpaque();

    public void setOpaque(int opaque);

    public long getCas();

    public void setCas(long cas);
}
