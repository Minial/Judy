package pl.wroc.pwr.judy.operators.common.verifiers;

import org.junit.Test;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ListInterfaceAPIVerifierTest {

	@Test
	public void shouldVerify() throws Exception {
		ListInterfaceAPIVerifier verifier = new ListInterfaceAPIVerifier();
		assertFalse(verifier.verifyType(Type.getType(ArrayList.class)));
		assertTrue(verifier.verifyType(Type.getType(List.class)));
	}
}
