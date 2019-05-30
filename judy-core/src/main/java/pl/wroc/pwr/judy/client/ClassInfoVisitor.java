package pl.wroc.pwr.judy.client;

import org.objectweb.asm.*;
import pl.wroc.pwr.judy.common.IClassInfo;
import pl.wroc.pwr.judy.common.IFieldInfo;
import pl.wroc.pwr.judy.common.IMethodInfo;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ASM-based runtime reflection substitution. Class visitor collecting detailed
 * information about Java classes.
 *
 * @author pmiwaszko
 * @see IClassInfo
 * @see ClassInfoCache
 */
class ClassInfoVisitor extends ClassVisitor {

	private int access;
	private String name;
	private String signature;
	private String superName;
	private final List<String> interfaces = new LinkedList<>();
	private final List<IFieldInfo> fields = new LinkedList<>();
	private final List<IMethodInfo> methods = new LinkedList<>();

	public ClassInfoVisitor() {
		super(Opcodes.ASM4);
	}

	public IClassInfo getClassInfo() {
		return new ClassInfo(access, name, signature, superName, interfaces, fields, methods);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.access = access;
		this.name = name;
		this.signature = signature;
		this.superName = superName;
		if (interfaces != null) {
			this.interfaces.addAll(Arrays.asList(interfaces));
		}
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	@Override
	public void visitAttribute(Attribute attr) {
	}

	@Override
	public FieldVisitor visitField(int access, String name, String description, String signature, Object value) {
		fields.add(new FieldInfo(access, name, description, signature, value));
		return null;
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		methods.add(new MethodInfo(access, name, desc, signature, exceptions == null ? new LinkedList<String>()
				: Arrays.asList(exceptions)));
		return null;
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
	}

	@Override
	public void visitSource(String source, String debug) {
	}

	@Override
	public void visitEnd() {
	}
}
