package uk.gov.hmcts.reform.sandl.model.transaction;

import java.util.UUID;

import lombok.Getter;
import lombok.ToString;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Getter
@ToString
public class Assert extends Change
{
	private final Identified fact;

	public Assert(UUID transactionId, Identified fact)
	{
		super(transactionId, fact.id);
		this.fact = fact;
	}
}
