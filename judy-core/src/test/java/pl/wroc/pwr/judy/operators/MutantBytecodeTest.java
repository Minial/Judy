package pl.wroc.pwr.judy.operators;

import org.junit.Test;
import pl.wroc.pwr.judy.common.IMutantBytecode;

import static org.junit.Assert.assertEquals;

public class MutantBytecodeTest {
	@Test
	public void shouldCreateNewMutantWithInstrumentedBytecode() throws Exception {
		byte[] bytecode = new byte[]{1, 3, 3, 7};
		int lineNumber = 7331;
		String description = "description";
		String methodName = "method name";
		String methodDescriptor = "method descriptor";
		IMutantBytecode base = new MutantBytecode(bytecode, lineNumber, description, methodName, methodDescriptor);

		byte[] newBytecode = new byte[]{7, 3, 3, 1};
		IMutantBytecode instrumented = new MutantBytecode(base, newBytecode);

		assertEquals(newBytecode, instrumented.getBytecode());
		assertEquals(description, instrumented.getDescription());
		assertEquals(lineNumber, instrumented.getLineNumber());
		assertEquals(methodDescriptor, instrumented.getMethodDescriptor());
		assertEquals(methodName, instrumented.getMethodName());
	}

}
