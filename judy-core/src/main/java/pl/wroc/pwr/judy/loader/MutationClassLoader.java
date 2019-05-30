package pl.wroc.pwr.judy.loader;

import pl.wroc.pwr.judy.common.IBytecodeCache;

import java.net.URLClassLoader;

/**
 * Custom class loader mutating specified class.
 *
 * @author pmiwaszko
 */
public class MutationClassLoader extends URLClassLoader {
	private final IBytecodeCache cache;
	private final String mutantClassName;
	private final byte[] mutantBytecode;
	private Instrumenter instrumenter;

	/**
	 * <code>MutationClassLoader</code> constructor.
	 */
	/**
	 * @param cache           test and source classes cache
	 * @param mutantClassName mutant class name
	 * @param mutantBytecode  mutant bytecode
	 */
	public MutationClassLoader(IBytecodeCache cache, String mutantClassName, byte[] mutantBytecode) {
		super(cache.getURLs());
		this.cache = cache;
		this.mutantClassName = mutantClassName;
		this.mutantBytecode = mutantBytecode;
	}

	@Override
	public Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
		Class<?> loadedClass = findLoadedClass(className);
		if (loadedClass == null) {
			if (className.equals(mutantClassName)) {
				byte[] bytecode = mutantBytecode;
				if (instrumenter != null) {
					bytecode = instrumenter.instrument(mutantBytecode);
				}
				// mutate
				loadedClass = defineClass(className, bytecode, 0, bytecode.length);
			} else {
				byte[] bytecode = cache.get(className);

				if (bytecode != null) {
					// use bytecode from m_cache
					loadedClass = defineClass(className, bytecode, 0, bytecode.length);
				} else {
					// load with parent class loader
					loadedClass = super.loadClass(className, false);
				}
			}
		}
		if (resolve) {
			resolveClass(loadedClass);
		}
		return loadedClass;
	}

	public void setInstrumenter(Instrumenter instrumenter) {
		this.instrumenter = instrumenter;
	}
}
