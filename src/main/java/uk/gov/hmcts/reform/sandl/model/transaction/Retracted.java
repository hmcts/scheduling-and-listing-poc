package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Data;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Data
public class Retracted
{
	private final UUID transactionId;
	private final Identified fact;
	private final UUID factId;

	public Retracted(UUID transactionId, Identified fact)
	{
		this.transactionId = transactionId;
		this.fact = fact;
		this.factId = fact.id;
	}
}
