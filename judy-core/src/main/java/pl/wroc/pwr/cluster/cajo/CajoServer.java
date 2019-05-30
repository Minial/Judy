package pl.wroc.pwr.cluster.cajo;

import gnu.cajo.Cajo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.wroc.pwr.cluster.IQueue;
import pl.wroc.pwr.cluster.Queue;

import java.lang.reflect.UndeclaredThrowableException;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;

/**
 * Stand-alone server for Cajo-based cluster.
 *
 * @author pmiwaszko
 */
public final class CajoServer {
	private static final Logger LOGGER = LogManager.getLogger(CajoServer.class);

	private CajoServer() {
	}

	/**
	 * Application entry point.
	 */
	public static void main(String[] args) {
		try {
			LOGGER.info("Judy Server, " + new GregorianCalendar().getTime());
			Cajo cajo = new Cajo();
			IQueue queue = new Queue();
			cajo.export(queue);
			LOGGER.info("Service started...");
		} catch (RemoteException e) {
			LOGGER.error("Connection error: " + e.getMessage() + "!");
			System.exit(1);
		} catch (UndeclaredThrowableException e) {
			LOGGER.error("Connection error: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage())
					+ "!");
			System.exit(1);
		} catch (Exception e) {
			LOGGER.error("Unexpected server error: " + e.getClass().getSimpleName() + "!");
			e.printStackTrace();
			System.exit(1);
		}
	}
}
