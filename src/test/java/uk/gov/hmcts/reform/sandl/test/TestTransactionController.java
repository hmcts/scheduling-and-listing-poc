package uk.gov.hmcts.reform.sandl.test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.ToString;
import uk.gov.hmcts.reform.sandl.controller.UserTransactionController;
import uk.gov.hmcts.reform.sandl.controller.UserTransactionEvent;
import uk.gov.hmcts.reform.sandl.controller.UserTransactionEventController;
import uk.gov.hmcts.reform.sandl.controller.UserTransactionEventEndpoint;
import uk.gov.hmcts.reform.sandl.controller.UserTransactionEventEndpointFilter;
import uk.gov.hmcts.reform.sandl.engine.RulesEngine;
import uk.gov.hmcts.reform.sandl.model.common.Identified;
import uk.gov.hmcts.reform.sandl.model.transaction.Assert;
import uk.gov.hmcts.reform.sandl.model.transaction.Change;
import uk.gov.hmcts.reform.sandl.model.transaction.Commit;
import uk.gov.hmcts.reform.sandl.model.transaction.Retract;
import uk.gov.hmcts.reform.sandl.model.transaction.Rollback;
import uk.gov.hmcts.reform.sandl.model.util.UUIDUtil;

public class TestTransactionController
{
	@ToString
	public static class A extends Identified
	{
		public int value;
		
		public A(UUID id, int value)
		{
			this.id = id;
			this.value = value;
		}
	}

	@ToString
	public static class B extends Identified
	{
		public int value;
		
		public B(UUID id, int value)
		{
			this.id = id;
			this.value = value;
		}
	}

	private final Set<RulesEngine> engines = new HashSet<>();

	public static void main(String[] args) throws Exception
	{
		new TestTransactionController().run();
	}

	public void run() throws InterruptedException
	{
//		testRulesTransaction(true);
		testRulesTransaction(false);
	}

	public void testRulesTransaction(boolean commit) throws InterruptedException
	{
		System.out.println("=== Starting testRulesTransaction(commit = " + commit + ") ==============================================================");
		UserTransactionController controller = buildController();
		UUID transactionId = UUIDUtil.uuid();
		Assert cause = new Assert(transactionId, new A(UUIDUtil.uuid(), 0));
		UserTransactionEvent startEvent = new UserTransactionEvent(transactionId);
		startEvent.add(cause);
		controller.start(
				startEvent,
				e -> {
					System.out.println("=== Completed ==============================================================");
					for (Change change : e.getCauses())
					{
						System.out.println(change);
					}
					System.out.println("============================================================================");
					System.out.println("=== Engines ================================================================");
					for (RulesEngine engine : engines)
					{
						showWorkingMemory("Engine WM", engine);
					}
					System.out.println("============================================================================");
					UserTransactionEvent commitEvent = new UserTransactionEvent(transactionId, commit ? new Commit(transactionId) : new Rollback(transactionId));
					controller.start(
							commitEvent,
							ev -> {
								System.out.println("=== Completed testRulesTransaction(commit = " + commit + ") ==============================================================");
								for (Change change : ev.getCauses())
								{
									System.out.println(change);
								}
								System.out.println("============================================================================");
								System.out.println("=== Engines ================================================================");
								for (RulesEngine engine : engines)
								{
									showWorkingMemory("Engine WM", engine);
								}
								System.out.println("============================================================================");
								try
								{
									Thread.sleep(3000); // TODO: synchronize properly
								}
								catch (InterruptedException ex)
								{
									//discard
								}
								controller.stop();
							});
				});
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

	public UserTransactionController buildController()
	{
		UserTransactionController controller = new UserTransactionController(buildEventController());
		return controller;
	}

	public UserTransactionEventController buildEventController()
	{
		UserTransactionEventController controller = new UserTransactionEventController();
		controller.addEndpoint(buildEndpointA());
		controller.addEndpoint(buildEndpointB());
		return controller;
	}

	public UserTransactionEventEndpoint buildEndpointA()
	{
		RulesEngine engine = new RulesEngine("A");
		engines.add(engine);
		UserTransactionEventEndpointFilter filter = new UserTransactionEventEndpointFilter(
			engine, c -> c instanceof Retract || (c instanceof Assert && ((Assert)c).getFact() instanceof A));
		return filter;
	}

	public UserTransactionEventEndpoint buildEndpointB()
	{
		RulesEngine engine = new RulesEngine("B");
		engines.add(engine);
		UserTransactionEventEndpointFilter filter = new UserTransactionEventEndpointFilter(
			engine, c -> c instanceof Retract || (c instanceof Assert && ((Assert)c).getFact() instanceof B));
		return filter;
	}
}
