package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.ClassVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.verifiers.LinkedHashSetDescriptionVerifier;
import pl.wroc.pwr.judy.operators.common.verifiers.TypeVerifier;

/**
 * Mutation operator which changes LinkedHashSet into HashSet to forget elements
 * order. After that, LinkedHashSet is cleared and elements from HashSet are
 * copied back to LinkedHashSet. Applicable to methods returning LinkedHashSet.
 *
 * @author MH
 */
public class CST extends AbstractMutationOperator {
	private static final MethodVisitorProvider PROVIDER = new CSTMethodVisitorProvider();
	private static final TypeVerifier VERIFIER = new LinkedHashSetDescriptionVerifier();
	private static final String DESCRIPTION = "Changes LinkedHashSet to HashSet to shuffle order of elements in LinkedHashSet";

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
