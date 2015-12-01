package com.servinte.axioma.orm;

// Generated Jun 8, 2010 5:26:03 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ServiciosPaqueteOdon.
 * @see com.servinte.axioma.orm.ServiciosPaqueteOdon
 * @author Hibernate Tools
 */
public class ServiciosPaqueteOdonHome {

	private static final Log log = LogFactory
			.getLog(ServiciosPaqueteOdonHome.class);

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

	public void persist(ServiciosPaqueteOdon transientInstance) {
		log.debug("persisting ServiciosPaqueteOdon instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ServiciosPaqueteOdon instance) {
		log.debug("attaching dirty ServiciosPaqueteOdon instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ServiciosPaqueteOdon instance) {
		log.debug("attaching clean ServiciosPaqueteOdon instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ServiciosPaqueteOdon persistentInstance) {
		log.debug("deleting ServiciosPaqueteOdon instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ServiciosPaqueteOdon merge(ServiciosPaqueteOdon detachedInstance) {
		log.debug("merging ServiciosPaqueteOdon instance");
		try {
			ServiciosPaqueteOdon result = (ServiciosPaqueteOdon) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ServiciosPaqueteOdon findById(int id) {
		log.debug("getting ServiciosPaqueteOdon instance with id: " + id);
		try {
			ServiciosPaqueteOdon instance = (ServiciosPaqueteOdon) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ServiciosPaqueteOdon", id);
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

	public List findByExample(ServiciosPaqueteOdon instance) {
		log.debug("finding ServiciosPaqueteOdon instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ServiciosPaqueteOdon").add(
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
