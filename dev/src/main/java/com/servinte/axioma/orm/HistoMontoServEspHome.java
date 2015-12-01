package com.servinte.axioma.orm;

// Generated Sep 3, 2010 2:49:57 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistoMontoServEsp.
 * @see com.servinte.axioma.orm.HistoMontoServEsp
 * @author Hibernate Tools
 */
public class HistoMontoServEspHome {

	private static final Log log = LogFactory
			.getLog(HistoMontoServEspHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(HistoMontoServEsp transientInstance) {
		log.debug("persisting HistoMontoServEsp instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistoMontoServEsp instance) {
		log.debug("attaching dirty HistoMontoServEsp instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistoMontoServEsp instance) {
		log.debug("attaching clean HistoMontoServEsp instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistoMontoServEsp persistentInstance) {
		log.debug("deleting HistoMontoServEsp instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistoMontoServEsp merge(HistoMontoServEsp detachedInstance) {
		log.debug("merging HistoMontoServEsp instance");
		try {
			HistoMontoServEsp result = (HistoMontoServEsp) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistoMontoServEsp findById(int id) {
		log.debug("getting HistoMontoServEsp instance with id: " + id);
		try {
			HistoMontoServEsp instance = (HistoMontoServEsp) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistoMontoServEsp", id);
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

	public List findByExample(HistoMontoServEsp instance) {
		log.debug("finding HistoMontoServEsp instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistoMontoServEsp").add(
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
