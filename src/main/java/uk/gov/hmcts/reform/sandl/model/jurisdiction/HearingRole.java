package uk.gov.hmcts.reform.sandl.model.jurisdiction;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public class HearingRole extends TimeBound
{
	public UUID hearingTypeId;
	public String name;
}

