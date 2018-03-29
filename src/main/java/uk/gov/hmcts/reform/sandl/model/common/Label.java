package uk.gov.hmcts.reform.sandl.model.common;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode
public class Label
{
	@Id
	public String id;
	public String name;
}

