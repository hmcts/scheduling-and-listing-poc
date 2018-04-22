package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

public class Rollback extends Command
{
	public Rollback(UUID transactionId)
	{
		super(transactionId);
	}
}
