package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

public class TransactionDelete
{
	public final UUID transactionId;
	public final UUID factId;

	public TransactionDelete(UUID transactionId, UUID factId)
	{
		this.transactionId = transactionId;
		this.factId = factId;
	}
}
