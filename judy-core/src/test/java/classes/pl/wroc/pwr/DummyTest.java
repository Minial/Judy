package classes.pl.wroc.pwr;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DummyTest {
	@Test
	public void shouldReturnHelloWorld() throws Exception {
		Dummy cut = new Dummy();
		assertEquals("Hello world!", cut.helloWorld());
	}
}
