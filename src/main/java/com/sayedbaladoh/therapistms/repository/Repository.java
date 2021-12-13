package com.sayedbaladoh.therapistms.repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Interface for generic CRUD operations on a repository for a specific type.
 * 
 * @author Sayed Baladoh
 *
 * @param <ID> the type of the id of the entity
 * @param <T>  the entity type
 */
public interface Repository<ID, T> {

	/**
	 * Saves a given entity.
	 * 
	 * @param id     the id of the entity.
	 * @param entity the entity.
	 * @return the saved entity;
	 */
	T save(ID id, T entity);

	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	Collection<T> findAll();

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id the id of the entity.
	 * @return the Optional#entity with the given id or {@literal Optional#empty()}
	 *         if none found.
	 */
	Optional<T> findById(ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id the id of the entity.
	 * @return {@literal true} if an entity with the given id exists,
	 *         {@literal false} otherwise.
	 */
	boolean existsById(ID id);

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities.
	 */
	long count();

	/**
	 * Deletes the entity with the given id.
	 *
	 * @param id the id of the entity.
	 */
	void deleteById(ID id);

	/**
	 * Deletes all entities.
	 */
	void deleteAll();
}
