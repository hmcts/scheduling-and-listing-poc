package uk.gov.hmcts.reform.sandl.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import uk.gov.hmcts.reform.sandl.model.transaction.Change;

@AllArgsConstructor
@ToString
public class UserTransactionEvent
{
	@Getter
	private final UUID transactionId;
	private final Map<UUID, Change> changes = new HashMap<>();

	public void add(Change change)
	{
		changes.put(change.getFactId(), change);
	}

	public Collection<Change> getChanges()
	{
		return changes.values();
	}
}
