package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Data;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Data
public class Asserted
{
	private final UUID transactionId;
	private final Identified fact;
	private final UUID factId;

	public Asserted(UUID transactionId, Identified fact)
	{
		this.transactionId = transactionId;
		this.fact = fact;
		this.factId = fact.id;
	}
}
