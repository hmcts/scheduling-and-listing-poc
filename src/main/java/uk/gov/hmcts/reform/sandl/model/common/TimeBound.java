package uk.gov.hmcts.reform.sandl.model.common;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class TimeBound extends Identified
{
	public LocalDateTime begin = LocalDateTime.MIN;
	public LocalDateTime end = LocalDateTime.MAX;
}
