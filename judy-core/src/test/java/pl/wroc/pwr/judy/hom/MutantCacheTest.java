package pl.wroc.pwr.judy.hom;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import pl.wroc.pwr.judy.common.IMutant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MutantCacheTest {
	private IMutantCache cache;
	private MutationInfo mutationInfo;

	@Before
	public void setUp() throws Exception {
		mutationInfo = Mockito.mock(MutationInfo.class);
		cache = new MutantCache();
	}

	private IMutant mockMutant() {
		return Mockito.mock(IMutant.class);
	}

	@Test
	public void shouldSaveMutantsInCacheAndRetrieveThem() throws Exception {
		IMutant mutant = mockMutant();
		cache.add(mutationInfo, mutant);
		IMutant result = cache.get(mutationInfo);
		assertEquals(mutant, result);
	}

	@Test
	public void shouldReturnNullIfNothingWasFoundInCache() throws Exception {
		IMutant result = cache.get(mutationInfo);
		assertNull(result);
	}

	@Test
	public void shouldOverrideCachedMutantsOnSecondAddition() throws Exception {
		IMutant before = mockMutant();
		IMutant after = mockMutant();

		cache.add(mutationInfo, before);
		assertEquals(before, cache.get(mutationInfo));

		cache.add(mutationInfo, after);
		assertEquals(after, cache.get(mutationInfo));
	}

}
