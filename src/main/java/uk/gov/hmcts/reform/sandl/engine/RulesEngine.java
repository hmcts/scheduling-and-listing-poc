package uk.gov.hmcts.reform.sandl.engine;


import java.util.Collection;
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
import org.kie.api.runtime.rule.FactHandle;

import uk.gov.hmcts.reform.sandl.model.common.Identified;

public class RulesEngine implements RuleRuntimeEventListener
{
	public KieSession ksession;
	private final FactIndex factIndex = new FactIndex();

	private KieSession getSession()
	{
		if (ksession == null)
		{
			KieServices kieServices = KieServices.Factory.get();
			KieContainer kContainer = kieServices.getKieClasspathContainer();
			ksession = kContainer.newKieSession();
			ksession.addEventListener(this);
		}
		return ksession;
	}

	public void processTransaction(Collection<Identified> facts)
	{
		for (Identified fact : facts)
		{
			assertFact(fact);
		}
		fireAllRules();
	}

	public void insert(Object object)
	{
		getSession().insert(object);
	}

	public void assertFact(Identified fact)
	{
		FactHandle handle = factIndex.getFactHandle(fact.id);
		if (handle != null)
		{
			getSession().update(handle, fact);
		}
		else
		{
			getSession().insert(fact);
		}
	}

	public void retractFact(Identified fact)
	{
		retractFact(fact.id);
	}

	public void retractFact(UUID factId)
	{
		FactHandle handle = factIndex.getFactHandle(factId);
		if (handle != null)
		{
			getSession().delete(handle);
		}
	}

	public void fireAllRules()
	{
		getSession().fireAllRules();
	}

	public <T extends Identified> Collection<T> getFacts(Class<T> clazz, Predicate<T> filter)
	{
		return factIndex.get(clazz, filter);
	}

	public Collection<? extends Object> getFactsx(Predicate<Object> filter)
	{
		return ksession.getObjects(
				new ObjectFilter()
				{
					public boolean accept(Object o)
					{
						return filter.test(o);
					}
				});
	}

	@Override
	public void objectInserted(ObjectInsertedEvent event)
	{
		// Clone of update
		Object object = event.getObject();
		if (object instanceof Identified)
		{
			Identified fact = (Identified)object;
			factIndex.add(fact, event.getFactHandle());
		}
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent event)
	{
		// Clone of insert
		Object object = event.getObject();
		if (object instanceof Identified)
		{
			Identified fact = (Identified)object;
			factIndex.add(fact, event.getFactHandle());
		}
	}

	@Override
	public void objectDeleted(ObjectDeletedEvent event)
	{
		Object object = event.getOldObject();
		if (object instanceof Identified)
		{
			Identified fact = (Identified)object;
			factIndex.remove(fact.id);
		}
	}
}
 
