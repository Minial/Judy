package pl.wroc.pwr.judy.utils;

/**
 * Utility class for convenient access modifiers handling.
 *
 * @author pmiwaszko
 * @author mnegacz
 */
public final class Accesses {
	public static final int ACC_PUBLIC = 0x0001;
	public static final int ACC_PRIVATE = 0x0002;
	public static final int ACC_PROTECTED = 0x0004;
	public static final int ACC_STATIC = 0x0008;
	public static final int ACC_FINAL = 0x0010;
	public static final int ACC_SUPER = 0x0020;
	public static final int ACC_SYNCHRONIZED = 0x0020;
	public static final int ACC_VOLATILE = 0x0040;
	public static final int ACC_BRIDGE = 0x0040;
	public static final int ACC_VARARGS = 0x0080;
	public static final int ACC_TRANSIENT = 0x0080;
	public static final int ACC_NATIVE = 0x0100;
	public static final int ACC_INTERFACE = 0x0200;
	public static final int ACC_ABSTRACT = 0x0400;
	public static final int ACC_STRICT = 0x0800;
	public static final int ACC_SYNTHETIC = 0x1000;
	public static final int ACC_ANNOTATION = 0x2000;
	public static final int ACC_ENUM = 0x4000;

	private Accesses() {
		// utility class
	}

	/**
	 * Return access with modifier added.
	 */
	public static int add(int access, int mod) {
		return access | mod;
	}

	/**
	 * Remove access with modifier removed.
	 */
	public static int remove(int access, int mod) {
		return access ^ mod;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the <tt>public</tt>
	 * modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>public</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isPublic(int mod) {
		return (mod & ACC_PUBLIC) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the
	 * <tt>private</tt> modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>private</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isPrivate(int mod) {
		return (mod & ACC_PRIVATE) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the
	 * <tt>protected</tt> modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>protected</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isProtected(int mod) {
		return (mod & ACC_PROTECTED) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument does not include the
	 * <tt>private</tt>, <tt>protected</tt> or <tt>public</tt> modifier,
	 * <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> does not include the
	 * <tt>private</tt>, <tt>protected</tt> or <tt>public</tt> modifier;
	 * <tt>false</tt> otherwise.
	 */
	public static boolean isDefault(int mod) {
		return !isPrivate(mod) && !isProtected(mod) && !isPublic(mod);
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the <tt>static</tt>
	 * modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>static</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isStatic(int mod) {
		return (mod & ACC_STATIC) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the <tt>final</tt>
	 * modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>final</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isFinal(int mod) {
		return (mod & ACC_FINAL) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the
	 * <tt>synchronized</tt> modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the
	 * <tt>synchronized</tt> modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isSynchronized(int mod) {
		return (mod & ACC_SYNCHRONIZED) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the
	 * <tt>volatile</tt> modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>volatile</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isVolatile(int mod) {
		return (mod & ACC_VOLATILE) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the
	 * <tt>transient</tt> modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>transient</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isTransient(int mod) {
		return (mod & ACC_TRANSIENT) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the <tt>native</tt>
	 * modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>native</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isNative(int mod) {
		return (mod & ACC_NATIVE) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the
	 * <tt>interface</tt> modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>interface</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isInterface(int mod) {
		return (mod & ACC_INTERFACE) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the
	 * <tt>abstract</tt> modifier, <tt>false</tt> otherwise.
	 *
	 * @param mod a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the <tt>abstract</tt>
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isAbstract(int mod) {
		return (mod & ACC_ABSTRACT) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument includes the given modifier,
	 * <tt>false</tt> otherwise.
	 *
	 * @param access the given modifier
	 * @param mod    a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> includes the given modifier;
	 * <tt>false</tt> otherwise.
	 */
	public static boolean is(int access, int mod) {
		return (mod & access) != 0;
	}

	/**
	 * Return <tt>true</tt> if the integer argument doesn't include the given
	 * modifier, <tt>false</tt> otherwise.
	 *
	 * @param access the given modifier
	 * @param mod    a set of modifiers
	 * @return <tt>true</tt> if <code>mod</code> doesn't include the given
	 * modifier; <tt>false</tt> otherwise.
	 */
	public static boolean isNot(int access, int mod) {
		return (mod & access) == 0;
	}

	/**
	 * Method copied from {@link java.lang.reflect.Modifier} class.
	 *
	 * @param mod a set of modifiers
	 * @see java.lang.reflect.Modifier#toString(int)
	 */
	public static String toString(int mod) {
		StringBuilder sb = new StringBuilder();

		if ((mod & ACC_PUBLIC) != 0) {
			sb.append("public ");
		} else if ((mod & ACC_PROTECTED) != 0) {
			sb.append("protected ");
		} else if ((mod & ACC_PRIVATE) != 0) {
			sb.append("private ");
		} else {
			sb.append("default ");
		}

		/* Canonical order */
		if ((mod & ACC_ABSTRACT) != 0) {
			sb.append("abstract ");
		}
		if ((mod & ACC_STATIC) != 0) {
			sb.append("static ");
		}
		if ((mod & ACC_FINAL) != 0) {
			sb.append("final ");
		}
		if ((mod & ACC_TRANSIENT) != 0) {
			sb.append("transient ");
		}
		if ((mod & ACC_VOLATILE) != 0) {
			sb.append("volatile ");
		}
		if ((mod & ACC_SYNCHRONIZED) != 0) {
			sb.append("synchronized ");
		}
		if ((mod & ACC_NATIVE) != 0) {
			sb.append("native ");
		}
		if ((mod & ACC_STRICT) != 0) {
			sb.append("strictfp ");
		}
		if ((mod & ACC_INTERFACE) != 0) {
			sb.append("interface ");
		}

		if (sb.length() > 0) {
			return sb.toString().substring(0, sb.length() - 1);
		}
		return "";
	}
}
