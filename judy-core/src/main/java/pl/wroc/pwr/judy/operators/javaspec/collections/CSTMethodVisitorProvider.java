package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.MethodVisitor;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.MutationObserver;

public class CSTMethodVisitorProvider implements MethodVisitorProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MethodVisitor createVisitor(MethodVisitor methodVisitor, int access, String description,
									   MutationObserver observer) {
		CSTMethodVisitor cstMethodVisitor = new CSTMethodVisitor(methodVisitor, access, description);
		cstMethodVisitor.addObserver(observer);
		return cstMethodVisitor;
	}

}
