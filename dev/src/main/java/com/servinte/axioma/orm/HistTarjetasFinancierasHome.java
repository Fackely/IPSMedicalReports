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
 * Home object for domain model class HistTarjetasFinancieras.
 * @see com.servinte.axioma.orm.HistTarjetasFinancieras
 * @author Hibernate Tools
 */
public class HistTarjetasFinancierasHome {

	private static final Log log = LogFactory
			.getLog(HistTarjetasFinancierasHome.class);

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

	public void persist(HistTarjetasFinancieras transientInstance) {
		log.debug("persisting HistTarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistTarjetasFinancieras instance) {
		log.debug("attaching dirty HistTarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistTarjetasFinancieras instance) {
		log.debug("attaching clean HistTarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistTarjetasFinancieras persistentInstance) {
		log.debug("deleting HistTarjetasFinancieras instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistTarjetasFinancieras merge(
			HistTarjetasFinancieras detachedInstance) {
		log.debug("merging HistTarjetasFinancieras instance");
		try {
			HistTarjetasFinancieras result = (HistTarjetasFinancieras) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistTarjetasFinancieras findById(long id) {
		log.debug("getting HistTarjetasFinancieras instance with id: " + id);
		try {
			HistTarjetasFinancieras instance = (HistTarjetasFinancieras) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistTarjetasFinancieras",
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

	public List findByExample(HistTarjetasFinancieras instance) {
		log.debug("finding HistTarjetasFinancieras instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistTarjetasFinancieras").add(
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
