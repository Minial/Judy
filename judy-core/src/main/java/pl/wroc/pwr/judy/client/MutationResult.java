package pl.wroc.pwr.judy.client;

import pl.wroc.pwr.judy.IClassMutationResult;
import pl.wroc.pwr.judy.IMutationResult;
import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IDescriptable;
import pl.wroc.pwr.judy.common.IDurationStatistic;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * @author pmiwaszko, mszewczyszyn
 */
public class MutationResult implements IMutationResult {
	private final Date date;
	private final long duration;
	private final List<IDescriptable> operators;
	private final List<ITestResult> tests;
	private final List<IClassMutationResult> results;

	/**
	 * MutationResult constructor.
	 */
	public MutationResult(long duration, List<IDescriptable> operators, List<ITestResult> tests,
						  List<IClassMutationResult> results) {
		date = new GregorianCalendar().getTime();
		this.duration = duration;
		this.operators = operators;
		this.tests = tests;
		this.results = results;
	}

	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public long getDuration() {
		return duration;
	}

	@Override
	public List<IDescriptable> getOperators() {
		return operators;
	}

	@Override
	public List<ITestResult> getTests() {
		return tests;
	}

	@Override
	public List<IClassMutationResult> getResults() {
		return results;
	}

	@Override
	public int getMutantsCount() {
		return getMutants().size();
	}

	@Override
	public int getKilledMutantsCount() {
		return getKilledMutants().size();
	}

	@Override
	public int getKilledByInfiniteLoopGuardMutantsCount() {
		List<IMutant> killed = getKilledMutantsByInfiniteLoopGuard();
		// FIXME: Remove all killed by timeout because when mutant is killed by
		// timeout,
		// then InfiniteLoopException is thrown as well (needs investigation).
		killed.removeAll(getKilledByTimeoutMutants());
		return killed.size();
	}

	@Override
	public int getKilledByWorkTimeoutMutantsCount() {
		return getKilledByTimeoutMutants().size();
	}

	private List<IMutant> getKilledMutants() {
		ArrayList<IMutant> mutants = new ArrayList<>();
		for (IClassMutationResult result : results) {
			mutants.addAll(result.getKilledMutants());
		}
		mutants.trimToSize();
		return mutants;
	}

	private Set<IMutant> getKilledByTimeoutMutants() {
		Set<IMutant> mutants = new HashSet<>();
		for (IClassMutationResult result : results) {
			List<IMutant> killedMutants = result.getKilledMutants();
			for (IMutant mutant : killedMutants) {
				checkNewMutant:
				for (ITestResult testResult : mutant.getResults()) {
					List<Throwable> exceptions = testResult.getThrownExceptions();
					for (Throwable t : exceptions) {
						if (t instanceof TimeoutException) {
							mutants.add(mutant);
							break checkNewMutant;
						}
					}
				}
			}
		}
		return mutants;
	}

	private List<IMutant> getKilledMutantsByInfiniteLoopGuard() {
		ArrayList<IMutant> mutants = new ArrayList<>();
		for (IClassMutationResult result : results) {
			List<IMutant> killedMutants = result.getKilledMutants();
			for (IMutant mutant : killedMutants) {
				int failedBecauseOfInfiniteLoopGuard = mutant.getResults()
						.getFailingTestMethodsBecauseOfInfiniteLoopGuard().size();
				int failed = mutant.getResults().getFailingTestMethods().size();
				if (failedBecauseOfInfiniteLoopGuard == failed) {
					mutants.add(mutant);
				}
			}
		}
		mutants.trimToSize();
		return mutants;
	}

	@Override
	public List<IMutant> getMutants() {
		ArrayList<IMutant> mutants = new ArrayList<>();
		for (IClassMutationResult result : results) {
			mutants.addAll(result.getMutants());
		}
		mutants.trimToSize();
		return mutants;
	}

	@Override
	public int getTestsCount() {
		return tests.size();
	}

	@Override
	public int getClassesCount() {
		return results.size();
	}

	@Override
	public long getMutantGenerationDuration() {
		long generationDuration = 0;
		for (IDurationStatistic stat : getStatistics()) {
			generationDuration += stat.getGenerationDuration();
		}
		return generationDuration;
	}

	@Override
	public long getTestsDuration() {
		long testsDuration = 0;
		for (IDurationStatistic stat : getStatistics()) {
			testsDuration += stat.getTestsDuration();
		}
		return testsDuration;
	}

	private List<IDurationStatistic> getStatistics() {
		ArrayList<IDurationStatistic> stats = new ArrayList<>(results.size());
		for (IClassMutationResult classMutationResult : results) {
			if (classMutationResult != null) {
				stats.add(classMutationResult.getStatistic());
			}
		}
		stats.trimToSize();
		return stats;
	}

}
