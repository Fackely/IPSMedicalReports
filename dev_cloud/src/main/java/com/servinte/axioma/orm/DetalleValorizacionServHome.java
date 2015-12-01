package com.servinte.axioma.orm;

// Generated 4/05/2011 11:22:23 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetalleValorizacionServ.
 * @see com.servinte.axioma.orm.DetalleValorizacionServ
 * @author Hibernate Tools
 */
public class DetalleValorizacionServHome {

	protected static final Log log = LogFactory
			.getLog(DetalleValorizacionServHome.class);

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

	public void persist(DetalleValorizacionServ transientInstance) {
		log.debug("persisting DetalleValorizacionServ instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetalleValorizacionServ instance) {
		log.debug("attaching dirty DetalleValorizacionServ instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetalleValorizacionServ instance) {
		log.debug("attaching clean DetalleValorizacionServ instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetalleValorizacionServ persistentInstance) {
		log.debug("deleting DetalleValorizacionServ instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetalleValorizacionServ merge(
			DetalleValorizacionServ detachedInstance) {
		log.debug("merging DetalleValorizacionServ instance");
		try {
			DetalleValorizacionServ result = (DetalleValorizacionServ) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetalleValorizacionServ findById(long id) {
		log.debug("getting DetalleValorizacionServ instance with id: " + id);
		try {
			DetalleValorizacionServ instance = (DetalleValorizacionServ) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetalleValorizacionServ",
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

	public List findByExample(DetalleValorizacionServ instance) {
		log.debug("finding DetalleValorizacionServ instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.DetalleValorizacionServ")
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
