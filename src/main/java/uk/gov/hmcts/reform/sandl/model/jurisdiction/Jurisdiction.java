package uk.gov.hmcts.reform.sandl.model.jurisdiction;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Jurisdiction extends TimeBound
{
	public String name;
}

