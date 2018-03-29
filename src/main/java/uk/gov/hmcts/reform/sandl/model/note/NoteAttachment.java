package uk.gov.hmcts.reform.sandl.model.note;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public class NoteAttachment extends TimeBound
{
	public UUID noteId;
	public UUID targetId;
}
