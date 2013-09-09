package org.aminakov.cassandra.dao;

import java.io.Serializable;
import java.util.List;

public interface AbstractDao<K extends Serializable, E extends Serializable> {

    public K persist(E entity);

    public List<K> persist(List<E> entityList);

    public void delete(E entity);

    public void delete(List<E> entityList);

    public E getById(K key);

    public List<E> getById(List<K> keys);

}
