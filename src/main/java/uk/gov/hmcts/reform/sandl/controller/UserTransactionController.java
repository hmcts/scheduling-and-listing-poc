package uk.gov.hmcts.reform.sandl.controller;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import uk.gov.hmcts.reform.sandl.model.transaction.Change;

/**
 * Creates and manages a UserTransaction to propagate and store the results of a set of fact changes.
 * A UserTransationEventController is used to dispatch events to suitable endpoints, and fact changes
 * resulting are added to the UserTransaction record.  Clients provide a callback to be invoked when
 * processing of the transaction is complete and the results can be broadcast.
 */
public class UserTransactionController
{
	private final UserTransactionEventController eventController;

	public UserTransactionController(UserTransactionEventController eventController)
	{
		this.eventController = eventController;
	}

	public void start(UserTransactionEvent startEvent, Consumer<UserTransaction> onComplete)
	{
		UserTransaction userTransaction = new UserTransaction(startEvent.getTransactionId());
		for (Change startChange : startEvent.getChanges())
		{
			userTransaction.addCause(startChange);
		}
		handleEvent(userTransaction, startEvent, eventController, new AtomicInteger(0), onComplete);
	}

	private static void handleEvent(UserTransaction userTransaction, UserTransactionEvent event, UserTransactionEventController eventController, AtomicInteger pendingEventCount, Consumer<UserTransaction> onComplete)
	{
		if (!event.getChanges().isEmpty())
		{
			pendingEventCount.incrementAndGet();
			eventController.handle(
					event,
					(input, output) ->
					{
						for (Change effect : output.getChanges())
						{
							userTransaction.addEffect(effect);
						}
						handleEvent(userTransaction, output, eventController, pendingEventCount, onComplete);
					},
					input ->
					{
						int count = pendingEventCount.decrementAndGet();
						if (count <= 0)
						{
							onComplete.accept(userTransaction);
						}
					});
		}
	}
}
