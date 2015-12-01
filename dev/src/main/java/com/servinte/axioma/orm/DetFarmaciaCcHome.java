package com.servinte.axioma.orm;

// Generated 16/01/2012 07:10:27 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetFarmaciaCc.
 * @see com.servinte.axioma.orm.DetFarmaciaCc
 * @author Hibernate Tools
 */
public class DetFarmaciaCcHome {

	private static final Log log = LogFactory.getLog(DetFarmaciaCcHome.class);

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

	public void persist(DetFarmaciaCc transientInstance) {
		log.debug("persisting DetFarmaciaCc instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetFarmaciaCc instance) {
		log.debug("attaching dirty DetFarmaciaCc instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetFarmaciaCc instance) {
		log.debug("attaching clean DetFarmaciaCc instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetFarmaciaCc persistentInstance) {
		log.debug("deleting DetFarmaciaCc instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetFarmaciaCc merge(DetFarmaciaCc detachedInstance) {
		log.debug("merging DetFarmaciaCc instance");
		try {
			DetFarmaciaCc result = (DetFarmaciaCc) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetFarmaciaCc findById(com.servinte.axioma.orm.DetFarmaciaCcId id) {
		log.debug("getting DetFarmaciaCc instance with id: " + id);
		try {
			DetFarmaciaCc instance = (DetFarmaciaCc) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetFarmaciaCc", id);
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

	public List findByExample(DetFarmaciaCc instance) {
		log.debug("finding DetFarmaciaCc instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.DetFarmaciaCc")
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
