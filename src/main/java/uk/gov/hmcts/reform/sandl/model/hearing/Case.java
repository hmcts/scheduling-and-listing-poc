package uk.gov.hmcts.reform.sandl.model.hearing;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Case extends Identified
{
	public UUID caseTypeId;
	public String title;
}
