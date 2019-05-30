package pl.wroc.pwr.judy.operators;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Abstract base class for all method mutaters.
 *
 * @author pmiwaszko
 */
public abstract class AbstractMethodMutater extends MethodVisitor implements IMethodMutater {
	/**
	 * <code>AbstractMethodMutater</code> constructor.
	 */
	public AbstractMethodMutater(final MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		lastLineNumber = 0; // TODO refactor line numbers!
	}

	@Override
	public int getLastLineNumber() {
		return lastLineNumber;
	}

	/**
	 * Last seen line number.
	 */
	protected int lastLineNumber;
}
