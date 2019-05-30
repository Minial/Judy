package pl.wroc.pwr.judy.loader;

public interface IMutationClassLoaderFactory extends ClassLoaderFactory {

	@Override
	MutationClassLoader createLoader();

}
