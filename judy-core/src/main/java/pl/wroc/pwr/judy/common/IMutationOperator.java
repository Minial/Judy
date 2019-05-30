package pl.wroc.pwr.judy.common;

import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * Common interface for all mutation operators.
 *
 * @author pmiwaszko, TM
 */
public interface IMutationOperator extends IDescriptable, Opcodes {
	/**
	 * Gets all possible points in given class (bytecode) where different
	 * mutants can be produced.
	 *
	 * @param bytecode bytecode to be analyzed
	 * @return list of mutation points witihn given class (bytecode)
	 */
	List<IMutationPoint> getMutationPoints(byte[] bytecode);

	/**
	 * Mutate given bytecode to create mutants with given mutation points.
	 *
	 * @param bytecode      bytecode to be mutated
	 * @param mutationPoint mutation point in given class (bytecode)
	 * @return list of mutants created within given point
	 */
	List<IMutantBytecode> mutate(byte[] bytecode, IMutationPoint mutationPoint);

	/**
	 * @param bytecode           bytecode to be mutated
	 * @param mutationPointIndex mutation point in given class (bytecode)
	 * @param mutantIndex        specific mutant index within given point
	 * @return mutated bytecode - mutant
	 */
	IMutantBytecode mutate(byte[] bytecode, int mutationPointIndex, int mutantIndex);
}
