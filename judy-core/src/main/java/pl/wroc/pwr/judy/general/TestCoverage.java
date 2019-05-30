package pl.wroc.pwr.judy.general;

import java.util.HashSet;
import java.util.Set;

public class TestCoverage {
	private final String testClassName;
	private Set<MethodCoverage> methodCoverage = new HashSet<>();

	/**
	 * Default constructor
	 *
	 * @param testClassName test class name
	 */
	public TestCoverage(String testClassName) {
		this.testClassName = testClassName;
	}

	/**
	 * @return the testClassName
	 */
	public String getTestClassName() {
		return testClassName;
	}

	/**
	 * Adds method coverage
	 *
	 * @param method method coverage
	 */
	public void addMethod(MethodCoverage method) {
		if (!methodCoverage.add(method)) {
			fillExistingMethodLines(method);
		}
	}

	private void fillExistingMethodLines(MethodCoverage newMethod) {
		for (MethodCoverage mc : methodCoverage) {
			if (mc.equals(newMethod)) {
				mc.addLines(newMethod.getCoveredLines());
			}
		}
	}

	/**
	 * @return the methodCoverage
	 */
	public Set<MethodCoverage> getMethodCoverage() {
		return methodCoverage;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (methodCoverage == null ? 0 : methodCoverage.hashCode());
		result = prime * result + (testClassName == null ? 0 : testClassName.hashCode());
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
		if (!(obj instanceof TestCoverage)) {
			return false;
		}
		TestCoverage other = (TestCoverage) obj;
		if (testClassName == null) {
			if (other.testClassName != null) {
				return false;
			}
		} else if (!testClassName.equals(other.testClassName)) {
			return false;
		}
		return true;
	}

}
