package pl.wroc.pwr.judy.operators.polymorphism;

import org.junit.Test;
import pl.wroc.pwr.judy.helpers.AbstractMutationOperatorTesting;
import pl.wroc.pwr.judy.helpers.Mutate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

public class OMD2Test extends AbstractMutationOperatorTesting {

	@Test
	public void test() throws Exception {
		assertMutationPointsCountEquals(4);
		assertMutantsCountEquals(4);

		assertArrayEquals(
				list("pop(String)String[]", "push()String[]", "push(String)String[]", "push(StringString)String",
						"push(int)String[]", "push(intStringint)String[]", "test()String[]"),
				(String[]) getMethodResult());

		try {
			assertMutatedMethodResultEquals(null, 0);
			fail();
		} catch (InvocationTargetException e) {
		}
		assertArrayEquals(
				list("pop(String)String[]", "push()String[]", "push(String)String[]", "push(StringString)String",
						"push(intStringint)String[]", "test()String[]"), (String[]) getMutatedMethodResult(1));
		assertArrayEquals(
				list("pop(String)String[]", "push(String)String[]", "push(StringString)String", "push(int)String[]",
						"push(intStringint)String[]", "test()String[]"), (String[]) getMutatedMethodResult(2));
		assertArrayEquals(
				list("pop(String)String[]", "push()String[]", "push(StringString)String", "push(int)String[]",
						"push(intStringint)String[]", "test()String[]"), (String[]) getMutatedMethodResult(3));
	}

	@Mutate(operator = OMD.class)
	public class TestClass {
		public String[] test() {
			return push(2, "TEST", 1);
		}

		public String[] push(int a, String s, int b) {
			Method[] methods = this.getClass().getDeclaredMethods();
			String[] result = new String[methods.length];
			for (int i = 0; i < methods.length; i++) {
				result[i] = getDesc(methods[i]);
			}
			Arrays.sort(result);
			return result;
		}

		protected String[] push(int a) {
			return null;
		}

		protected String[] push() {
			return null;
		}

		protected String[] push(String s) {
			return null;
		}

		protected String push(String s1, String s2) {
			return "TEST";
		}

		protected String[] pop(String s) {
			return null;
		}
	}

	private String[] list(String... strings) {
		return strings;
	}

	public String getDesc(Method method) {
		StringBuilder builder = new StringBuilder();

		builder.append(method.getName());
		builder.append("(");
		for (Class<?> c : method.getParameterTypes()) {
			builder.append(c.getSimpleName());
		}
		builder.append(")");
		builder.append(method.getReturnType().getSimpleName());

		return builder.toString();
	}
}
