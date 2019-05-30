package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.MutationObserver;

public class DULMethodVisitorProvider implements MethodVisitorProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor createVisitor(MethodVisitor mv, int access, String desc, MutationObserver observer) {
		DULMethodVisitor dulMethodVisitor = new DULMethodVisitor(mv, access, desc);
		dulMethodVisitor.addObserver(observer);
		return dulMethodVisitor;
	}
}
