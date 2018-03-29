package uk.gov.hmcts.reform.sandl.model.session;

import java.time.Duration;
import java.time.LocalDateTime;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Session extends Identified
{
	public LocalDateTime begin;
	public Duration duration;
	public LocalDateTime getEnd()
	{
		return begin.plus(duration);
	}
}

