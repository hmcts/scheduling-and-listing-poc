package uk.gov.hmcts.reform.sandl.controller;

import java.util.function.Predicate;

import lombok.AllArgsConstructor;
import uk.gov.hmcts.reform.sandl.model.transaction.Change;

@AllArgsConstructor
public class UserTransactionEventEndpointFilter implements UserTransactionEventEndpoint
{
	private final UserTransactionEventEndpoint endpoint;
	private final Predicate<Change> filter;

	@Override
	public UserTransactionEvent handle(UserTransactionEvent event)
	{
		UserTransactionEvent filtered = new UserTransactionEvent(event.getTransactionId());
		for (Change change : event.getChanges())
		{
			if (filter.test(change))
			{
				filtered.add(change);
			}
		}
		return filtered.getChanges().size() == 0 ? filtered : endpoint.handle(filtered);
	}

}
