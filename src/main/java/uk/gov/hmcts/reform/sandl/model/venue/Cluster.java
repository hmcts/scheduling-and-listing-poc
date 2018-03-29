package uk.gov.hmcts.reform.sandl.model.venue;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Cluster extends Identified
{
	public UUID regionId;
	public String name;
}

