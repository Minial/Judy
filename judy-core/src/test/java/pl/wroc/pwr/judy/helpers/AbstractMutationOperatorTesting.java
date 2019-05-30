package pl.wroc.pwr.judy.helpers;

import org.junit.Before;
import pl.wroc.pwr.judy.client.EnvironmentFactory;
import pl.wroc.pwr.judy.common.IBytecodeCache;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationPoint;
import pl.wroc.pwr.judy.loader.MutationClassLoader;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.junit.Assert.assertEquals;
import static pl.wroc.pwr.judy.helpers.TestHelper.getBytecodeCache;

/**
 * Simple framework for mutation operators testing.
 *
 * @author pmiwaszko
 */
public abstract class AbstractMutationOperatorTesting {
	protected static final int NA = -1;

	private Class<?> original;

	private byte[] bytecode;

	private AbstractMutationOperator operator;

	private IBytecodeCache cache;

	private List<IMutationPoint> mutationPoints;

	private List<IMutantBytecode> mutants;

	// method result
	public void assertMethodResultEquals(Object expected) throws Exception {
		assertMethodResultEquals(expected, "test");
	}

	// mutated method result
	public void assertMethodResultEquals(Object expected, String methodName, Object... args) throws Exception {
		assertEquals(expected, getMethodResult(methodName, args));
	}

	/**
	 * Assert number of mutants.
	 */
	public void assertMutantsCountEquals(int expected) {
		assertEquals(expected, mutants.size());
	}

	public void assertMutatedMethodResultEquals(Object expected, int mutantIndex) throws Exception {
		assertMutatedMethodResultEquals(expected, mutantIndex, "test");
	}

	public void assertMutatedMethodResultEquals(Object expected, int mutantIndex, String methodName, Object... args)
			throws Exception {
		assertEquals(expected, getMutatedMethodResult(mutantIndex, methodName, args));
	}

	/**
	 * Assert number of mutation points.
	 */
	public void assertMutationPointsCountEquals(int expected) {
		assertEquals(expected, mutationPoints.size());
	}

	/**
	 * Create synthetic environment.
	 */
	private IEnvironment createEnvironment() throws IOException {
		cache = getBytecodeCache();
		String className = original.getName().replace('.', '/');
		bytecode = cache.get(className);

		Map<String, String> inheritance = newLinkedHashMap();
		// TODO inheritance.put(className, superClassName);
		initClassHierarchy(inheritance);
		return new EnvironmentFactory(inheritance).create(cache);
	}

	/**
	 * Locate {@link Mutate} annotation and instantiate operator defined in it.
	 */
	private Mutate findMutateAnnotation() {
		Mutate annotation = findAnnotationOnInnerClasses();

		if (annotation == null) {
			annotation = findAnnotationOnClass();
		}

		if (original == null || annotation == null) {
			throw new MutationOperatorTestException("No target class found - @Mutate was not used!");
		}

		return annotation;
	}

	private Mutate findAnnotationOnInnerClasses() {
		Mutate annotation = null;

		Mutate consideredAnnotation;
		int classes = 0;
		for (Class<?> clazz : getClass().getClasses()) {
			consideredAnnotation = clazz.getAnnotation(Mutate.class);
			if (consideredAnnotation != null) {
				original = clazz;
				annotation = consideredAnnotation;
				classes++;
			}
		}

		if (classes > 1) {
			throw new MutationOperatorTestException("Maximum one public inner class may be annotated with @Mutate!");
		}
		return annotation;
	}

	private Mutate findAnnotationOnClass() {
		Mutate annotation = getClass().getAnnotation(Mutate.class);

		if (annotation != null) {
			original = annotation.targetClass();
		}

		return annotation;
	}

	public Object getMethodResult() throws Exception {
		return getMethodResult("test");
	}

	// method result

	/**
	 * Get result of unmodified method invocation.
	 */
	public Object getMethodResult(String methodName, Object... args) throws Exception {
		// use original class loaded with app class loader

		Object obj = instantiate(original, this);
		return original.getMethod(methodName).invoke(obj, (Object[]) null);
	}

	private Object instantiate(Class<?> clazz, Object testInstance) throws Exception {
		Object obj = null;
		// if (mutatingInner) {
		if (clazz.getDeclaringClass() != null) {
			Constructor<?> c = clazz.getDeclaredConstructor(testInstance.getClass());
			obj = c.newInstance(testInstance);
		} else {
			obj = clazz.newInstance();
		}
		return obj;
	}

	/**
	 * Get result of mutated method invocation.
	 */
	public Object getMutatedMethodResult(int mutantIndex, String methodName, Object... args) throws Exception {
		byte[] code = mutants.get(mutantIndex).getBytecode();

		ClassLoader loader = new MutationClassLoader(cache, original.getName(), code);

		Class<?> mutant = loader.loadClass(original.getName());
		Object test = loader.loadClass(getClass().getName()).newInstance();

		Object obj = instantiate(mutant, test);

		// run method
		return mutant.getMethod(methodName).invoke(obj, args);
	}

	public Object getMutatedMethodResult(int mutantIndex) throws Exception {
		return getMutatedMethodResult(mutantIndex, "test");
	}

	@Before
	public void init() throws Exception {
		Mutate annotation = findMutateAnnotation();
		IEnvironment env = createEnvironment();
		instantiateOperator(annotation, env);
		mutationPoints = operator.getMutationPoints(bytecode);
		mutate();
	}

	/**
	 * Initialize class hierarchy. This method is intended to be overridden in
	 * tests to add other classes to inheritance map.
	 *
	 * @param inheritance inheritance map (class name -&gt; super class name)
	 */
	protected void initClassHierarchy(Map<String, String> inheritance) {
		// to be overridden in subclasses
	}

	/**
	 * Instantiate
	 *
	 * @param annotation
	 */
	private void instantiateOperator(Mutate annotation, IEnvironment env) {
		try {
			operator = annotation.operator().newInstance();
			operator.setEnvironment(env);
		} catch (InstantiationException e) {
			throw new MutationOperatorTestException("Inner class annotated with @Mutate cannot be instantiated!", e);
		} catch (IllegalAccessException e) {
			throw new MutationOperatorTestException(
					"Default constructor of inner class annotated with @Mutate is not accessible!", e);
		} catch (IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create mutants.
	 */
	private void mutate() {
		mutants = newArrayList();
		for (IMutationPoint mutationPoint : mutationPoints) {
			mutants.addAll(operator.mutate(bytecode, mutationPoint));
		}
	}
}
