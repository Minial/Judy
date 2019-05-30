package pl.wroc.pwr.judy.loader;

public interface ClassLoaderFactory {
	/**
	 * Creates basic class loader
	 *
	 * @return class loader
	 */
	ClassLoader createLoader();
}
