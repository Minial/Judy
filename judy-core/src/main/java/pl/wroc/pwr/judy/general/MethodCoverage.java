package pl.wroc.pwr.judy.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodCoverage {
	private final String methodName;
	private final ArrayList<Integer> coveredLines = new ArrayList<>();

	/**
	 * Default constructor
	 *
	 * @param methodName test method name
	 */
	public MethodCoverage(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * Constructor for testing.
	 *
	 * @param methodName test method name
	 * @param lines      covered lines
	 */
	public MethodCoverage(String methodName, List<Integer> lines) {
		this(methodName);
		addLines(lines);
	}

	/**
	 * Adds multiple covered lines
	 *
	 * @param lines covered lines
	 */
	public void addLines(Collection<Integer> lines) {
		for (int line : lines) {
			addLine(line);
		}
	}

	/**
	 * Adds covered line
	 *
	 * @param lineNumber line number
	 */
	public void addLine(int lineNumber) {
		coveredLines.add(lineNumber);
	}

	/**
	 * @return the coveredLines
	 */
	public List<Integer> getCoveredLines() {
		return coveredLines;
	}

	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Trims size of covered lines collection in order to save up memmory
	 */
	public void trimSize() {
		coveredLines.trimToSize();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (!(obj instanceof MethodCoverage)) {
			return false;
		}
		MethodCoverage other = (MethodCoverage) obj;
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
