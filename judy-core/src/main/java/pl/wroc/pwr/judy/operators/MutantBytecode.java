package pl.wroc.pwr.judy.operators;

import pl.wroc.pwr.judy.common.IMutantBytecode;

import java.util.Arrays;

/**
 * Immutable implementation of mutated version of bytecode.
 *
 * @author pmiwaszko
 */
public class MutantBytecode implements IMutantBytecode {

	private byte[] bytecode;
	private final int line;
	private final String description;
	private final String methodDescriptor;
	private final String methodName;

	/**
	 * MutantBytecode constructor.
	 *
	 * @param bytecode         bytecode
	 * @param lineNumber       first mutated line number
	 * @param description      mutation description
	 * @param methodName       mutated method name
	 * @param methodDescriptor method description
	 */
	public MutantBytecode(final byte[] bytecode, final int lineNumber, final String description,
						  final String methodName, final String methodDescriptor) {
		this.bytecode = bytecode;
		line = lineNumber;
		this.description = description;
		this.methodName = methodName;
		this.methodDescriptor = methodDescriptor;
	}

	/**
	 * MutantBytecode constructor.
	 *
	 * @param bytecode   mutant bytecode
	 * @param lineNumber first mutated line number
	 */
	public MutantBytecode(final byte[] bytecode, final int lineNumber) {
		this(bytecode, lineNumber, null, null, null);
	}

	/**
	 * Creates new mutant bytecode instance based on passed mutant and sets
	 * instrumented bytecode
	 *
	 * @param mutant   base mutant
	 * @param bytecode instrumented bytecode
	 */
	public MutantBytecode(IMutantBytecode mutant, byte[] bytecode) {
		this(bytecode, mutant.getLineNumber(), mutant.getDescription(), mutant.getMethodName(), mutant
				.getMethodDescriptor());
	}

	@Override
	public byte[] getBytecode() {
		return bytecode;
	}

	@Override
	public int getLineNumber() {
		return line;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getMethodDescriptor() {
		return methodDescriptor;
	}

	@Override
	public String getMethodName() {
		return methodName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytecode);
		result = prime * result + line;
		result = prime * result + (methodName == null ? 0 : methodName.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MutantBytecode other = (MutantBytecode) obj;
		if (!Arrays.equals(bytecode, other.bytecode)) {
			return false;
		}
		if (line != other.line) {
			return false;
		}
		if (methodName == null) {
			if (other.methodName != null) {
				return false;
			}
		} else if (!methodName.equals(other.methodName)) {
			return false;
		}
		return true;
	}
}
