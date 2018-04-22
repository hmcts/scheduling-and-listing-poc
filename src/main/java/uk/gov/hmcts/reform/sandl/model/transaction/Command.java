package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class Command
{
	private final UUID transactionId;

	protected Command(UUID transactionId)
	{
		this.transactionId = transactionId;
	}
}