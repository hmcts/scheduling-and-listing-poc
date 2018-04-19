package uk.gov.hmcts.reform.sandl.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.kie.api.runtime.rule.FactHandle;

import uk.gov.hmcts.reform.sandl.model.common.Identified;

/**
 * @deprecated
 * This is a purely interim class.  Lookup/query of facts should be implemented be either
 * (a) a proper persistence layer; (b) backward-chaining queries in the rules engine.
 * 
 * This class provides a wrapper which enables the rules engine to be queried to retrieve
 * facts from working memory.
 * @author jon
 */
@Deprecated
public class FactIndex
{
	private final Map<Class<?>, Map<UUID, Identified>> indexesByClass = new HashMap<>();
	private final Map<UUID, Identified> factsById = new HashMap<>();
	private final Map<UUID, FactHandle> factHandlesById = new HashMap<>();

	private Map<UUID, Identified> getIndex(Class<?> clazz)
	{
		Map<UUID, Identified> index = (Map<UUID, Identified>) indexesByClass.get(clazz);
		if (index == null)
		{
			index = new HashMap<>();
			indexesByClass.put(clazz, index);
		}
		return index;
	}

	private void add(Identified fact, Class<?> clazz)
	{
		getIndex(clazz).put(fact.id, fact);
		Class<?> superClass = clazz.getSuperclass();
		if (Identified.class.isAssignableFrom(superClass))
		{
			add(fact, superClass);
		}
	}

	public <T extends Identified> void add(T fact, FactHandle factHandle)
	{
		add(fact, fact.getClass());
		factsById.put(fact.id, fact);
		factHandlesById.put(fact.id, factHandle);
	}

	private void remove(UUID factId, Class<?> clazz)
	{
		getIndex(clazz).remove(factId);
		Class<?> superClass = clazz.getSuperclass();
		if (Identified.class.isAssignableFrom(superClass))
		{
			remove(factId, superClass);
		}
	}

	public void remove(UUID factId)
	{
		Identified fact = factsById.get(factId);
		if (fact != null)
		{
			remove(factId, fact.getClass());
			factsById.remove(factId);
			factHandlesById.remove(factId);
		}
	}

	public <T extends Identified> T get(UUID factId)
	{
		@SuppressWarnings("unchecked")
		T fact = (T)factsById.get(factId);
		return fact;
	}

	public <T extends Identified> Collection<T> get(Class<T> clazz, Predicate<T> filter)
	{
		@SuppressWarnings("unchecked")
		Collection<T> facts = ((Collection<T>)indexesByClass.get(clazz).values());
		return facts.stream().filter(filter).collect(Collectors.toList());
	}

	public FactHandle getFactHandle(UUID factId)
	{
		return factHandlesById.get(factId);
	}
}
