package uk.gov.hmcts.reform.sandl.model.common;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Transactional extends Identified
{
	public boolean committed;
}
