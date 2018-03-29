package uk.gov.hmcts.reform.sandl.model.venue;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Room extends Identified
{
	public UUID venueId;
	public String name;
}

