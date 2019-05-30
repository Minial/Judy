package pl.wroc.pwr.judy.common;

import pl.wroc.pwr.judy.IClassMutationResultPart;

import java.util.List;

public class ClassMutationResultPart implements IClassMutationResultPart {
	private static final long serialVersionUID = 999L;
	private final List<IMutant> aliveMutants;
	private final List<IMutant> killedMutants;
	private final int killedCount;
	private final int count;

	public ClassMutationResultPart(int aliveCount, int killedCount) {
		aliveMutants = null;
		killedMutants = null;
		this.killedCount = killedCount;
		count = killedCount + aliveCount;
	}

	public ClassMutationResultPart(int aliveCount, List<IMutant> aliveMutants, int killedCount,
								   List<IMutant> killedMutants) {
		this.aliveMutants = aliveMutants;
		this.killedMutants = killedMutants;
		this.killedCount = killedCount;
		count = killedCount + aliveCount;
	}

	@Override
	public List<IMutant> getAliveMutants() {
		return aliveMutants;
	}

	@Override
	public List<IMutant> getKilledMutants() {
		return killedMutants;
	}

	@Override
	public int getKilledMutantsCount() {
		return killedCount;
	}

	@Override
	public int getMutantsCount() {
		return count;
	}
}
