package pl.wroc.pwr.judy.hom.objectives;

import pl.wroc.pwr.judy.common.IMutant;

import static org.junit.Assert.assertEquals;

public abstract class ObjectiveCalculatorTest {

	protected static final double PRECISION = 0.0001;
	protected static final String DESCRIPTION = "desc";

	protected void checkCalculatedValue(double expected, IMutant hom, ObjectiveCalculator calculator) {
		assertEquals(expected, calculator.calculate(hom, null, hom.getOrder()), PRECISION);
	}

}
