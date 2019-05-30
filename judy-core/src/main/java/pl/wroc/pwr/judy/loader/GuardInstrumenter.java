package pl.wroc.pwr.judy.loader;

import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.common.IMutationPoint;
import pl.wroc.pwr.judy.operators.guards.InfiniteLoopGuard;
import pl.wroc.pwr.judy.operators.guards.InterruptionGuard;

import java.util.ArrayList;
import java.util.List;

public class GuardInstrumenter implements Instrumenter {
	private List<IMutationOperator> guards = new ArrayList<>(2);

	/**
	 * Creates instrumenter
	 *
	 * @param timeout loop timeout in milliseconds
	 */
	public GuardInstrumenter(long timeout) {
		guards.add(new InfiniteLoopGuard(timeout));
		guards.add(new InterruptionGuard());
	}

	@Override
	public byte[] instrument(byte[] mutatedBytecode) {
		byte[] bytecode = mutatedBytecode;
		for (IMutationOperator guard : guards) {
			bytecode = instrument(bytecode, guard);
		}
		return bytecode;
	}

	private byte[] instrument(byte[] mutatedBytecode, IMutationOperator guard) {
		byte[] bytecode = mutatedBytecode;
		List<IMutationPoint> points = guard.getMutationPoints(bytecode);
		for (IMutationPoint p : points) {
			bytecode = guard.mutate(bytecode, p).iterator().next().getBytecode();
		}
		return bytecode;
	}
}
