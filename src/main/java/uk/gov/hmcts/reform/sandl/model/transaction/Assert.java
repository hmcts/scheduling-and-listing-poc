package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Data
@Getter
public class Assert
{
	private final UUID transactionId;
	@Getter
	private final Identified fact;
	private final UUID factId;

	public Assert(UUID transactionId, Identified fact)
	{
		this.transactionId = transactionId;
		this.fact = fact;
		this.factId = fact.id;
	}
}
