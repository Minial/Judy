package pl.wroc.pwr.judy.hom;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HomStrategyTest {

	@Test
	public void shouldLookupStrategyByParameter() throws Exception {
		assertEquals(HomStrategy.ON_THE_FLY, HomStrategy.lookup(HomStrategy.ON_THE_FLY.getParameter()));
		assertEquals(HomStrategy.UP_FRONT, HomStrategy.lookup(HomStrategy.UP_FRONT.getParameter()));
	}

	@Test
	public void shouldReturnNullForUnknownParameter() throws Exception {
		assertNull(HomStrategy.lookup("dummy"));
	}

	@Test
	public void shouldReturnStrategyParameter() throws Exception {
		assertEquals("OnTheFly", HomStrategy.ON_THE_FLY.getParameter());
		assertEquals("UpFront", HomStrategy.UP_FRONT.getParameter());
	}
}
