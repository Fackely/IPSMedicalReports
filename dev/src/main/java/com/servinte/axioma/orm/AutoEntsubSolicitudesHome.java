package com.servinte.axioma.orm;

// Generated 24/06/2011 12:15:38 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AutoEntsubSolicitudes.
 * @see com.servinte.axioma.orm.AutoEntsubSolicitudes
 * @author Hibernate Tools
 */
public class AutoEntsubSolicitudesHome {

	private static final Log log = LogFactory
			.getLog(AutoEntsubSolicitudesHome.class);

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

	public void persist(AutoEntsubSolicitudes transientInstance) {
		log.debug("persisting AutoEntsubSolicitudes instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutoEntsubSolicitudes instance) {
		log.debug("attaching dirty AutoEntsubSolicitudes instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutoEntsubSolicitudes instance) {
		log.debug("attaching clean AutoEntsubSolicitudes instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutoEntsubSolicitudes persistentInstance) {
		log.debug("deleting AutoEntsubSolicitudes instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutoEntsubSolicitudes merge(AutoEntsubSolicitudes detachedInstance) {
		log.debug("merging AutoEntsubSolicitudes instance");
		try {
			AutoEntsubSolicitudes result = (AutoEntsubSolicitudes) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutoEntsubSolicitudes findById(long id) {
		log.debug("getting AutoEntsubSolicitudes instance with id: " + id);
		try {
			AutoEntsubSolicitudes instance = (AutoEntsubSolicitudes) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AutoEntsubSolicitudes", id);
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

	public List findByExample(AutoEntsubSolicitudes instance) {
		log.debug("finding AutoEntsubSolicitudes instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.AutoEntsubSolicitudes")
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
