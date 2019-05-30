package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.MutationObserver;

public class CCEMethodVisitorProvider implements MethodVisitorProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor createVisitor(MethodVisitor mv, int access, String desc, MutationObserver observer) {
		CCEMethodVisitor cceMethodVisitor = new CCEMethodVisitor(mv, access, desc);
		cceMethodVisitor.addObserver(observer);
		return cceMethodVisitor;
	}

}
