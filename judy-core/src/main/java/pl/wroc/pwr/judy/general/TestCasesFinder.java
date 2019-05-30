package pl.wroc.pwr.judy.general;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Finds JUnit test methods, which are: - public void - without arguments -
 * annotated with @Test or with a name starting with 'test'
 *
 * @author TM
 */
public final class TestCasesFinder {

	private TestCasesFinder() {

	}

	/**
	 * Looks for runnable test methods in given JUnit test class.
	 *
	 * @param clazz test class
	 * @return list of runnable test method names
	 */
	public static Set<String> findTestMethods(Class<?> clazz) {
		Set<String> methods = new HashSet<>();
		for (Method m : clazz.getMethods()) {
			if (hasValidSignature(m) && isTest(m)) {
				methods.add(m.getName());
			}
		}
		return methods;
	}

	private static boolean isTest(Method m) {
		return isJUnit3Test(m) || isJUnit4Test(m);
	}

	private static boolean isJUnit4Test(Method m) {
		return m.getAnnotation(Test.class) != null;
	}

	private static boolean hasValidSignature(Method m) {
		return isPublic(m) && isVoid(m) && hasNoArguments(m);
	}

	private static boolean isJUnit3Test(Method m) {
		return m.getName().startsWith("test");
	}

	private static boolean isVoid(Method m) {
		return m.getReturnType().equals(Void.TYPE);
	}

	private static boolean hasNoArguments(Method m) {
		return m.getParameterTypes().length == 0;
	}

	private static boolean isPublic(Method m) {
		return Modifier.isPublic(m.getModifiers());
	}

}
