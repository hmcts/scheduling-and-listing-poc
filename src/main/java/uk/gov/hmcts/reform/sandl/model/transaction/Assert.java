package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Data;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Data
public class Assert
{
	private final UUID transactionId;
	private final Identified fact;

	public Assert(UUID transactionId, Identified fact)
	{
		this.transactionId = transactionId;
		this.fact = fact;
	}
}
