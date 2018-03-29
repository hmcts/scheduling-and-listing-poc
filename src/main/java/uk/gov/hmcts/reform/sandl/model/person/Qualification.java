package uk.gov.hmcts.reform.sandl.model.person;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Qualification extends TimeBound
{
	public UUID personId;
	public UUID hearingRoleId;
}

