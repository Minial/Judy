package pl.wroc.pwr.judy.operators.guards;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import pl.wroc.pwr.judy.operators.AbstractClassMutater;
import pl.wroc.pwr.judy.operators.AbstractMutationOperator;

public class InfiniteLoopGuard extends AbstractMutationOperator {
	private static final String DESCRIPTION = "Introduces escape point in every loop in order to avoid inifinite loop execution";
	public static final long DEFAULT_TIMEOUT = 5000l;
	private long timeout = DEFAULT_TIMEOUT;

	/**
	 * Constructs infinite loop guard with default timeout
	 */
	public InfiniteLoopGuard() {
	}

	/**
	 * @param timeout loop timeout in milliseconds
	 */
	public InfiniteLoopGuard(long timeout) {
		this.timeout = timeout;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	protected AbstractClassMutater createCountingClassMutater() {
		return new InfiniteLoopGuardClassMutator();
	}

	@Override
	protected AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex, int mutantIndex) {
		return new InfiniteLoopGuardClassMutator(classWriter, mutationPointIndex, mutantIndex, getTimeout());
	}

	@Override
	protected int getWriterFlags() {
		return ClassWriter.COMPUTE_FRAMES;
	}

	/**
	 * Overridable method for testing purposes
	 *
	 * @return loop timeout in milliseconds
	 */
	public long getTimeout() {
		return timeout;
	}

}