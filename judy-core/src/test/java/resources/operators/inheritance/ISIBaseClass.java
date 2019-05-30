package resources.operators.inheritance;

@SuppressWarnings("unused")
public class ISIBaseClass {
	int def = 1;
	public int pub = 2;
	protected int prot = 3;
	private final int priv = 4;
	protected int inherited = 100;

	protected static int protstat = 5;

	int def() {
		return 1;
	}

	public int pub() {
		return 2;
	}

	protected int prot() {
		return 3;
	}

	private int priv() {
		return 4;
	}

	protected int inherited() {
		return 100;
	}

	protected static int protstat() {
		return 5;
	}
}
