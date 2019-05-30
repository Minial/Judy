package pl.wroc.pwr.judy.hom.som;

public interface ISomGeneratorFactory {
	/**
	 * Creates SOM generator
	 *
	 * @return generator
	 */
	SomFactory createGenerator();

	String getName();
}
