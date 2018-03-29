package uk.gov.hmcts.reform.sandl.model.hearing;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class HearingFacility extends Identified
{
	public UUID hearingPartId;
	public UUID facilityId;
	public UUID hearingLocationId;
}

