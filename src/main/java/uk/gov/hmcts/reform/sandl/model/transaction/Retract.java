package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.ToString;

@ToString
public class Retract extends Change
{
	public Retract(UUID transactionId, UUID factId)
	{
		super(transactionId, factId);
	}
}
