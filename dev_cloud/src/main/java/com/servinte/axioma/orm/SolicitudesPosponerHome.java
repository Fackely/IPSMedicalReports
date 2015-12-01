package com.servinte.axioma.orm;

// Generated Dec 15, 2010 5:58:38 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class SolicitudesPosponer.
 * @see com.servinte.axioma.orm.SolicitudesPosponer
 * @author Hibernate Tools
 */
public class SolicitudesPosponerHome {

	private static final Log log = LogFactory
			.getLog(SolicitudesPosponerHome.class);

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

	public void persist(SolicitudesPosponer transientInstance) {
		log.debug("persisting SolicitudesPosponer instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SolicitudesPosponer instance) {
		log.debug("attaching dirty SolicitudesPosponer instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SolicitudesPosponer instance) {
		log.debug("attaching clean SolicitudesPosponer instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SolicitudesPosponer persistentInstance) {
		log.debug("deleting SolicitudesPosponer instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SolicitudesPosponer merge(SolicitudesPosponer detachedInstance) {
		log.debug("merging SolicitudesPosponer instance");
		try {
			SolicitudesPosponer result = (SolicitudesPosponer) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SolicitudesPosponer findById(long id) {
		log.debug("getting SolicitudesPosponer instance with id: " + id);
		try {
			SolicitudesPosponer instance = (SolicitudesPosponer) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SolicitudesPosponer", id);
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

	public List findByExample(SolicitudesPosponer instance) {
		log.debug("finding SolicitudesPosponer instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.SolicitudesPosponer").add(
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
