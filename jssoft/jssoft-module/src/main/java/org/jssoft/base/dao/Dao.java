package org.jssoft.base.dao;

import java.io.Serializable;
import java.util.List;

public interface Dao<T, PK extends Serializable> {

	public T save(final T t);

    public T update(final T t);

    public void delete(final T t);

    public T find(final PK id);
    
    public List<T> findAll();
    
    public List<T> findByObject(T t, String... fuzzy);
}
