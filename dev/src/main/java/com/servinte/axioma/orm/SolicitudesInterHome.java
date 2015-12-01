package com.servinte.axioma.orm;

// Generated 23/06/2012 05:39:07 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class SolicitudesInter.
 * @see com.servinte.axioma.orm.SolicitudesInter
 * @author Hibernate Tools
 */
public class SolicitudesInterHome {

	private static final Log log = LogFactory
			.getLog(SolicitudesInterHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

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

	public void persist(SolicitudesInter transientInstance) {
		log.debug("persisting SolicitudesInter instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SolicitudesInter instance) {
		log.debug("attaching dirty SolicitudesInter instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SolicitudesInter instance) {
		log.debug("attaching clean SolicitudesInter instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SolicitudesInter persistentInstance) {
		log.debug("deleting SolicitudesInter instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SolicitudesInter merge(SolicitudesInter detachedInstance) {
		log.debug("merging SolicitudesInter instance");
		try {
			SolicitudesInter result = (SolicitudesInter) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SolicitudesInter findById(int id) {
		log.debug("getting SolicitudesInter instance with id: " + id);
		try {
			SolicitudesInter instance = (SolicitudesInter) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SolicitudesInter", id);
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

	public List findByExample(SolicitudesInter instance) {
		log.debug("finding SolicitudesInter instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.SolicitudesInter")
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
