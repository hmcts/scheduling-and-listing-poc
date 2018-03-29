package uk.gov.hmcts.reform.sandl.model.util;

import java.util.UUID;

public class UUIDUtil
{
	public static long seed = System.currentTimeMillis();

	public static synchronized UUID uuid()
	{
		return new UUID(seed, ++seed);
	}

	public static void main(String[] args) throws Exception
	{
		for (int i = 0; i < 1000; ++i)
		{
			System.out.println(uuid());
		}
	}
}
