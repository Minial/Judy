package pl.wroc.pwr.judy.common;

import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.general.TestResultList;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Interface for representation of applied mutation.
 *
 * @author pmiwaszko
 */

/**
 * @author tmuc
 */
public interface IMutant extends Comparable<IMutant>, Serializable, Cloneable {
	/**
	 * Get names of the mutation operators.
	 *
	 * @return names
	 */
	List<String> getOperatorsNames();

	/**
	 * Get the mutation point index.
	 *
	 * @return mutation points indexes
	 */
	List<Integer> getMutionPointsIndexes();

	/**
	 * Get the mutant index.
	 *
	 * @return mutant index
	 */
	int getMutantIndex();

	/**
	 * Add another mutation information for higher order mutants
	 *
	 * @param line          fist mutated line number
	 * @param operator      operator name
	 * @param mutationPoint operator related mutation point
	 * @param mutationIndex index of mutation in given mutation point
	 */
	void addMutation(int line, String operator, int mutationPoint, int mutationIndex);

	/**
	 * Get starting lines of mutations
	 *
	 * @return list of first mutated line numbers of different mutations
	 */
	List<Integer> getLinesNumbers();

	/**
	 * Get descriptive details of mutant. Might be null.
	 *
	 * @return mutant description, may be null.
	 */
	String getDescription();

	/**
	 * Sets descriptive details of mutant
	 *
	 * @param description mutant details, might be null
	 */
	void setDescription(String description);

	/**
	 * Returns index of operator applied lately
	 *
	 * @return operator index
	 */
	int getLastOperatorIndex();

	/**
	 * Setter for mutant's bytecode
	 *
	 * @param bytecode mutant bytecode
	 */
	void setBytecode(IMutantBytecode bytecode);

	/**
	 * Getter for mutant's bytecode
	 *
	 * @return mutant bytecode wrapper
	 */
	IMutantBytecode getBytecode();

	/**
	 * Sets mutant identifier, unique in scope of all created mutants
	 *
	 * @param id mutant id to set
	 */
	void setId(int id);

	/**
	 * Sets mutant identifier, unique in scope of all created mutants
	 *
	 * @return -1 if ID has not been set yet, otherwise its unique identifier
	 */
	int getId();

	/**
	 * Stores test run results details for evaluation
	 *
	 * @param result test run results
	 */
	void saveResult(ITestResult result);

	/**
	 * Gets all test run results for evaluation
	 *
	 * @return test run results
	 */
	TestResultList getResults();

	/**
	 * Gets mutated target class name
	 *
	 * @return target class name
	 */
	String getTargetClassName();

	/**
	 * Get mutant order
	 *
	 * @return order
	 */
	int getOrder();

	/**
	 * Gets mutants indexes in corresponding mutation points
	 *
	 * @return mutation indexes
	 */
	LinkedList<Integer> getMutationIndexes();
}
