package uk.gov.hmcts.reform.sandl.model.hearing;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class HearingParticipant extends Identified
{
	public UUID hearingPartId;
	public UUID hearingRoleId;
	public UUID personId;
	public UUID hearingLocationId;
}

