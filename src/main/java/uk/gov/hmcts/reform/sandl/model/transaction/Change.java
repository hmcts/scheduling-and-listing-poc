package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Getter;

@Getter
public abstract class Change extends Command
{
	private final UUID factId;

	protected Change(UUID transactionId, UUID factId)
	{
		super(transactionId);
		this.factId = factId;
	}
}
