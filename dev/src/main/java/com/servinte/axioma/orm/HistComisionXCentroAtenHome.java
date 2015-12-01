package com.servinte.axioma.orm;

// Generated Nov 23, 2010 10:55:28 AM by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistComisionXCentroAten.
 * @see com.servinte.axioma.orm.HistComisionXCentroAten
 * @author Hibernate Tools
 */
public class HistComisionXCentroAtenHome {

	private static final Log log = LogFactory
			.getLog(HistComisionXCentroAtenHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext()
					.lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(HistComisionXCentroAten transientInstance) {
		log.debug("persisting HistComisionXCentroAten instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistComisionXCentroAten instance) {
		log.debug("attaching dirty HistComisionXCentroAten instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistComisionXCentroAten instance) {
		log.debug("attaching clean HistComisionXCentroAten instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistComisionXCentroAten persistentInstance) {
		log.debug("deleting HistComisionXCentroAten instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistComisionXCentroAten merge(
			HistComisionXCentroAten detachedInstance) {
		log.debug("merging HistComisionXCentroAten instance");
		try {
			HistComisionXCentroAten result = (HistComisionXCentroAten) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistComisionXCentroAten findById(long id) {
		log.debug("getting HistComisionXCentroAten instance with id: " + id);
		try {
			HistComisionXCentroAten instance = (HistComisionXCentroAten) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistComisionXCentroAten",
							id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(HistComisionXCentroAten instance) {
		log.debug("finding HistComisionXCentroAten instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistComisionXCentroAten").add(
					Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
