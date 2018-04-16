package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import uk.gov.hmcts.reform.sandl.model.common.Identified;

public class TransactionInserted
{
	public final UUID transactionId;
	public final Identified fact;

	public TransactionInserted(UUID transactionId, Identified fact)
	{
		this.transactionId = transactionId;
		this.fact = fact;
	}
}
