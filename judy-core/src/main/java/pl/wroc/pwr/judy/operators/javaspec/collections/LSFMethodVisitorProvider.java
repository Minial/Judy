package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.MutationObserver;

public class LSFMethodVisitorProvider implements MethodVisitorProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor createVisitor(MethodVisitor mv, int access, String desc, MutationObserver observer) {
		LSFMethodVisitor lsfMethodVisitor = new LSFMethodVisitor(mv, access, desc);
		lsfMethodVisitor.addObserver(observer);
		return lsfMethodVisitor;
	}

}
