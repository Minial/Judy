package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.IMutationWorkFactory;

public interface IMutationClientFactory {
	/**
	 * Creates mutation client
	 *
	 * @return mutation client
	 */
	IMutationClient createClient();

	/**
	 * Sets mutation work factory implementation
	 *
	 * @param workFactory mutation work factory
	 */
	void setWorkFactory(IMutationWorkFactory workFactory);
}
