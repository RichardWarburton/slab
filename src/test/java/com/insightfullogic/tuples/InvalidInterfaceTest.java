package com.insightfullogic.tuples;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class InvalidInterfaceTest {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { NoGettersOrSetters.class }, { InvalidReturnGetter.class }, { ParameterGetter.class },
				{ InvalidReturnSetter.class }, { NoParameterSetter.class } });
	}

	private Class<?> representingKlass;

	public InvalidInterfaceTest(Class<?> representingKlass) {
		this.representingKlass = representingKlass;
	}

	@Test(expected = InvalidInterfaceException.class)
	public void interfaceIsInvalid() {
		BlockAllocator.of(representingKlass);
		System.err.println(representingKlass.getName());
	}

	// ---------------------------------------------------

	public interface NoGettersOrSetters {
		public void neitherGetterNorSetter();
	}

	public interface InvalidReturnGetter {
		public Object getFoo();
	}

	public interface ParameterGetter {
		public int getFoo(long bar);
	}

	public interface InvalidReturnSetter {
		public Object setFoo();
	}

	public interface NoParameterSetter {
		public void setFoo();
	}

}
