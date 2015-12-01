package com.servinte.axioma.orm;

// Generated Sep 23, 2010 9:08:31 AM by Hibernate Tools 3.2.4.GA

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NivelAutorizacion.
 * @see com.servinte.axioma.orm.NivelAutorizacion
 * @author Hibernate Tools
 */
public class NivelAutorizacionHome {

	private static final Log log = LogFactory
			.getLog(NivelAutorizacionHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(NivelAutorizacion transientInstance) {
		log.debug("persisting NivelAutorizacion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NivelAutorizacion instance) {
		log.debug("attaching dirty NivelAutorizacion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NivelAutorizacion instance) {
		log.debug("attaching clean NivelAutorizacion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NivelAutorizacion persistentInstance) {
		log.debug("deleting NivelAutorizacion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NivelAutorizacion merge(NivelAutorizacion detachedInstance) {
		log.debug("merging NivelAutorizacion instance");
		try {
			NivelAutorizacion result = (NivelAutorizacion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NivelAutorizacion findById(int id) {
		log.debug("getting NivelAutorizacion instance with id: " + id);
		try {
			NivelAutorizacion instance = (NivelAutorizacion) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NivelAutorizacion", id);
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

	public List findByExample(NivelAutorizacion instance) {
		log.debug("finding NivelAutorizacion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.NivelAutorizacion").add(
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
