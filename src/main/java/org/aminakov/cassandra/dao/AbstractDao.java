package org.aminakov.cassandra.dao;

/**
 * @author Oleksandr Minakov
 * date: 8/4/13
 */

import java.io.Serializable;
import java.util.List;

/**
 * A generic dao used to interact with Cassandra
 *
 * @param <K> the type of the key used to save entities to Cassandra
 * @param <E> the entity type
 */
public interface AbstractDao<K extends Serializable, E extends Serializable> {

    /**
     * Persists or updates entity in Cassandra
     *
     * @param entity - entity to persist/update
     * @return <K> a key which was generated/used to save entity
     */
    public K persist(E entity);

    /**
     * Persists or updates list of entities in Cassandra
     *
     * @param entityList - a list of entities to persist/update
     * @return list of <K>  a list of keys which were generated/used to save entities
     */
    public List<K> persist(List<E> entityList);

    /**
     * Deletes entity from Cassandra
     *
     * @param entity - entity to delete
     */
    public void delete(E entity);

    /**
     * Deletes entities from Cassandra
     *
     * @param entityList - a list of entities to delete
     */
    public void delete(List<E> entityList);

    /**
     * Returns entity specified by the key {@code id}. If none found returns null
     *
     * @param key - a key to search entity by
     * @return <E> entity
     */
    public E getById(K key);

    /**
     * Returns list of entities specified by the list of keys If none found returns null
     *
     * @param keys - a list of keys to search entities by
     * @return list of <E> is list of entities
     */
    public List<E> getById(List<K> keys);
}
