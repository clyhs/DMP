package org.jssoft.base.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public abstract class BaseDao<T, PK extends Serializable> implements Dao<T, PK> {

	protected Class<T> t;
	protected EntityManager em;

	public BaseDao(Class<T> t) {
		this.t = t;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	public T save(T t) {
		em.persist(t);
		em.flush();
		return t;
	}

	public T update(T t) {
		em.merge(t);
		em.flush();
		return t;
	}

	public void delete(T t) {
		T _t = this.em.merge(t);
		em.remove(_t);
		em.flush();
	}

	public T find(PK id) {
		return em.find(t, id);
	}

	public List<T> findAll() {
		try {
			return findByObject(t.newInstance());
		} catch (Exception e) {
			throw new RuntimeException("无法创建实例！");
		}
	}

	public List<T> findByObject(T t, String... fuzzy) {
		Query q = DaoQueryMaker.makeQuery(em, t, fuzzy);
		List<T> result = q.getResultList();
		return result;
	}
}
