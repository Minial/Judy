package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.ClassVisitor;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.verifiers.SortedCollectionAPIVerifier;
import pl.wroc.pwr.judy.operators.common.verifiers.TypeVerifier;

/**
 * Mutation operator reverses the order of collections, on interfaces which
 * support ordering of elements sort objects with specified comparator<br/>
 * Applicable to: All methods returning subtype or subinterface of
 * <code>java.collection.OrderedSet</code> or
 * <code>java.util.collection.OrderedMap</code>.
 *
 * @author TM
 */
public class ORV extends AbstractMutationOperator {
	private static final MethodVisitorProvider PROVIDER = new ORVMethodVisitorProvider();
	private static final TypeVerifier VERIFIER = new SortedCollectionAPIVerifier();

	private static final String DESCRIPTION = "Reverses the order of collections with ordering based on comparator";

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
