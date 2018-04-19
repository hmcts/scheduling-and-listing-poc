package uk.gov.hmcts.reform.sandl.engine;


import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

import org.drools.core.ObjectFilter;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import uk.gov.hmcts.reform.sandl.model.common.Identified;
import uk.gov.hmcts.reform.sandl.model.transaction.Assert;
import uk.gov.hmcts.reform.sandl.model.transaction.Commit;
import uk.gov.hmcts.reform.sandl.model.transaction.Retract;
import uk.gov.hmcts.reform.sandl.model.transaction.Rollback;

public class RulesEngine// implements RuleRuntimeEventListener
{
	private KieSession ksession = getSession();

	private KieSession getSession()
	{
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kContainer = kieServices.getKieClasspathContainer();
		return kContainer.newKieSession();
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
}
 
