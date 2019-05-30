package pl.wroc.pwr.judy.hom.objectives;

public enum ObjectiveType {
	FITNESS("fitness") {
		@Override
		public ObjectiveCalculator getInstance() {
			return new FitnessObjective(getName(), new FragilityCalculator());
		}
	},
	MAX_OPERATOR_DIVERSITY("maxOperatorDiversity") {
		@Override
		public ObjectiveCalculator getInstance() {
			return new MaxOperatorDiversityObjective(getName());
		}
	},
	MIN_OPERATOR_DIVERSITY("minOperatorDiversity") {
		@Override
		public ObjectiveCalculator getInstance() {
			return new MinOperatorDiversityObjective(getName());
		}
	},
	MUTATION_ORDER("mutationOrder") {
		@Override
		public ObjectiveCalculator getInstance() {
			return new MutationOrderObjective(getName());
		}
	/*QV_Nguyen*/
	},
	MIN_TCsSSHOM("MinTCsSSHOM") {
		@Override
		public ObjectiveCalculator getInstance() { return new MinTCsSSHOMObjective(getName()); }
	},
	MIN_TCsNew("MinTCsNew") {
		@Override
		public ObjectiveCalculator getInstance() { return new MinTCsNewObjective(getName()); }
	},
	MIN_TCsOld("MinTCsOld") {
		@Override
		public ObjectiveCalculator getInstance() { return new MinTCsOldObjective(getName()); }
	};

	private String name;

	private ObjectiveType(String param) {
		name = param;
	}

	/**
	 * Objective name
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets Objective by name
	 *
	 * @param name objective nme
	 * @return objective
	 */
	public static ObjectiveType lookup(String name) {
		for (ObjectiveType o : ObjectiveType.values()) {
			if (o.getName().equalsIgnoreCase(name)) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Gets objective calculator instance
	 *
	 * @return calculator instance
	 */
	public abstract ObjectiveCalculator getInstance();
}
