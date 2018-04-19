package uk.gov.hmcts.reform.sandl.test;

import static uk.gov.hmcts.reform.sandl.model.util.UUIDUtil.uuid;

import java.util.UUID;

import uk.gov.hmcts.reform.sandl.engine.RulesEngine;
import uk.gov.hmcts.reform.sandl.model.common.Identified;
import uk.gov.hmcts.reform.sandl.model.util.UUIDUtil;

public class TestTransactions
{
	public static class A extends Identified
	{
		public int a;
		
		public A(UUID id, int a)
		{
			this.id = id;
			this.a = a;
		}

		public String toString()
		{
			return "A ( " + this.id + " , " + this.a + " )";
		}
	}

	public static void main(String[] args) throws Exception
	{
		new TestTransactions().run();
	}

	public void run()
	{
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("testAssertWithCommit");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		testAssertWithCommit();
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("testAssertWithRollback");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		testAssertWithRollback();
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("testUpsertWithCommit");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		testUpsertWithCommit();
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("testUpsertWithRollback");
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		testUpsertWithRollback();
	}

	public boolean testAssertWithCommit()
	{
		RulesEngine engine = new RulesEngine();
		UUID transactionId = uuid();
		A a = new A(UUIDUtil.uuid(), 1);
		engine.assertFacts(transactionId, a);
		engine.fireAllRules();
		showWorkingMemory("A + Asserted", engine);
		engine.commit(transactionId);
		engine.fireAllRules();
		showWorkingMemory("A", engine);		
		return true;
	}

	public boolean testAssertWithRollback()
	{
		RulesEngine engine = new RulesEngine();
		UUID transactionId = uuid();
		A a = new A(uuid(), 1);
		engine.assertFacts(transactionId, a);
		engine.fireAllRules();
		showWorkingMemory("A + Asserted", engine);
		engine.rollback(transactionId);
		engine.fireAllRules();
		showWorkingMemory("<empty>", engine);		
		return true;
	}

	public boolean testUpsertWithCommit()
	{
		RulesEngine engine = new RulesEngine();
		UUID transactionId = uuid();
		A a1 = new A(uuid(), 1);
		engine.assertFacts(transactionId, a1);
		engine.fireAllRules();
		engine.commit(transactionId);
		showWorkingMemory("A(1) + Asserted(A(1)) + Commit", engine);
		engine.fireAllRules();
		showWorkingMemory("A(1)", engine);

		transactionId = uuid();
		A a2 = new A(a1.id, 3);
		engine.assertFacts(transactionId, a2);
		showWorkingMemory("A(1) + Assert(A(3))", engine);
		engine.fireAllRules();
		showWorkingMemory("A(3) + Asserted(A(3)) + Retracted(A(1))", engine);
		engine.commit(transactionId);
		engine.fireAllRules();
		showWorkingMemory("A(3)", engine);		
		return true;
	}

	public boolean testUpsertWithRollback()
	{
		RulesEngine engine = new RulesEngine();
		UUID transactionId = uuid();
		A a1 = new A(uuid(), 1);
		engine.assertFacts(transactionId, a1);
		engine.fireAllRules();
		engine.commit(transactionId);
		showWorkingMemory("A(1) + Asserted(A(1)) + Commit", engine);
		engine.fireAllRules();
		showWorkingMemory("A(1)", engine);

		transactionId = uuid();
		A a2 = new A(a1.id, 3);
		engine.assertFacts(transactionId, a2);
		showWorkingMemory("A(1) + Assert(A(3))", engine);
		engine.fireAllRules();
		showWorkingMemory("A(3) + Asserted(A(3)) + Retracted(A(1))", engine);
		engine.rollback(transactionId);
		engine.fireAllRules();
		showWorkingMemory("A(1)", engine);		
		return true;
	}

	public void showWorkingMemory(String message, RulesEngine engine)
	{
		System.out.println("===========================================================================");
		System.out.println("TARGET : " + message);
		System.out.println("===========================================================================");
		for (Object o : engine.getWorkingMemory())
		{
			System.out.println(o);
		}
		System.out.println("===========================================================================");
	}
}
