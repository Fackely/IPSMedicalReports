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
 * Home object for domain model class DetalleValorizacionArt.
 * @see com.servinte.axioma.orm.DetalleValorizacionArt
 * @author Hibernate Tools
 */
public class DetalleValorizacionArtHome {

	protected static final Log log = LogFactory
			.getLog(DetalleValorizacionArtHome.class);

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

	public void persist(DetalleValorizacionArt transientInstance) {
		log.debug("persisting DetalleValorizacionArt instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetalleValorizacionArt instance) {
		log.debug("attaching dirty DetalleValorizacionArt instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetalleValorizacionArt instance) {
		log.debug("attaching clean DetalleValorizacionArt instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetalleValorizacionArt persistentInstance) {
		log.debug("deleting DetalleValorizacionArt instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetalleValorizacionArt merge(DetalleValorizacionArt detachedInstance) {
		log.debug("merging DetalleValorizacionArt instance");
		try {
			DetalleValorizacionArt result = (DetalleValorizacionArt) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetalleValorizacionArt findById(long id) {
		log.debug("getting DetalleValorizacionArt instance with id: " + id);
		try {
			DetalleValorizacionArt instance = (DetalleValorizacionArt) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetalleValorizacionArt",
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

	public List findByExample(DetalleValorizacionArt instance) {
		log.debug("finding DetalleValorizacionArt instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.DetalleValorizacionArt")
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
