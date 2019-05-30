package pl.wroc.pwr.judy.loader;

import pl.wroc.pwr.judy.common.IBytecodeCache;

public class MutationClassLoaderFactory implements IMutationClassLoaderFactory {

	private String mutantClassName;
	private byte[] mutantBytecode;
	private IBytecodeCache cache;
	private Instrumenter instrumenter;

	/**
	 * @param mutantClassName mutant class name
	 * @param mutantBytecode  mutant bytecode
	 * @param cache           test and source classes cache
	 */
	public MutationClassLoaderFactory(String mutantClassName, byte[] mutantBytecode, IBytecodeCache cache, long infiniteLoopGuardTimeout) {
		this.mutantClassName = mutantClassName;
		this.mutantBytecode = mutantBytecode;
		this.cache = cache;
		instrumenter = new GuardInstrumenter(infiniteLoopGuardTimeout);
	}

	@Override
	public MutationClassLoader createLoader() {
		MutationClassLoader classLoader = new MutationClassLoader(cache, mutantClassName, mutantBytecode);
		classLoader.setInstrumenter(instrumenter);
		return classLoader;
	}
}
