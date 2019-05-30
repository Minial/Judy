package resources;

import java.io.Serializable;

@SuppressWarnings("serial")
class EnvironmentChild1Resource extends EnvironmentParentResource implements Cloneable, Serializable {
	int a;
	int b;

	protected int getC() {
		return 0;
	}
}
