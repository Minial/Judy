package pl.wroc.pwr.judy.operators.common;

import org.objectweb.asm.MethodVisitor;

/**
 * Interface for mutator specific Method Visitor providers
 */
public interface MethodVisitorProvider {

	/**
	 * Creates method visitor for mutable methods
	 *
	 * @param mv       current method visitor to 'overload'
	 * @param access   current method access opcode
	 * @param desc     current method description
	 * @param observer mutation event observer, for setting mutation line number
	 * @return custom method visitor
	 */
	MethodVisitor createVisitor(MethodVisitor mv, int access, String desc, MutationObserver observer);
}
