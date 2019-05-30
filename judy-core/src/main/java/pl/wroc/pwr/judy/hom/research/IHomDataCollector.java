package pl.wroc.pwr.judy.hom.research;

import org.moeaframework.core.Solution;
import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

/**
 * Interface for HOM research data collector (logger)
 *
 * @author TM
 */
public interface IHomDataCollector {
	/**
	 * Logs data for given HOM population
	 *
	 * @param hom  HOM mutant
	 * @param foms FOMS used to create HOM
	 * @param s    solution
	 */
	void log(IMutant hom, List<IMutant> foms, Solution s);
}