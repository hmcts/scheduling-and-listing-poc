package uk.gov.hmcts.reform.sandl.engine;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import org.drools.core.ObjectFilter;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import uk.gov.hmcts.reform.sandl.model.common.Identified;

public class RulesEngine
{
	public KieSession ksession;
	public Map<UUID, FactHandle> factHandles = new HashMap<>();
	public Map<UUID, Identified> facts = new HashMap<>();

	private KieSession getSession()
	{
		if (ksession == null)
		{
			KieServices kieServices = KieServices.Factory.get();
			KieContainer kContainer = kieServices.getKieClasspathContainer();
			ksession = kContainer.newKieSession();
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

	public void assertFact(Identified fact)
	{
		FactHandle handle = factHandles.get(fact.id);
		if (handle != null)
		{
			getSession().update(handle, fact);
		}
		else
		{
			factHandles.put(fact.id, getSession().insert(fact));
		}
		facts.put(fact.id, fact);
	}

	public void retractFact(Identified fact)
	{
		FactHandle handle = factHandles.remove(fact.id);
		if (handle != null)
		{
			getSession().delete(handle);
		}
		facts.remove(fact.id);
	}

	public void fireAllRules()
	{
		getSession().fireAllRules();
	}

	public <T extends Identified> T getFact(UUID id)
	{
		@SuppressWarnings("unchecked")
		T t = (T)facts.get(id);
		return t;
	}

	public <T extends Identified> Collection<T> getEngineFacts(Class<T> clazz, Predicate<T> filter)
	{
		@SuppressWarnings("unchecked")
		Collection<T> foundFacts =
			(Collection<T>)ksession.getObjects(
				new ObjectFilter()
				{
					public boolean accept(Object x)
					{
						return clazz.isAssignableFrom(x.getClass()) && filter.test((T)x);
					}
				});
		return foundFacts;
	}

	public <T extends Identified> Collection<T> getStatedFacts(Class<T> clazz, Predicate<T> filter)
	{
		List<T> foundFacts = new ArrayList<>();
		for (Identified fact : facts.values())
		{
			if (clazz.isAssignableFrom(fact.getClass()))
			{
				@SuppressWarnings("unchecked")
				T t = (T)fact;
				if (filter.test(t))
				{
					foundFacts.add(t);
				}
			}
		}
		return foundFacts;
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
}
 
