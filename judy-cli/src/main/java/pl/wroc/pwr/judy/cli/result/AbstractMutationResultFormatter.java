package pl.wroc.pwr.judy.cli.result;

import pl.wroc.pwr.judy.cli.progress.WorkProgressObserver;
import pl.wroc.pwr.judy.work.MutationResultFormatter;

import java.util.Observer;

public abstract class AbstractMutationResultFormatter implements MutationResultFormatter {

	public static final String SPACER = "  ";
	public static final String SECTION_DIVIDER = "  |  ";
	protected static final int DEFAULT_PADDING = 6;

	@Override
	public Observer getWorkObserver() {
		return new WorkProgressObserver();
	}

	protected String pad(final Object s, final int n) {
		return String.format("%1$-" + n + "s", s.toString());
	}

	protected String padRight(final Object s, final int n) {
		return String.format("%1$" + n + "s", s);
	}
}
