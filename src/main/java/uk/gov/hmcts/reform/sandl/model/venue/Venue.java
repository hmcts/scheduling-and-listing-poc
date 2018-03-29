package uk.gov.hmcts.reform.sandl.model.venue;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Venue extends Identified
{
	public UUID regionId;
	public UUID clusterId;
	public String name;
}

