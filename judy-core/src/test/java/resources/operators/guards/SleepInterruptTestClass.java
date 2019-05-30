package resources.operators.guards;

public class SleepInterruptTestClass {

	public SleepInterruptTestClass() {
		privateMethod();
	}

	public void publicMethod() {
		privateMethod();
	}

	void defaultMethod() {
		privateMethod();
	}

	protected void protectedMethod() {
		privateMethod();
	}

	private void privateMethod() {
	}

}
