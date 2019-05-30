package pl.wroc.pwr.judy.hom;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.HashMap;
import java.util.Map;

public class MutantCache implements IMutantCache {
	private Map<MutationInfo, IMutant> cache = new HashMap<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(MutationInfo info, IMutant mutant) {
		cache.put(info, mutant);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMutant get(MutationInfo info) {
		return cache.get(info);
	}
}
