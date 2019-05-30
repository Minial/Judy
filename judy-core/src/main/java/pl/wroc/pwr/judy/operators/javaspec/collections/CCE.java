package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.ClassVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.verifiers.CollectionAPIVerifier;
import pl.wroc.pwr.judy.operators.common.verifiers.TypeVerifier;

/**
 * Mutation operator clears the collection using <code>clear()</code> method in
 * a line just before return from a method.<br/>
 * Applicable to: All methods returning subtype or subinterface of
 * <code>java.collection.Collection</code>.
 *
 * @author TM
 */
public class CCE extends AbstractMutationOperator {
	private static final MethodVisitorProvider PROVIDER = new CCEMethodVisitorProvider();
	private static final TypeVerifier VERIFIER = new CollectionAPIVerifier();

	private static final String DESCRIPTION = "Clears the collection";

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
