package pl.wroc.pwr.judy.operators.javaspec.collections;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import pl.wroc.pwr.judy.operators.AbstractCoreClassMutater;
import pl.wroc.pwr.judy.operators.common.MethodVisitorProvider;
import pl.wroc.pwr.judy.operators.common.MutationObserver;
import pl.wroc.pwr.judy.operators.common.verifiers.TypeVerifier;

/**
 * Base class for class visitor implementation visiting only public methods
 * returning one of a specified subclass or subinterface from
 * <code>java.util</code> package.
 *
 * @author TM
 */
public class CollectionClassMutator extends AbstractCoreClassMutater implements MutationObserver {
	private TypeVerifier verifier;
	private MethodVisitorProvider provider;

	/**
	 * Constructs mutator counting mutation points only
	 *
	 * @param verifier method description verifier
	 * @param provider method visitor provider
	 */
	public CollectionClassMutator(TypeVerifier verifier, MethodVisitorProvider provider) {
		super();
		this.verifier = verifier;
		this.provider = provider;
	}

	/**
	 * Constructs new class mutator object for mutating classes.
	 *
	 * @param cv                 class writer, ASM events consumer
	 * @param mutationPointIndex index of applicable mutation point
	 * @param mutantIndex        index of mutant, assigned during mutatns generation
	 * @param verifier           method description verifier
	 * @param provider           method visitor provider
	 */
	public CollectionClassMutator(ClassVisitor cv, int mutationPointIndex, int mutantIndex, TypeVerifier verifier,
								  MethodVisitorProvider provider) {
		super(cv, mutationPointIndex, mutantIndex);
		this.verifier = verifier;
		this.provider = provider;
	}

	/**
	 * {@inheritDoc}<br>
	 * Visits only public methods returning one of a subclass or subinterface of
	 * <code>java.util.Collection</code>
	 */
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		if (access == Opcodes.ACC_PUBLIC && verifier.verifyMethodDescription(desc)) {
			nextMutationPoint();
			if (shouldMutate()) {
				setMutatedMethodName(name);
				mv = provider.createVisitor(mv, access, desc, this);
			}
		}
		return mv;
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		return super.visitField(access, name, desc, signature, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMutation(int lineNumber) {
		setMutantLineNumber(lineNumber);
	}
}