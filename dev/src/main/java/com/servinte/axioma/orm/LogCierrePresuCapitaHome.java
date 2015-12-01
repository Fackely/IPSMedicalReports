package com.servinte.axioma.orm;

// Generated 16/05/2011 12:25:36 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogCierrePresuCapita.
 * @see com.servinte.axioma.orm.LogCierrePresuCapita
 * @author Hibernate Tools
 */
public class LogCierrePresuCapitaHome {

	private static final Log log = LogFactory
			.getLog(LogCierrePresuCapitaHome.class);

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

	public void persist(LogCierrePresuCapita transientInstance) {
		log.debug("persisting LogCierrePresuCapita instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogCierrePresuCapita instance) {
		log.debug("attaching dirty LogCierrePresuCapita instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogCierrePresuCapita instance) {
		log.debug("attaching clean LogCierrePresuCapita instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogCierrePresuCapita persistentInstance) {
		log.debug("deleting LogCierrePresuCapita instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogCierrePresuCapita merge(LogCierrePresuCapita detachedInstance) {
		log.debug("merging LogCierrePresuCapita instance");
		try {
			LogCierrePresuCapita result = (LogCierrePresuCapita) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogCierrePresuCapita findById(long id) {
		log.debug("getting LogCierrePresuCapita instance with id: " + id);
		try {
			LogCierrePresuCapita instance = (LogCierrePresuCapita) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogCierrePresuCapita", id);
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

	public List findByExample(LogCierrePresuCapita instance) {
		log.debug("finding LogCierrePresuCapita instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.LogCierrePresuCapita")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
