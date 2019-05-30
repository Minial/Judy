package pl.wroc.pwr.judy.cli.progress;

import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.judy.IClassMutationResult;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import static org.apache.logging.log4j.LogManager.getLogger;
//import static org.testng.log4testng.Logger.getLogger;

/**
 * Class observes work made by Judy. It logs information about work progress on
 * every update.
 *
 * @author MH
 */
public class WorkProgressObserver implements Observer {
	private static final Logger LOGGER = getLogger(WorkProgressObserver.class);

	private float progress = 0.0f;

	@Override
	public void update(Observable o, Object arg) {
		IClassMutationResult result = (IClassMutationResult) arg;
		LOGGER.info(createMessage(result));
	}

	/**
	 * Prepares message to be logged.
	 *
	 * @param result result of work
	 * @return message to be logged.
	 */
	public String createMessage(IClassMutationResult result) {
		progress += result.getTargetClass().getEffort();
		return String.format(Locale.US, "[%5.1f%%]" + result.getSummary(), progress);
	}

}
