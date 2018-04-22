package uk.gov.hmcts.reform.sandl.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import uk.gov.hmcts.reform.sandl.util.ActionQueue;

public class UserTransactionEventController
{
	private boolean stopping = false;
	private final int queueCapacity = 10000;
	private Map<UserTransactionEventEndpoint, ActionQueue> endpointQueues = new HashMap<>();

	public void addEndpoint(UserTransactionEventEndpoint endpoint)
	{
		synchronized (this)
		{
			if (!endpointQueues.containsKey(endpoint))
			{
				Map<UserTransactionEventEndpoint, ActionQueue> newEndpointQueues = new HashMap<>(endpointQueues);
				newEndpointQueues.put(endpoint, new ActionQueue(queueCapacity).start());
				endpointQueues = newEndpointQueues;
			}
		}
	}

	public void handle(UserTransactionEvent event, BiConsumer<UserTransactionEvent, UserTransactionEvent> forEach, Consumer<UserTransactionEvent> onComplete)
	{
		System.err.println("################# Handling Event : " + event);
		if (stopping)
		{
			throw new IllegalStateException(this.getClass().getName() + ": cannot handle() events when stopped.");
		}
		final AtomicInteger actionCount = new AtomicInteger(0);
		for (Map.Entry<UserTransactionEventEndpoint, ActionQueue> entry : endpointQueues.entrySet())
		{
			actionCount.incrementAndGet();
			UserTransactionEventEndpoint endpoint = entry.getKey();
			ActionQueue queue = entry.getValue();
			queue.put(
					new Runnable()
					{
						public void run()
						{
							UserTransactionEvent result = endpoint.handle(event);
							forEach.accept(event, result);
							int count = actionCount.decrementAndGet();
							if (count <= 0)
							{
								onComplete.accept(event);
							}
						}
					});
		}
	}

	public void stop()
	{
		stopping = true;
		for (ActionQueue queue : endpointQueues.values())
		{
			queue.stop();
		}
	}
}
