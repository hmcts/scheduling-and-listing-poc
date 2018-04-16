package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

public class Uncommitted
{
	public final UUID factId;

	public Uncommitted(UUID factId)
	{
		this.factId = factId;
	}
}
