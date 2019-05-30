package pl.wroc.pwr.cluster;

import pl.wroc.pwr.judy.IClassMutationResultPart;

import java.io.Serializable;
import java.util.Map;

public interface ISubresult extends Serializable {
	long getSubtaskId();

	Map<String, IClassMutationResultPart> getClassResults();

	long getTestsDuration();

	long getGenerationDuration();
}
