package pl.wroc.pwr.judy.hom.utils;

import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.Problem;
import org.moeaframework.core.spi.AlgorithmFactory;
import org.moeaframework.util.TypedProperties;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Extension of MOEA AlgorithmFactory allowing to return algorithms with
 * customized initialization of solution variables for given algorithm. In order
 * to use algorithm with customized initialization defined in
 * CustomBinaryVariableInitialization please append "_CustomInitialization" to
 * original algorithm name
 *
 * @author Maciej Szewczyszyn
 */
public class CustomInitializationAlgorithmFactory extends AlgorithmFactory {

	private static AlgorithmFactory instance = new CustomInitializationAlgorithmFactory();

	public static synchronized AlgorithmFactory getInstance() {
		return instance;
	}

	@Override
	public synchronized Algorithm getAlgorithm(String name, Properties properties, Problem problem) {
		Algorithm algorithm;
		if (name.endsWith("CustomInitialization")) {
			String originalName = name.substring(0, name.lastIndexOf("_"));
			algorithm = super.getAlgorithm(originalName, properties, problem);
			Class<?> algorithmClass = algorithm.getClass();
			// Check if chosen algorithm has field "initialization"
			try {
				TypedProperties typedProperties = new TypedProperties(properties);
				Field initializationField = getInitializationField(algorithmClass);
				initializationField.setAccessible(true);
				Initialization initialization = new CustomBinaryVariableInitialization(problem,
						(int) typedProperties.getDouble("populationSize", 100), typedProperties.getInt(
						"maxMutationOrder", 15));
				initializationField.set(algorithm, initialization);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// No field "initialization" means no custom initialization
				return algorithm;
			}
		} else {
			algorithm = super.getAlgorithm(name, properties, problem);
		}
		return algorithm;
	}

	private Field getInitializationField(Class<?> algorithmClass) throws NoSuchFieldException, SecurityException {
		try {
			Field initializationField = algorithmClass.getDeclaredField("initialization");
			return initializationField;
		} catch (NoSuchFieldException e) {
			Class<?> parentClass = algorithmClass.getSuperclass();
			if (parentClass.equals(Object.class)) {
				throw e;
			} else {
				return getInitializationField(parentClass);
			}
		}
	}
}
