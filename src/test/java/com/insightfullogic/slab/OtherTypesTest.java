package com.insightfullogic.slab;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Test;

public class OtherTypesTest {
	
	public static interface OtherTypes extends Cursor {
		public byte getByte();
		public void setByte(byte value);
		
		public short getShort();
		public void setShort(short value);
		
		public float getFloat();
		public void setFloat(float value);

		public boolean getBool();
		public void setBool(boolean value);

		public char getChar();
		public void setChar(char value);
	}

	private static final Allocator<OtherTypes> allocator = Allocator.of(OtherTypes.class);
	private static final OtherTypes value = allocator.allocate(1);

	@Test
	public void fieldsGettableSettable() {
		value.setByte((byte) 23);
		assertEquals(23, value.getByte());
		
		value.setShort((short) 15);
		assertEquals(15, value.getShort());
		
		value.setFloat(0.5f);
		assertEquals(0.5f, value.getFloat(), 0.0f);
		
		value.setBool(true);
		assertTrue(value.getBool());
		
		value.setChar('c');
		assertEquals('c', value.getChar());
	}
	
	@AfterClass
	public static void free() {
		value.close();
	}

}
