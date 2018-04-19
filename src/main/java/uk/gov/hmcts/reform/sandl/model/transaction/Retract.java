package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Data;

@Data
public class Retract
{
	private final UUID transactionId;
	private final UUID factId;

	public Retract(UUID transactionId, UUID factId)
	{
		this.transactionId = transactionId;
		this.factId = factId;
	}
}
