package pl.wroc.pwr.judy.operators.guards;

import org.objectweb.asm.ClassVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;

public class InterruptionGuard extends AbstractMutationOperator {

	private static final String DESCRIPTION = "Intorduces sleep() method call in the beginning of every method in order to provide gentle exit point for test threads interruption";

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new InterruptionGuardClassMutator();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor cv, int mutationPointIndex, int mutantIndex) {
		return new InterruptionGuardClassMutator(cv, mutationPointIndex, mutantIndex);
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

}
