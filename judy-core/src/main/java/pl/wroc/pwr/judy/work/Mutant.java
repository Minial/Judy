package pl.wroc.pwr.judy.work;

import pl.wroc.pwr.judy.ITestResult;
import pl.wroc.pwr.judy.common.IMutant;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.general.TestResultList;

import java.util.LinkedList;
import java.util.List;

/**
 * Representation of applied mutation.
 *
 * @author pmiwaszko
 */
public class Mutant implements IMutant {
	private static final long serialVersionUID = 666L;

	private final List<String> operatorsNames;
	private final List<Integer> points;
	private final LinkedList<Integer> indexes;
	private final List<Integer> linesNumbers;
	private String details;
	private final List<Integer> operatorsIndexes;
	private IMutantBytecode mutantBytecode;
	private int id;
	private TestResultList testResults = new TestResultList();
	private String targetClassName;
	public static int nbrMutant=0;

	/**
	 * Mutant constructor.
	 *
	 * @param operatorName    name of the mutation operator
	 * @param point           mutation point (one of the available mutation points for given
	 *                        operator)
	 * @param index           mutant index (in scope of given class)
	 * @param targetClassName mutated class name
	 * @param line            mutated line number
	 * @param details         mutant description
	 * @param bytecode        bytecode with applied mutation
	 * @param operatorIndex   index of mutation operator
	 */
	public Mutant(String operatorName, int point, int index, String targetClassName, int line, String details,
				  IMutantBytecode bytecode, int operatorIndex) {
		operatorsNames = new LinkedList<>();
		operatorsNames.add(operatorName);
		points = new LinkedList<>();
		points.add(point);
		indexes = new LinkedList<>();
		indexes.add(index);
		linesNumbers = new LinkedList<>();
		linesNumbers.add(line);
		this.details = details;
		operatorsIndexes = new LinkedList<>();
		operatorsIndexes.add(operatorIndex);
		mutantBytecode = bytecode;
		this.targetClassName = targetClassName;
	}

	/**
	 * Shallow copy constructor
	 *
	 * @param mutant base mutant to copy
	 */
	public Mutant(IMutant mutant) {
		this(mutant.getOperatorsNames().get(0), mutant.getMutionPointsIndexes().get(0), mutant.getMutantIndex(), mutant
						.getTargetClassName(), mutant.getLinesNumbers().get(0), mutant.getDescription(), mutant.getBytecode(),
				mutant.getLastOperatorIndex());
	}

	/**
	 * Constructor used in tests
	 *
	 * @param operatorName    name of the mutation operator
	 * @param point           mutation point (one of the available mutation points for given
	 *                        operator)
	 * @param index           mutant index (in scope of given class)
	 * @param targetClassName mutated class name
	 * @param line            mutated line number
	 * @param operatorIndex   index of mutation operator
	 */
	public Mutant(String operatorName, int point, int index, String targetClassName, int line, int operatorIndex) {
		this(operatorName, point, index, targetClassName, line, null, null, operatorIndex);
	}

	@Override
	public void addMutation(int line, String operator, int mutationPoint, int mutationIndex) {
		linesNumbers.add(line);
		operatorsNames.add(operator);
		points.add(mutationPoint);
		indexes.add(mutationIndex);
		HashToId();
	}

	@Override
	public List<String> getOperatorsNames() {
		return operatorsNames;
	}

	@Override
	public void HashToId() {//create a unique id to identify each mutant
		id=0;
		String temp;
		for (String OpNames : operatorsNames) {
			id+=OpNames.hashCode();
		}/*
		for (char OpNamesChar : temp.toCharArray()) {
			id+=getNumeric
		}*/
		for(int point : points) {
			id+=point;
		}
		for(int index : indexes) {
			id+=index;
		}
		for(int line : linesNumbers) {
			id+=line;
		}
		for(int operatorIndex : operatorsIndexes) {
			id+=operatorIndex;
		}
		nbrMutant++;
		//System.out.println("id : " + id + "\t nbrMutant : " + nbrMutant);
		
	}
	
	@Override
	public List<Integer> getMutionPointsIndexes() {
		return points;
	}

	@Override
	public int getMutantIndex() {
		return indexes.getLast();
	}

	@Override
	public List<Integer> getLinesNumbers() {
		return linesNumbers;
	}

	@Override
	public String getDescription() {
		return details;
	}

	@Override
	public void setDescription(String description) {
		details = description;
	}

	@Override
	public int getLastOperatorIndex() {
		int size = operatorsIndexes.size();
		return operatorsIndexes.get(size - 1);
	}

	@Override
	public void setBytecode(IMutantBytecode bytecode) {
		mutantBytecode = bytecode;
	}

	@Override
	public IMutantBytecode getBytecode() {
		return mutantBytecode;
	}

	/**
	 * @return the id
	 */
	@Override
	public int getId() {
		//System.out.println("get id : " + id + "\t nbrMutant : " + nbrMutant);
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(int id) {
		//System.out.println("set id : " + id + "\t nbrMutant : " + nbrMutant);
		this.id = id;
	}

	@Override
	public LinkedList<Integer> getMutationIndexes() {
		return indexes;
	}

	@Override
	public int compareTo(IMutant o) {
		if (operatorsNames.equals(o.getOperatorsNames())) {
			return -1;
		}
		if (!points.equals(o.getMutionPointsIndexes())) {
			return -1;
		}
		int cmp = getMutantIndex() - o.getMutantIndex();
		if (cmp != 0) {
			return cmp;
		}
		return 0;
	}

	@Override
	public void saveResult(ITestResult result) {
		testResults.add(result);
	}

	@Override
	public TestResultList getResults() {
		return testResults;
	}

	/**
	 * @return the targetClassName
	 */
	@Override
	public String getTargetClassName() {
		return targetClassName;
	}

	@Override
	public int getOrder() {
		return operatorsNames.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (indexes == null ? 0 : indexes.hashCode());
		result = prime * result + (mutantBytecode == null ? 0 : mutantBytecode.hashCode());
		result = prime * result + (operatorsNames == null ? 0 : operatorsNames.hashCode());
		result = prime * result + (points == null ? 0 : points.hashCode());
		result = prime * result + (targetClassName == null ? 0 : targetClassName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Mutant)) {
			return false;
		}
		Mutant other = (Mutant) obj;

		if (targetClassName == null) {
			if (other.targetClassName != null) {
				return false;
			}
		} else if (!targetClassName.equals(other.targetClassName)) {
			return false;
		}

		if (mutantBytecode == null) {
			if (other.mutantBytecode != null) {
				return false;
			}
		} else if (!mutantBytecode.equals(other.mutantBytecode)) {
			return false;
		}

		if (operatorsNames == null) {
			if (other.operatorsNames != null) {
				return false;
			}
		} else if (!operatorsNames.equals(other.operatorsNames)) {
			return false;
		}
		if (points == null) {
			if (other.points != null) {
				return false;
			}
		} else if (!points.equals(other.points)) {
			return false;
		}

		if (indexes == null) {
			if (other.indexes != null) {
				return false;
			}
		} else if (!indexes.equals(other.indexes)) {
			return false;
		}

		return true;
	}
}
