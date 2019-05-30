package pl.wroc.pwr.judy.operators;

import org.objectweb.asm.ClassVisitor;
import pl.wroc.pwr.judy.common.IMutationPoint;

import java.util.List;

import static com.google.common.collect.Lists.newLinkedList;
import static org.objectweb.asm.Opcodes.ASM5;

/**
 * Abstract base class for all class mutaters.
 *
 * @author pmiwaszko
 */
public abstract class AbstractClassMutater extends ClassVisitor implements IClassMutater, ICountingClassMutater {
	/**
	 * Indicates if this class mutater performs mutation or just counts mutants.
	 */
	private final boolean countingMutants;

	/**
	 * Index of mutation point where mutation should be performed.
	 */
	private final int mutationPointIndex;

	/**
	 * Index of possible mutation in given mutation point to perform.
	 */
	private final int mutantIndex;

	/**
	 * List of mutation points.
	 */
	private final List<IMutationPoint> mutationPoints;

	/**
	 * Number of already found mutation points.
	 */
	private int mutantionPointsCount;

	/**
	 * First line number of mutated code.
	 */
	private int mutantLineNumber;

	/**
	 * Description of performed mutation.
	 */
	private String mutantDescription;

	/**
	 * Name of method being mutated (if applicable)
	 */
	private String mutatedMethodName;

	/**
	 * Bytecode descriptor of mutated method (if applicable)
	 */
	private String mutatedMethodDescriptor;

	/**
	 * Enclosing class name.
	 */
	public String className;

	/**
	 * Constructs new {@link AbstractClassMutater} object.
	 */
	public AbstractClassMutater(ClassVisitor cv, int mutationPointIndex, int mutantIndex, boolean countingMutants) {
		super(ASM5, cv);
		this.mutationPointIndex = mutationPointIndex;
		this.mutantIndex = mutantIndex;

		this.countingMutants = countingMutants;
		if (countingMutants) {
			mutationPoints = newLinkedList();
		} else {
			mutationPoints = null;
		}

		mutantLineNumber = -1;
		mutantDescription = null;
	}

	@Override
	public List<IMutationPoint> getMutationPoints() {
		return mutationPoints;
	}

	@Override
	public int getMutantLineNumber() {
		return mutantLineNumber;
	}

	@Override
	public String getMutantDescription() {
		return mutantDescription;
	}

	@Override
	public String getMethodDescriptor() {
		return mutatedMethodDescriptor;
	}

	@Override
	public String getMethodName() {
		return mutatedMethodName;
	}

	/**
	 * Set number of the first mutated line.
	 */
	public void setMutantLineNumber(int lineNumber) {
		mutantLineNumber = lineNumber;
	}

	/**
	 * Set description of performed mutation.
	 */
	public void setMutantDescription(String description) {
		mutantDescription = description;
	}

	/**
	 * Set descriptor of method being mutated.
	 */
	public void setMutatedMethodName(String methodName) {
		mutatedMethodName = methodName;
	}

	/**
	 * Set descriptor of method being mutated.
	 */
	public void setMutatedMethodDescriptor(String descriptor) {
		mutatedMethodDescriptor = descriptor;
	}

	/**
	 * Check if this class mutater is created to count mutants instead of
	 * mutating.
	 */
	public boolean isCountingMutants() {
		return countingMutants;
	}

	/**
	 * Check if mutation should be applied at this point.
	 */
	public boolean shouldMutate() {
		return !countingMutants && mutationPointIndex == mutantionPointsCount - 1;
	}

	/**
	 * Index of mutation that should be performed.
	 */
	public int getMutantIndex() {
		return mutantIndex;
	}

	/**
	 * Add new mutation point with a single possible mutant.
	 */
	public void nextMutationPoint() {
		if (countingMutants) {
			mutationPoints.add(new MutationPoint(mutantionPointsCount, 1));
		}
		mutantionPointsCount++;
	}

	/**
	 * Add new mutation point with a given number of possible mutants.
	 *
	 * @param mutantsCount possible number of mutants
	 */
	public void nextMutationPoint(int mutantsCount) {
		if (countingMutants) {
			mutationPoints.add(new MutationPoint(mutantionPointsCount, mutantsCount));
		}
		mutantionPointsCount++;
	}
}
