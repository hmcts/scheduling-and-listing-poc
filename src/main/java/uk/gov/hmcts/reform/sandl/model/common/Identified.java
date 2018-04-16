package uk.gov.hmcts.reform.sandl.model.common;

import java.util.UUID;

import javax.persistence.Id;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Identified
{
	@Id
	public UUID id;
}
