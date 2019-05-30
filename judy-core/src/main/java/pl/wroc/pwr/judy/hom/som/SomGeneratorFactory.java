package pl.wroc.pwr.judy.hom.som;

public class SomGeneratorFactory implements ISomGeneratorFactory {

	private String name;

	/**
	 * Creates factory using given generator name
	 *
	 * @param name generator name
	 */
	public SomGeneratorFactory(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public SomFactory createGenerator() {
		SomFactory generator = SomStrategy.DIFFERENT_OPERATORS.getInstance();

		SomStrategy strategy = SomStrategy.getByParam(name);
		if (strategy != null) {
			generator = strategy.getInstance();
		}
		return generator;
	}

}
