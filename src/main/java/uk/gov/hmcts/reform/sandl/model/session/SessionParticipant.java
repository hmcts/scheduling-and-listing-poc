package uk.gov.hmcts.reform.sandl.model.session;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class SessionParticipant extends Identified
{
	public UUID sessionId;
	public UUID hearingRoleId;
	public UUID personId;
	public UUID sessionLocationId;
}

