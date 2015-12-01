package com.servinte.axioma.orm;

// Generated May 10, 2010 11:37:05 AM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class SuperficieDental.
 * @see com.servinte.axioma.orm.SuperficieDental
 * @author Hibernate Tools
 */
public class SuperficieDentalHome {

	private static final Log log = LogFactory
			.getLog(SuperficieDentalHome.class);

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

	public void persist(SuperficieDental transientInstance) {
		log.debug("persisting SuperficieDental instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SuperficieDental instance) {
		log.debug("attaching dirty SuperficieDental instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SuperficieDental instance) {
		log.debug("attaching clean SuperficieDental instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SuperficieDental persistentInstance) {
		log.debug("deleting SuperficieDental instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SuperficieDental merge(SuperficieDental detachedInstance) {
		log.debug("merging SuperficieDental instance");
		try {
			SuperficieDental result = (SuperficieDental) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SuperficieDental findById(java.math.BigDecimal id) {
		log.debug("getting SuperficieDental instance with id: " + id);
		try {
			SuperficieDental instance = (SuperficieDental) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SuperficieDental", id);
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

	public List findByExample(SuperficieDental instance) {
		log.debug("finding SuperficieDental instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.SuperficieDental").add(
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
