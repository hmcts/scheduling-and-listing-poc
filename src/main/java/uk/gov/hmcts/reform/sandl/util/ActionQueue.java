package uk.gov.hmcts.reform.sandl.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * A simple queue of actions to be carried out on a single thread.
 * There are probably standard Java classes which do this better.
 */
public class ActionQueue
{
	private final BlockingQueue<Runnable> queue;
	private volatile boolean running = true;
	private boolean stopping = false;

	public ActionQueue(int capacity)
	{
		this.queue = new ArrayBlockingQueue<>(capacity);
	}

	public ActionQueue start()
	{
		new Thread(
				new Runnable()
				{
					public void run()
					{
						while (running)
						{
							try
							{
								queue.take().run();
							}
							catch (InterruptedException e)
							{
								// discard
							}
						}
					}
				}).start();
		return this;
	}

	public void put(Runnable action)
	{
		synchronized (this)
		{
			if (stopping)
			{
				throw new IllegalStateException(this.getClass().getName() + ": cannot put() to a stopped queue.");
			}
			while (true)
			{
				try
				{
					queue.put(action);
					break;
				}
				catch (InterruptedException e)
				{
					// discard
				}
			}
		}
	}

	public void stop()
	{
		synchronized (this)
		{
			stopping = true;
			while (true)
			{
				try
				{
					queue.put(() -> this.running = false);
					break;
				}
				catch (InterruptedException e)
				{
					//discard
				}
			}
		}
	}
}
