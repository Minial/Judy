package pl.wroc.pwr.judy.operators.guards;

import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.List;

public final class JumpOpcodeVerifier {
	private static final List<Integer> JUMP_OPCODES = Arrays.asList(Opcodes.IF_ACMPEQ, Opcodes.IF_ACMPNE,
			Opcodes.IF_ICMPEQ, Opcodes.IF_ICMPGE, Opcodes.IF_ICMPGT, Opcodes.IF_ICMPLE, Opcodes.IF_ICMPLT,
			Opcodes.IF_ICMPNE, Opcodes.IFEQ, Opcodes.IFGE, Opcodes.IFGT, Opcodes.IFLE, Opcodes.IFLT, Opcodes.IFNE,
			Opcodes.IFNONNULL, Opcodes.IFNULL, Opcodes.GOTO);

	private JumpOpcodeVerifier() {
	}

	/**
	 * Checks if opcode is one of the jump operation codes
	 *
	 * @param opcode operation code
	 * @return true if opcode is a jump operation code
	 */
	public static boolean verify(int opcode) {
		return JUMP_OPCODES.contains(opcode);
	}
}
