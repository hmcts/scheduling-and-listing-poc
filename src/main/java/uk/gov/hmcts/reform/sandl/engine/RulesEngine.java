package uk.gov.hmcts.reform.sandl.engine;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import org.drools.core.ObjectFilter;
import org.kie.api.KieServices;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import lombok.Getter;
import uk.gov.hmcts.reform.sandl.controller.UserTransactionEvent;
import uk.gov.hmcts.reform.sandl.controller.UserTransactionEventEndpoint;
import uk.gov.hmcts.reform.sandl.model.common.Identified;
import uk.gov.hmcts.reform.sandl.model.transaction.Assert;
import uk.gov.hmcts.reform.sandl.model.transaction.Change;
import uk.gov.hmcts.reform.sandl.model.transaction.Commit;
import uk.gov.hmcts.reform.sandl.model.transaction.Retract;
import uk.gov.hmcts.reform.sandl.model.transaction.Rollback;

public class RulesEngine implements UserTransactionEventEndpoint// implements RuleRuntimeEventListener
{
	private final KieSession ksession;
	private final ChangeListener changeListener;

	public RulesEngine(String sessionName)
	{
		changeListener = new ChangeListener();
		ksession = getSession(sessionName);
		ksession.addEventListener(changeListener);
	}

	@Override
	public UserTransactionEvent handle(UserTransactionEvent event)
	{
		synchronized (this)
		{
			UUID transactionId = event.getTransactionId();
			changeListener.init(event);
			for (Change change : event.getChanges())
			{
				ksession.insert(change);
			}
			ksession.fireAllRules();
			UserTransactionEvent result = new UserTransactionEvent(transactionId);
			for (Change change : changeListener.getChanges())
			{
				result.add(change);
			}
			changeListener.clear();
			return result;
		}
	}

	private KieSession getSession(String sessionName)
	{
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kContainer = kieServices.getKieClasspathContainer();
		return kContainer.newKieSession(sessionName);
	}

	public void commit(UUID transactionId)
	{
		ksession.insert(new Commit(transactionId));
	}

	public void rollback(UUID transactionId)
	{
		ksession.insert(new Rollback(transactionId));
	}

	public void assertFacts(UUID transactionId, Identified ... facts)
	{
		for (Identified fact : facts)
		{
			ksession.insert(new Assert(transactionId, fact));
		}
	}

	public void retractFacts(UUID transactionId, UUID ... factIds)
	{
		for (UUID factId : factIds)
		{
			ksession.insert(new Retract(transactionId, factId));
		}
	}

	public void retractFacts(UUID transactionId, Identified ... facts)
	{
		for (Identified fact : facts)
		{
			ksession.insert(new Retract(transactionId, fact.id));
		}
	}

	public void fireAllRules()
	{
		ksession.fireAllRules();
	}

	public Collection<? extends Object> getWorkingMemory()
	{
		return ksession.getObjects();
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public <T extends Identified> Collection<T> getFacts(Class<T> clazz, Predicate<T> filter)
	{
		return
			(Collection<T>)ksession.getObjects(new ObjectFilter()
				{
					@Override
					public boolean accept(Object object)
					{
						return clazz.isAssignableFrom(object.getClass()) && filter.test((T)object);
					}
				});
	}

	public static class ChangeListener implements RuleRuntimeEventListener
	{
		private UUID currentTransactionId;
		@Getter
		private final Set<Change> changes = new HashSet<>();
		private final Set<UUID> ignoreRetract = new HashSet<>();
		private final Set<Identified> ignoreAssert = new HashSet<>();

		public void init(UserTransactionEvent event)
		{
			clear();
			this.currentTransactionId = event.getTransactionId();
			for (Change change : event.getChanges())
			{
				if (change instanceof Assert)
				{
					ignoreAssert.add(((Assert)change).getFact());
				}
				else if (change instanceof Retract)
				{
					ignoreRetract.add(((Retract)change).getFactId());
				}
			}
		}

		public void clear()
		{
			changes.clear();
			ignoreAssert.clear();
			ignoreRetract.clear();
		}

		@Override
		public void objectDeleted(ObjectDeletedEvent event)
		{
			Object deleted = event.getOldObject();
			if (deleted instanceof Identified)
			{
				UUID factId = ((Identified)deleted).id;
				if (!ignoreRetract.contains(factId))
				{
					changes.add(new Retract(currentTransactionId, factId));
				}
			}
		}

		@Override
		public void objectInserted(ObjectInsertedEvent event)
		{
			objectUpserted(event.getObject());
		}

		@Override
		public void objectUpdated(ObjectUpdatedEvent event)
		{
			objectUpserted(event.getObject());
		}

		public void objectUpserted(Object object)
		{
			if (object instanceof Identified)
			{
				Identified fact = (Identified)object;
				if (!ignoreAssert.contains(fact))
				{
					changes.add(new Assert(currentTransactionId, fact));
				}
			}
		}
	}
}
 
