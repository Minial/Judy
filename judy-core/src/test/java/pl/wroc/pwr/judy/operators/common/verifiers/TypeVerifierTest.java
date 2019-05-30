package pl.wroc.pwr.judy.operators.common.verifiers;

import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class TypeVerifierTest {
	private TypeVerifier verifier;

	@Before
	public void setUp() {
		verifier = new TypeVerifier() {
			@Override
			protected List<String> getTypes() {
				return new ArrayList<>();
			}
		};
	}

	@Test
	public void basicTypesShouldNotBeSuccessfullyVerified() throws Exception {
		assertFalse(verifier.verifyType(Type.BOOLEAN_TYPE));
		assertFalse(verifier.verifyType(Type.BYTE_TYPE));
		assertFalse(verifier.verifyType(Type.CHAR_TYPE));
		assertFalse(verifier.verifyType(Type.DOUBLE_TYPE));
		assertFalse(verifier.verifyType(Type.FLOAT_TYPE));
		assertFalse(verifier.verifyType(Type.INT_TYPE));
		assertFalse(verifier.verifyType(Type.LONG_TYPE));
		assertFalse(verifier.verifyType(Type.SHORT_TYPE));
		assertFalse(verifier.verifyType(Type.VOID_TYPE));
	}

}
