package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.ClassVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.common.verifiers.ListAPIVerifier;

/**
 * Mutation operator that reverses the list using <code>reverse()</code> method
 * in a line just before return from a method.<br/>
 * Applicable to: All methods returning subtype or subinterface of
 * <code>java.collection.List</code>.
 *
 * @author TM
 */
public class REV extends AbstractMutationOperator {

	private static final REVMethodVisitorProvider PROVIDER = new REVMethodVisitorProvider();
	private static final ListAPIVerifier VERIFIER = new ListAPIVerifier();

	private static final String DESCRIPTION = "Reverses the list";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new CollectionClassMutator(VERIFIER, PROVIDER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new CollectionClassMutator(classWriter, mutationPointIndex, mutantIndex, VERIFIER, PROVIDER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
}
