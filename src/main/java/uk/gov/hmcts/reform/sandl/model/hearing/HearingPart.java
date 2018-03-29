package uk.gov.hmcts.reform.sandl.model.hearing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class HearingPart extends Identified
{
	public UUID hearingId;
	public LocalDateTime dateTime;
	public Duration duration;
}

