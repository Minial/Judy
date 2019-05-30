package pl.wroc.pwr.judy.operators.guards;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.LocalVariablesSorter;

/**
 * Adds sleep() method call to every method in order to provide gentle exit
 * point for test threads interruption.
 *
 * @author TM
 */
public class SleepMethodMutator extends LocalVariablesSorter {

	private static final String SLEEP_METHOD_SIGNATURE = "(J)V";
	private static final String SLEEP_METHOD = "sleep";
	private static final String JAVA_LANG_THREAD = "java/lang/Thread";

	/**
	 * Creates sleep method mutator
	 *
	 * @param mv     method visitor
	 * @param access method's access flag
	 * @param desc   method's description
	 */
	public SleepMethodMutator(MethodVisitor mv, int access, String desc) {
		super(Opcodes.ASM5, access, desc, mv);
	}

	@Override
	public void visitCode() {
		super.visitCode();
		addCallToSleepMethod();
	}

	private void addCallToSleepMethod() {
		mv.visitInsn(Opcodes.LCONST_0);
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, JAVA_LANG_THREAD, SLEEP_METHOD, SLEEP_METHOD_SIGNATURE, false);
	}
}