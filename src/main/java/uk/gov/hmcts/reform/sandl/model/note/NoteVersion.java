package uk.gov.hmcts.reform.sandl.model.note;

import java.util.UUID;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import uk.gov.hmcts.reform.sandl.model.common.TimeBound;

@Entity
@EqualsAndHashCode(callSuper = true)
public class NoteVersion extends TimeBound
{
	public UUID noteId;
	public String text;
}
