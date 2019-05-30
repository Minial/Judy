package resources;

import pl.wroc.pwr.judy.common.IBytecodeCache;

import java.net.URL;

public class ClassInfoCacheResource implements IBytecodeCache {
	@Override
	public byte[] get(String className) {
		return null;
	}

	@Override
	public URL[] getURLs() {
		return null;
	}

	int a;
	public IBytecodeCache b;
	@SuppressWarnings("unused")
	private String c;
}
