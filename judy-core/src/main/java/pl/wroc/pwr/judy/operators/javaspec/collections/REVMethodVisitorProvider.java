package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.MutationObserver;

public class REVMethodVisitorProvider implements MethodVisitorProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor createVisitor(MethodVisitor mv, int access, String desc, MutationObserver observer) {
		REVMethodVisitor revMethodVisitor = new REVMethodVisitor(mv, access, desc);
		revMethodVisitor.addObserver(observer);
		return revMethodVisitor;
	}

}
