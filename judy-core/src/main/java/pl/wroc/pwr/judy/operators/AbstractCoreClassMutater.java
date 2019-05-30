package pl.wroc.pwr.judy.operators;

import org.objectweb.asm.*;
import pl.wroc.pwr.judy.utils.EmptyVisitor;

/**
 * Abstract base class for all class mutators.
 *
 * @author pmiwaszko
 */
public abstract class AbstractCoreClassMutater extends AbstractClassMutater {
	/**
	 * Constructs new {@link AbstractCoreClassMutater} object for mutating
	 * classes.
	 *
	 * @param cv                 class writer, ASM events consumer
	 * @param mutationPointIndex index of applicable mutation point
	 * @param mutantIndex        index of mutant, assigned during mutatns generation
	 */
	public AbstractCoreClassMutater(final ClassVisitor cv, final int mutationPointIndex, final int mutantIndex) {
		super(cv, mutationPointIndex, mutantIndex, false);
	}

	/**
	 * Constructs new {@link AbstractCoreClassMutater} object for counting
	 * mutants.
	 */
	public AbstractCoreClassMutater() {
		super(new EmptyVisitor(), -1, -1, true);
	}

	@Override
	public void visit(final int version, final int access, final String name, final String signature,
					  final String superName, final String[] interfaces) {
		className = name;
		cv.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public void visitSource(final String source, final String debug) {
		cv.visitSource(source, debug);
	}

	@Override
	public void visitOuterClass(final String owner, final String name, final String desc) {
		cv.visitOuterClass(owner, name, desc);
	}

	@Override
	public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
		return cv.visitAnnotation(desc, visible);
	}

	@Override
	public void visitAttribute(final Attribute attr) {
		cv.visitAttribute(attr);
	}

	@Override
	public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
		cv.visitInnerClass(name, outerName, innerName, access);
	}

	@Override
	public FieldVisitor visitField(final int access, final String name, final String desc, final String signature,
								   final Object value) {
		return cv.visitField(access, name, desc, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
									 final String[] exceptions) {
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitEnd() {
		cv.visitEnd();
	}
}
