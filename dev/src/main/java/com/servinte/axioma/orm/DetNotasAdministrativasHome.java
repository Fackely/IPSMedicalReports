package com.servinte.axioma.orm;

// Generated May 19, 2010 10:58:28 AM by Hibernate Tools 3.3.0.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetNotasAdministrativas.
 * @see com.servinte.axioma.orm.DetNotasAdministrativas
 * @author Hibernate Tools
 */
public class DetNotasAdministrativasHome {

	private static final Log log = LogFactory
			.getLog(DetNotasAdministrativasHome.class);

	protected final SessionFactory sessionFactory =HibernateUtil.getSessionFactory();

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

	public void persist(DetNotasAdministrativas transientInstance) {
		log.debug("persisting DetNotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetNotasAdministrativas instance) {
		log.debug("attaching dirty DetNotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetNotasAdministrativas instance) {
		log.debug("attaching clean DetNotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetNotasAdministrativas persistentInstance) {
		log.debug("deleting DetNotasAdministrativas instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetNotasAdministrativas merge(
			DetNotasAdministrativas detachedInstance) {
		log.debug("merging DetNotasAdministrativas instance");
		try {
			DetNotasAdministrativas result = (DetNotasAdministrativas) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetNotasAdministrativas findById(int id) {
		log.debug("getting DetNotasAdministrativas instance with id: " + id);
		try {
			DetNotasAdministrativas instance = (DetNotasAdministrativas) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetNotasAdministrativas",
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

	public List findByExample(DetNotasAdministrativas instance) {
		log.debug("finding DetNotasAdministrativas instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetNotasAdministrativas").add(
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
