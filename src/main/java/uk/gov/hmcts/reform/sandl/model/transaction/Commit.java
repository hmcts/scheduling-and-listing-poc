package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

public class Commit extends Command
{
	public Commit(UUID transactionId)
	{
		super(transactionId);
	}
}
