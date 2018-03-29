package uk.gov.hmcts.reform.sandl.model.person;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Person extends Identified
{
	public String name;
}

