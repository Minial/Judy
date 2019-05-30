package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.MutationObserver;

public class ORVMethodVisitorProvider implements MethodVisitorProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor createVisitor(MethodVisitor mv, int access, String desc, MutationObserver observer) {
		ORVMethodVisitor orvMethodVisitor = new ORVMethodVisitor(mv, access, desc);
		orvMethodVisitor.addObserver(observer);
		return orvMethodVisitor;
	}

}
