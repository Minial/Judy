package pl.wroc.pwr.judy.hom.som;

public enum SomStrategy {
	LAST_TO_FIRST("Last to first", "LastToFirst") {
		@Override
		public SomFactory getInstance() {
			return new LastToFirstStrategy();
		}
	},
	LAST_TO_FIRST_A("Last to first A", "LastToFirstA") {
		@Override
		public SomFactory getInstance() {
			return new LastToFirstAStrategy();
		}
	},
	LAST_TO_FIRST_B("Last to first B", "LastToFirstB") {
		@Override
		public SomFactory getInstance() {
			return new LastToFirstBStrategy();
		}
	},
	LAST_TO_FIRST_C("Last to first C", "LastToFirstC") {
		@Override
		public SomFactory getInstance() {
			return new LastToFirstCStrategy();
		}
	},
	LAST_TO_FIRST_ABC("Last to first ABCD", "LastToFirstABC") {
		@Override
		public SomFactory getInstance() {
			return new LastToFirstABCStrategy();
		}
	},
	LAST_TO_FIRST_SORTED_ABC("Last to first Sorted ABC", "LastToFirstSortedABC") {
		@Override
		public SomFactory getInstance() {
			return new LastToFirstSortedABCStrategy();
		}
	},

	LAST_TO_FIRST_SORTED("Last to first Sorted", "LastToFirstSorted") {
		@Override
		public SomFactory getInstance() {
			return new LastToFirstSortedStrategy();
		}
	},
	DIFFERENT_OPERATORS("Different operators", "DifferentOperators") {
		@Override
		public SomFactory getInstance() {
			return new DifferentOperatorsStrategy();
		}
	}, //
	CLOSE_PAIR("Close Pair", "ClosePair") {
		@Override
		public SomFactory getInstance() {
			return new ClosePairStrategy();
		}
	}, //
	RANDOM_MIX("Random Mix", "RandomMix") {
		@Override
		public SomFactory getInstance() {
			return new RandomMixStrategy();
		}
	};

	private String name;
	private String param;

	SomStrategy(final String name, final String param) {
		this.name = name;
		this.param = param;
	}

	/**
	 * Gets SOM generation strategy specified in CLI
	 *
	 * @param param strategy name parameter specified via CLI
	 * @return SOM strategy
	 */
	public static SomStrategy getByParam(final String param) {
		for (final SomStrategy strategy : values()) {
			if (strategy.getParam().equalsIgnoreCase(param)) {
				return strategy;
			}
		}
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}

	/**
	 * Gets SOM generations strategy instance
	 *
	 * @return SOM generation strategy
	 */
	public abstract SomFactory getInstance();
}
