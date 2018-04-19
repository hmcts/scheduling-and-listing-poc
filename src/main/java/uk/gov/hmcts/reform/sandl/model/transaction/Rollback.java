package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Data;

@Data
public class Rollback
{
	private final UUID transactionId;

	public Rollback(UUID transactionId)
	{
		this.transactionId = transactionId;
	}
}
