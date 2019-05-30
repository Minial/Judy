package pl.wroc.pwr.judy.loader;

public interface Instrumenter {

	/**
	 * Instruments class bytecode
	 *
	 * @param mutatedBytecode bytecode to be changed
	 * @return instrumented bytecode
	 */
	byte[] instrument(byte[] mutatedBytecode);

}
