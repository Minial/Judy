package pl.wroc.pwr.judy.hom;

public enum HomStrategy {
	ON_THE_FLY("OnTheFly"), //
	UP_FRONT("UpFront"),
	NOT_EQUIVALENT("NotEquivalent");

	private String parameter;

	HomStrategy(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Gets Objective by name
	 *
	 * @param parameter objective parameter
	 * @return objective
	 */
	public static HomStrategy lookup(String parameter) {
		for (HomStrategy o : HomStrategy.values()) {
			if (o.getParameter().equalsIgnoreCase(parameter)) {
				return o;
			}
		}
		return null;
	}
}
