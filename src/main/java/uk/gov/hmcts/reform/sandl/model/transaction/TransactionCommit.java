package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

public class TransactionCommit
{
	public final UUID transactionId;

	public TransactionCommit(UUID transactionId)
	{
		this.transactionId = transactionId;
	}
}
