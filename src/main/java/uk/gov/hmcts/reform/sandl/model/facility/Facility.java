package uk.gov.hmcts.reform.sandl.model.facility;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Facility extends TimeBound
{
	public UUID locationId;
	public String name;
}

