package uk.gov.hmcts.reform.sandl.model.session;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class SessionFacility extends Identified
{
	public UUID sessionId;
	public UUID facilityId;
	public UUID sessionLocationId;
}

