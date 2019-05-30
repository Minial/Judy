package pl.wroc.pwr.judy.operators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;
import pl.wroc.pwr.judy.common.IEnvironment;
import pl.wroc.pwr.judy.common.IMutantBytecode;
import pl.wroc.pwr.judy.common.IMutationOperator;
import pl.wroc.pwr.judy.common.IMutationPoint;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import static org.objectweb.asm.ClassReader.SKIP_FRAMES;

/**
 * Abstract base class for all mutation operators.
 *
 * @author pmiwaszko
 */
public abstract class AbstractMutationOperator implements IMutationOperator {
	private static final Logger LOGGER = LogManager.getLogger(AbstractMutationOperator.class);
	protected static final String INIT = "<init>";
	protected static final String CLINIT = "<clinit>";
	protected static final String JAVA = "java";
	protected static final String THIS = "this$";

	/**
	 * Environment.
	 */
	private volatile IEnvironment environment;

	@Override
	public final List<IMutationPoint> getMutationPoints(final byte[] bytecode) {
		ClassReader classReader = new ClassReader(bytecode);
		AbstractClassMutater classMutater = createCountingClassMutater();
		classReader.accept(classMutater, getReaderFlags());
		return classMutater.getMutationPoints();
	}

	@Override
	public final IMutantBytecode mutate(final byte[] bytecode, final int mutationPointIndex, final int mutantIndex) {
		ClassReader cr = new ClassReader(bytecode);
		return mutate(mutationPointIndex, mutantIndex, cr);
	}

	@Override
	public final List<IMutantBytecode> mutate(final byte[] bytecode, final IMutationPoint mutationPoint) {
		List<IMutantBytecode> mutants = new LinkedList<>();
		ClassReader cr = new ClassReader(bytecode);
		for (int mutantIndex = 0; mutantIndex < mutationPoint.getMutantsCount(); mutantIndex++) {
			MutantBytecode mutant = mutate(mutationPoint.getIndex(), mutantIndex, cr);
			if (mutant != null) {
				mutants.add(mutant);
			}
		}
		return mutants;
	}

	private MutantBytecode mutate(final int mutationPointIndex, int mutantIndex, ClassReader cr) {
		try {
			trace(mutationPointIndex, mutantIndex);
			ClassWriter cw = createClassWriter(cr);
			AbstractClassMutater classMutater = createClassMutater(trace(cw), mutationPointIndex, mutantIndex);
			cr.accept(classMutater, getReaderFlags());

			// add mutated bytecode
			return new MutantBytecode(cw.toByteArray(), classMutater.getMutantLineNumber(),
					classMutater.getMutantDescription(), classMutater.getMethodName(),
					classMutater.getMethodDescriptor());
		} catch (Exception e) {
			LOGGER.debug("Mutator exception", e);
		}
		return null;
	}

	private ClassWriter createClassWriter(ClassReader cr) {
		return new ClassWriter(cr, getWriterFlags()) {
			@Override
			protected String getCommonSuperClass(String type1, String type2) {
				try {
					return super.getCommonSuperClass(type1, type2);
				} catch (RuntimeException e) {
					return "java/lang/Object";
				}
			}
		};
	}

	protected int getReaderFlags() {
		return SKIP_FRAMES;
	}

	protected int getWriterFlags() {
		return ClassWriter.COMPUTE_FRAMES;
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public String getDescription() {
		return "";
	}

	/**
	 * Set environment.
	 *
	 * @param environment environment to be set
	 */
	public void setEnvironment(IEnvironment environment) {
		this.environment = environment;
	}

	protected IEnvironment getEnvironment() {
		if (environment == null) {
			throw new IllegalStateException("Environment has not been initialized!");
		}
		return environment;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String name = getName();
		String desc = getDescription();
		if (name != null && !name.isEmpty()) {
			builder.append(name);
			if (desc != null && !desc.isEmpty()) {
				builder.append(" - ");
			}
		}
		if (desc != null && !desc.isEmpty()) {
			builder.append(desc);
		}
		return builder.toString();
	}

	@SuppressWarnings("unused")
	private void verify(ClassReader cr, IMutationPoint mutationPoint, int i) {
		if (LOGGER.isDebugEnabled()) {
			try {
				ClassWriter cw = new ClassWriter(cr, getWriterFlags());
				AbstractClassMutater cv = createClassMutater(cw, mutationPoint.getIndex(), i);
				cr.accept(new CheckClassAdapter(cv), getReaderFlags());
				CheckClassAdapter.verify(new ClassReader(cw.toByteArray()), false, new PrintWriter(System.out));
			} catch (Exception ex) {
				LOGGER.debug("Exception", ex);
			}
		}
	}

	private void trace(int mutationPointIndex, int mi) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("\n\n" + getName() + " - mutpoint: " + mutationPointIndex + ", mutindex: " + mi);
		}
	}

	private ClassVisitor trace(ClassWriter cw) {
		if (LOGGER.isTraceEnabled()) {
			return new TraceClassVisitor(cw, new PrintWriter(System.out));
		}
		return cw;
	}

	/**
	 * Create custom class mutator for counting mutation points.
	 */
	protected abstract AbstractClassMutater createCountingClassMutater();

	/**
	 * Create custom class mutater for mutating classes.
	 *
	 * @param classWriter the class writer (or class visitor) to which created mutater
	 *                    must delegate calls
	 */
	protected abstract AbstractClassMutater createClassMutater(ClassVisitor classWriter, int mutationPointIndex,
															   int mutantIndex);

}
