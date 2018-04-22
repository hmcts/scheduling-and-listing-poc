package uk.gov.hmcts.reform.sandl.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.hmcts.reform.sandl.model.transaction.Change;

@AllArgsConstructor
public class UserTransaction
{
	@Getter
	private final UUID id;
	private final Map<UUID, Change> causes = new HashMap<>();
	private final Map<UUID, Change> effects = new HashMap<>();

	public void addCause(Change change)
	{
		causes.put(change.getFactId(), change);
	}

	public Collection<Change> getCauses()
	{
		return causes.values();
	}


	public void addEffect(Change change)
	{
		effects.put(change.getFactId(), change);
	}

	public Collection<Change> getEffects()
	{
		return effects.values();
	}
}
