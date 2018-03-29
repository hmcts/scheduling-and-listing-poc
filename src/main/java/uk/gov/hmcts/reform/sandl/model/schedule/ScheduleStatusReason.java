package uk.gov.hmcts.reform.sandl.model.schedule;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class ScheduleStatusReason extends Identified
{
}
