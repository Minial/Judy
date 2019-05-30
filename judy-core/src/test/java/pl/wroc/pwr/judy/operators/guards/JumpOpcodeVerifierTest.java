package pl.wroc.pwr.judy.operators.guards;

import org.junit.Test;
import org.objectweb.asm.Opcodes;

import static org.junit.Assert.assertTrue;

public class JumpOpcodeVerifierTest {

	@Test
	public void shouldPositivelyVerifyJumpOpcodes() throws Exception {
		for (int i = 153; i <= 166; i++) {
			assertTrue(JumpOpcodeVerifier.verify(i));
		}
	}

	@Test
	public void shouldPositivelyVerifyNullNonNullOpcodes() throws Exception {
		JumpOpcodeVerifier.verify(Opcodes.IFNULL);
		JumpOpcodeVerifier.verify(Opcodes.IFNONNULL);
	}

	@Test
	public void shouldPositivelyVerifyGotoOpcode() throws Exception {
		JumpOpcodeVerifier.verify(Opcodes.GOTO);
	}
}
