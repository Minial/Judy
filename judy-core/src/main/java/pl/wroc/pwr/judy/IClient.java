package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.utils.Timer;

public interface IClient {
	IMutationResult compute(Timer timer) throws Exception;
}
