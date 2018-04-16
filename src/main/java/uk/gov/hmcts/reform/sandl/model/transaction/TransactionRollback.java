package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

public class TransactionRollback
{
	public final UUID transactionId;

	public TransactionRollback(UUID transactionId)
	{
		this.transactionId = transactionId;
	}
}
