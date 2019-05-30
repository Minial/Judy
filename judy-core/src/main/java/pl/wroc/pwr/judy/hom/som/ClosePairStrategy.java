package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;

public class ClosePairStrategy extends AbstractOrderStrategy {

	@Override
	protected int getNextFomIndex(List<IMutant> foms) {
		return 1;
	}

}
