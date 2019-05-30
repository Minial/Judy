package pl.wroc.pwr.judy.work;

import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.IClassMutationResultPart;
import pl.wroc.pwr.judy.ITargetClass;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.utils.ClassKind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Immutable result of mutation analysis for a single class.
 *
 * @author pmiwaszko
 */
public class ClassMutationResult implements IClassMutationResult {
	private static final long serialVersionUID = 9229L;

	private final ClassKind kind;
	private final List<IMutant> aliveMutants;
	private final List<IMutant> killedMutants;
	private final int killedCount;
	private final int count;

	private String name = null;
	private IDurationStatistic statistic = null;
	private ITargetClass targetClass = null;
	private String summary = null;

	public ClassMutationResult(String name, ClassKind kind) {
		this.name = name;
		this.kind = kind;
		aliveMutants = null;
		killedMutants = null;
		killedCount = 0;
		count = 0;
	}

	public ClassMutationResult(String name, int aliveCount, List<IMutant> aliveMutants, int killedCount,
							   List<IMutant> killedMutants) {
		this.name = name;
		kind = ClassKind.NORMAL;
		this.aliveMutants = aliveMutants;
		this.killedMutants = killedMutants;
		this.killedCount = killedCount;
		count = aliveCount + killedCount;
	}

	public ClassMutationResult(String name, List<IMutant> aliveMutants, List<IMutant> killedMutants) {
		this(name, aliveMutants.size(), aliveMutants, killedMutants.size(), killedMutants);
	}

	public ClassMutationResult(String name, int aliveCount, int killedCount) {
		this(name, aliveCount, null, killedCount, null);
	}

	public ClassMutationResult(String name, List<IClassMutationResultPart> parts) {
		this.name = name;
		kind = ClassKind.NORMAL;

		int killedCounter = 0;
		int counter = 0;
		aliveMutants = new LinkedList<>();
		killedMutants = new LinkedList<>();

		if (parts != null) {
			for (IClassMutationResultPart part : parts) {
				if (part.getAliveMutants() != null) {
					aliveMutants.addAll(part.getAliveMutants());
				}
				if (part.getKilledMutants() != null) {
					killedMutants.addAll(part.getKilledMutants());
				}
				killedCounter += part.getKilledMutantsCount();
				counter += part.getMutantsCount();
			}
		}

		killedCount = killedCounter;
		count = counter;
	}

	public ClassMutationResult(ClassKind kind, ITargetClass targetClass, List<IMutant> aliveMutants,
							   List<IMutant> killedMutants, IDurationStatistic statistic, String summary) {
		this.targetClass = targetClass;
		this.kind = kind;
		this.aliveMutants = Collections.unmodifiableList(aliveMutants);
		this.killedMutants = Collections.unmodifiableList(killedMutants);
		killedCount = killedMutants.size();
		count = aliveMutants.size() + killedCount;
		this.statistic = statistic;
		this.summary = summary;
		name = targetClass.getName();
	}

	@Override
	public String getClassName() {
		return name;
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
	public double getMutationScore() {
		return count == 0 ? 0.0 : (double) killedCount / (double) count;
	}

	@Override
	public IDurationStatistic getStatistic() {
		return statistic;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public ClassKind getClassKind() {
		return kind;
	}

	@Override
	public int getKilledMutantsCount() {
		return killedMutants.size();
	}

	@Override
	public int getMutantsCount() {
		return count;
	}

	@Override
	public ITargetClass getTargetClass() {
		return targetClass;
	}

	@Override
	public List<IMutant> getMutants() {
		List<IMutant> mutants = new ArrayList<>(aliveMutants.size() + killedMutants.size());
		mutants.addAll(aliveMutants);
		mutants.addAll(killedMutants);
		return mutants;
	}
}