package pl.wroc.pwr.judy.operators;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import pl.wroc.pwr.judy.utils.EmptyVisitor;

/**
 * Abstract base class for all class mutaters.
 *
 * @author pmiwaszko
 */
public abstract class AbstractTreeClassMutater extends AbstractClassMutater {
	/**
	 * Constructs new {@link AbstractTreeClassMutater} object for mutating
	 * classes.
	 */
	public AbstractTreeClassMutater(final ClassVisitor cv, final int mutationPointIndex, final int mutantIndex) {
		super(cv, mutationPointIndex, mutantIndex, false);
		cn = new ClassNode(Opcodes.ASM5);
	}

	/**
	 * Constructs new {@link AbstractTreeClassMutater} object for counting
	 * mutants.
	 */
	public AbstractTreeClassMutater() {
		super(new EmptyVisitor(), -1, -1, true);
		cn = new ClassNode(Opcodes.ASM5);
	}

	/**
	 * Perform mutation.
	 */
	protected abstract void mutateClass(ClassNode cn);

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		className = name;
		cn.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return cn.visitAnnotation(desc, visible);
	}

	@Override
	public void visitAttribute(Attribute attr) {
		cn.visitAttribute(attr);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		return cn.visitField(access, name, desc, signature, value);
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		cn.visitInnerClass(name, outerName, innerName, access);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return cn.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		cn.visitOuterClass(owner, name, desc);
	}

	@Override
	public void visitSource(String source, String debug) {
		cn.visitSource(source, debug);
	}

	@Override
	public void visitEnd() {
		mutateClass(cn);
		cn.accept(cv);
	}

	protected ClassNode cn;
}
