package uk.gov.hmcts.reform.sandl.model.jurisdiction;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public class HearingType extends TimeBound
{
	public UUID caseTypeId;
	public String name;
}

