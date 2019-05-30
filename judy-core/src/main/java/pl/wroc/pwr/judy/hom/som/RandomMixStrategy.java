package pl.wroc.pwr.judy.hom.som;

import pl.wroc.pwr.judy.common.IMutant;

import java.util.List;
import java.util.Random;

/**
 * RandomMix strategy for generating SOMs.
 */
public class RandomMixStrategy extends AbstractOrderStrategy {

	private Random rnd = new Random();

	@Override
	protected int getNextFomIndex(List<IMutant> foms) {
		return rnd.nextInt(foms.size() - 1) + 1;
	}
}
