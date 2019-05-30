package pl.wroc.pwr.judy;

import pl.wroc.pwr.judy.common.IMutant;

import java.io.Serializable;
import java.util.List;

public interface IClassMutationResultPart extends Serializable {
	List<IMutant> getAliveMutants();

	List<IMutant> getKilledMutants();

	int getKilledMutantsCount();

	int getMutantsCount();
}
