package uk.gov.hmcts.reform.sandl.model.schedule;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public abstract class ScheduleStatus extends TimeBound
{
	public UUID subjectId;
	public final String status;

	protected ScheduleStatus(String status)
	{
		this.status = status;
	}
}

