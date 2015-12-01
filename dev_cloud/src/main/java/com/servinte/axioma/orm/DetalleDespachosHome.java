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
 * Home object for domain model class DetalleDespachos.
 * @see com.servinte.axioma.orm.DetalleDespachos
 * @author Hibernate Tools
 */
public class DetalleDespachosHome {

	private static final Log log = LogFactory
			.getLog(DetalleDespachosHome.class);

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

	public void persist(DetalleDespachos transientInstance) {
		log.debug("persisting DetalleDespachos instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetalleDespachos instance) {
		log.debug("attaching dirty DetalleDespachos instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetalleDespachos instance) {
		log.debug("attaching clean DetalleDespachos instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetalleDespachos persistentInstance) {
		log.debug("deleting DetalleDespachos instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetalleDespachos merge(DetalleDespachos detachedInstance) {
		log.debug("merging DetalleDespachos instance");
		try {
			DetalleDespachos result = (DetalleDespachos) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetalleDespachos findById(
			com.servinte.axioma.orm.DetalleDespachosId id) {
		log.debug("getting DetalleDespachos instance with id: " + id);
		try {
			DetalleDespachos instance = (DetalleDespachos) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetalleDespachos", id);
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

	public List findByExample(DetalleDespachos instance) {
		log.debug("finding DetalleDespachos instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetalleDespachos").add(
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
