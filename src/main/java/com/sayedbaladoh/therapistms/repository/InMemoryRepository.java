package com.sayedbaladoh.therapistms.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryRepository<ID, T> implements Repository<ID, T> {

	private final Map<ID, T> map;

	public InMemoryRepository() {
		map = new ConcurrentHashMap<>();
	}

	@Override
	public T save(ID id, T entity) {
		map.put(id, entity);
		return entity;
	}

	@Override
	public Collection<T> findAll() {
		return map.values();
	}

	@Override
	public Optional<T> findById(ID id) {
		return Optional.ofNullable(map.get(id));
	}

	@Override
	public boolean existsById(ID id) {
		return map.containsKey(id);
	}

	@Override
	public long count() {
		return map.size();
	}

	@Override
	public void deleteById(ID id) {
		map.remove(id);
	}

	@Override
	public void deleteAll() {
		map.clear();
	}
}
