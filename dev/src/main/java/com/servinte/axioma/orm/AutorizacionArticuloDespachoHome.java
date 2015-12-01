package com.servinte.axioma.orm;

// Generated Nov 24, 2010 2:03:24 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AutorizacionArticuloDespacho.
 * @see com.servinte.axioma.orm.AutorizacionArticuloDespacho
 * @author Hibernate Tools
 */
public class AutorizacionArticuloDespachoHome {

	private static final Log log = LogFactory
			.getLog(AutorizacionArticuloDespachoHome.class);

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

	public void persist(AutorizacionArticuloDespacho transientInstance) {
		log.debug("persisting AutorizacionArticuloDespacho instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutorizacionArticuloDespacho instance) {
		log.debug("attaching dirty AutorizacionArticuloDespacho instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutorizacionArticuloDespacho instance) {
		log.debug("attaching clean AutorizacionArticuloDespacho instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutorizacionArticuloDespacho persistentInstance) {
		log.debug("deleting AutorizacionArticuloDespacho instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutorizacionArticuloDespacho merge(
			AutorizacionArticuloDespacho detachedInstance) {
		log.debug("merging AutorizacionArticuloDespacho instance");
		try {
			AutorizacionArticuloDespacho result = (AutorizacionArticuloDespacho) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutorizacionArticuloDespacho findById(long id) {
		log.debug("getting AutorizacionArticuloDespacho instance with id: "
				+ id);
		try {
			AutorizacionArticuloDespacho instance = (AutorizacionArticuloDespacho) sessionFactory
					.getCurrentSession()
					.get(
							"com.servinte.axioma.orm.AutorizacionArticuloDespacho",
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

	public List findByExample(AutorizacionArticuloDespacho instance) {
		log.debug("finding AutorizacionArticuloDespacho instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AutorizacionArticuloDespacho")
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
