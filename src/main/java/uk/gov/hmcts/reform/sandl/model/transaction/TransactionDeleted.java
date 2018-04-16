package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import uk.gov.hmcts.reform.sandl.model.common.Identified;

public class TransactionDeleted
{
	public final UUID transactionId;
	public final Identified fact;

	public TransactionDeleted(UUID transactionId, Identified fact)
	{
		this.transactionId = transactionId;
		this.fact = fact;
	}
}
