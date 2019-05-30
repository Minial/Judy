package pl.wroc.pwr.judy.utils;

import org.objectweb.asm.ClassReader;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IEnvironment;

import java.util.Iterator;
import java.util.List;

/**
 * Utility class used for filtering classes that should not be mutated, e.g.
 * interfaces and applets.
 *
 * @author pmiwaszko
 */
public enum ClassKind {
	/**
	 * Class kind is unknown.
	 */
	UNKNOWN,

	/**
	 * Class is not covered by any test.
	 */
	NO_TESTS,

	NORMAL {
		@Override
		public boolean isMutable() {
			return true;
		}
	},
	INTERFACE, ABSTRACT, APPLET, GUI;

	/**
	 * Gets class kind
	 *
	 * @param className    class name
	 * @param bytecode     class bytecode
	 * @param superclasses list of class' superclasess
	 * @param hasTests     whether class has tests
	 * @return class kind
	 */
	public static ClassKind getKind(String className, byte[] bytecode, List<String> superclasses, boolean hasTests) {
		if (!hasTests) {
			return NO_TESTS;
		}
		if (bytecode != null) {
			ClassReader reader = new ClassReader(bytecode);
			ClassKindVisitor visitor = new ClassKindVisitor();
			reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
			if (visitor.isInterface) {
				return INTERFACE;
			}
			if (visitor.isAbstract) {
				return ABSTRACT;
			}
		}
		String name = className;
		Iterator<String> iter = superclasses.iterator();
		while (name != null) {
			if (name.contains("java.lang")) {
				break;
			}
			if (isApplet(name)) {
				return APPLET;
			}
			if (isGui(name)) {
				return GUI;
			}
			if (iter.hasNext()) {
				name = iter.next();
			} else {
				name = null;
			}
		}

		return NORMAL;
	}

	/**
	 * Checks if class is potentially mutable - whether it is not abstract nor
	 * interface.
	 *
	 * @param className class name
	 * @param bytecode  class bytecode
	 * @return true if class is not abstract nor interface
	 */
	public static boolean isPotentiallyMutable(String className, byte[] bytecode) {
		ClassReader reader = new ClassReader(bytecode);
		ClassKindVisitor visitor = new ClassKindVisitor();
		reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES);
		if (visitor.isInterface) {
			return false;
		}
		if (visitor.isAbstract) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String s;
		switch (this) {
			case NO_TESTS:
				s = "T";
				break;

			case APPLET:
				s = "P";
				break;

			default:
				s = super.toString().substring(0, 1);
		}
		return s;
	}

	private static boolean isApplet(String name) {
		return name.contains("java.applet");
	}

	private static boolean isGui(String name) {
		return name.contains("java.swing") || name.contains("java.awt");
	}

	private static class ClassKindVisitor extends EmptyVisitor {
		private boolean isAbstract;
		private boolean isInterface;

		@Override
		public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
			isInterface = Accesses.isInterface(access);
			isAbstract = Accesses.isAbstract(access);
		}
	}

	/**
	 * Tests whether given class kind is mutable or not
	 *
	 * @return false
	 */
	public boolean isMutable() {
		return false;
	}

	public static ClassKind getKind(ITargetClass targetClass, IBytecodeCache cache, IEnvironment env) {
		String className = targetClass.getName();
		return getKind(className, cache.get(className), env.getAllSuperclasses(className), !targetClass
				.getCoveringTestClasses().isEmpty());
	}
}
