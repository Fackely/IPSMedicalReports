package com.servinte.axioma.orm;

// Generated 2/06/2011 08:10:38 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class SolicitudesCirugia.
 * @see com.servinte.axioma.orm.SolicitudesCirugia
 * @author Hibernate Tools
 */
public class SolicitudesCirugiaHome {

	private static final Log log = LogFactory
			.getLog(SolicitudesCirugiaHome.class);

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

	public void persist(SolicitudesCirugia transientInstance) {
		log.debug("persisting SolicitudesCirugia instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SolicitudesCirugia instance) {
		log.debug("attaching dirty SolicitudesCirugia instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SolicitudesCirugia instance) {
		log.debug("attaching clean SolicitudesCirugia instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SolicitudesCirugia persistentInstance) {
		log.debug("deleting SolicitudesCirugia instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SolicitudesCirugia merge(SolicitudesCirugia detachedInstance) {
		log.debug("merging SolicitudesCirugia instance");
		try {
			SolicitudesCirugia result = (SolicitudesCirugia) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SolicitudesCirugia findById(int id) {
		log.debug("getting SolicitudesCirugia instance with id: " + id);
		try {
			SolicitudesCirugia instance = (SolicitudesCirugia) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SolicitudesCirugia", id);
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

	public List findByExample(SolicitudesCirugia instance) {
		log.debug("finding SolicitudesCirugia instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.SolicitudesCirugia")
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
