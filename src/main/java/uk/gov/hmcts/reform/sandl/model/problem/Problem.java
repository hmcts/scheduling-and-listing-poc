package uk.gov.hmcts.reform.sandl.model.problem;

import java.text.MessageFormat;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.Identified;

@Entity
@EqualsAndHashCode(callSuper = true)
public class Problem extends Identified
{
	public String description;
	//public UUID[] references;
	public Object[] references;

	public Problem(String description, Object ... references)
	{
		this.description = description;
		this.references = references;
	}

	public String toString()
	{
		return new MessageFormat(description).format(references);
	}
}
