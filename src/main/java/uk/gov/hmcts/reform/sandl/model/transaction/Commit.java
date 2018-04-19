package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Data;

@Data
public class Commit
{
	private final UUID transactionId;

	public Commit(UUID transactionId)
	{
		this.transactionId = transactionId;
	}
}
