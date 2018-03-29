package uk.gov.hmcts.reform.sandl.model.venue;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Region extends Identified
{
	public String name;
}

