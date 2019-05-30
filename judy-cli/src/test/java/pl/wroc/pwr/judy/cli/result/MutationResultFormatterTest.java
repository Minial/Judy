package pl.wroc.pwr.judy.cli.result;

import org.junit.Test;
import pl.wroc.pwr.judy.work.MutationResultFormatter;

import static org.junit.Assert.assertTrue;

public abstract class MutationResultFormatterTest {

	protected MutationResultFormatter formatter;

	protected String ignoreWhitespace(String expected) {
		return expected.replaceAll("\\s", "");
	}

	@Test
	public void shouldReturnWorkObserver() {
		assertTrue(formatter.getWorkObserver() != null);
	}

}
