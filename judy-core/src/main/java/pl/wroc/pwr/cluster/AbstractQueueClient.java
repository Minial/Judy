package pl.wroc.pwr.cluster;

import pl.wroc.pwr.judy.IClassMutationResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * Abstract base class for {@link IQueueClient} implementations.
 *
 * @author pmiwaszko
 */
public abstract class AbstractQueueClient extends Observable implements IQueueClient {
	private final long id;
	private final IQueue queue;
	private final List<IResult> results;
	private int count;

	/**
	 * <code>AbstractQueueClient</code> constructor.
	 */
	public AbstractQueueClient(long id, IQueue queue) {
		this.id = id;
		this.queue = queue;
		results = new LinkedList<>();
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public final synchronized void schedule(IWork work) throws WorkException {
		count++;
		queue.put(work);
	}

	@Override
	public final synchronized void complete(IClassMutationResult result) {
		count--;
		results.add(result);
		setChanged();
		notifyObservers(result);
		notify();
	}

	@Override
	public final synchronized void skipped() {
		count--;
		notify();
	}

	@Override
	public final synchronized List<IResult> getResults() throws InterruptedException {
		while (count > 0) {
			wait();
		}
		return results;
	}
}
