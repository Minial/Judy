package pl.wroc.pwr.judy.hom.som;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SomGeneratorFactoryTest {

	@Test
	public void shouldGetDefaultGeneratorForEmptyParam() throws Exception {
		ISomGeneratorFactory factory = new SomGeneratorFactory("");
		checkDefaultGeneratorCreation(factory);
	}

	@Test
	public void shouldGetDefaultGeneratorForNullParam() throws Exception {
		ISomGeneratorFactory factory = new SomGeneratorFactory(null);
		checkDefaultGeneratorCreation(factory);
	}

	@Test
	public void shouldGetDefaultGeneratorForDummyParam() throws Exception {
		ISomGeneratorFactory factory = new SomGeneratorFactory("DUMMY");
		checkDefaultGeneratorCreation(factory);
	}

	@Test
	public void shouldGetLastToFirst() throws Exception {
		checkGeneratorCreation(SomStrategy.LAST_TO_FIRST, LastToFirstStrategy.class);
	}

	@Test
	public void shouldClosePair() throws Exception {
		checkGeneratorCreation(SomStrategy.CLOSE_PAIR, ClosePairStrategy.class);
	}

	@Test
	public void shouldGetRandomMix() throws Exception {
		checkGeneratorCreation(SomStrategy.RANDOM_MIX, RandomMixStrategy.class);
	}

	@Test
	public void shouldGetDifferentOperators() throws Exception {
		checkGeneratorCreation(SomStrategy.DIFFERENT_OPERATORS, DifferentOperatorsStrategy.class);
	}

	private void checkGeneratorCreation(SomStrategy strategy, Class<?> clazz) {
		ISomGeneratorFactory factory = new SomGeneratorFactory(strategy.getParam());
		SomFactory generator = factory.createGenerator();
		assertNotNull(generator);
		assertTrue(clazz.isInstance(generator));
	}

	private void checkDefaultGeneratorCreation(ISomGeneratorFactory factory) {
		SomFactory generator = factory.createGenerator();
		assertNotNull(generator);
		assertTrue(generator instanceof DifferentOperatorsStrategy);
	}
}
