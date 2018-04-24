package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Change
{
	private final UUID transactionId;
	private final UUID factId;
}
