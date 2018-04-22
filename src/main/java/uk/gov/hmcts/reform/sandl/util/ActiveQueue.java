package uk.gov.hmcts.reform.sandl.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * Implements a FIFO queue with a thread which runs a consumer for the
 * queue.  Intended to avoid some of the synchronization problems which
 * occur trying to use a blocking queue in tandem with the flag-based
 * pattern for stopping a thread.
 */
public class ActiveQueue<T>
{
	private final BlockingQueue<Entry<T>> queue;
//	private final Consumer<T> consumer;
	private boolean running = true;

	public ActiveQueue(int capacity)
	{
		this.queue = new ArrayBlockingQueue<>(capacity);
//		this.consumer = consumer;
	}

	public void start()
	{
		new Thread(
				new Runnable()
				{
					public void run()
					{
						boolean running = true;
						while (running)
						{
							try
							{
								Entry<T> entry = queue.take();
								if (entry.end)
								{
									running = false;
								}
								else
								{
									entry.consumer.accept(entry.value);
								}
							}
							catch (InterruptedException e)
							{
								// discard
							}
						}
					}
				}).start();
	}

	public void put(T t, Consumer<T> consumer) throws InterruptedException
	{
		synchronized (this)
		{
			if (!running)
			{
				throw new IllegalStateException("Cannot put() to a stopped queue.");
			}
			queue.put(new Entry<>(t, consumer));
		}
	}

	public void stop() throws InterruptedException
	{
		synchronized (this)
		{
			running = false;
			queue.put(new Entry<>());
		}
	}

	private static class Entry<T>
	{
		public final boolean end;
		public final T value;
		public final Consumer<T> consumer;

		private Entry(T value, Consumer<T> consumer)
		{
			this.end = false;
			this.value = value;
			this.consumer = consumer;
		}

		private Entry()
		{
			this.end = true;
			this.value = null;
			this.consumer = null;
		}
	}
}
