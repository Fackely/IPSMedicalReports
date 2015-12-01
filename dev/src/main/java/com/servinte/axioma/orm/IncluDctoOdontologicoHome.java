package com.servinte.axioma.orm;

// Generated Jan 14, 2011 8:41:43 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class IncluDctoOdontologico.
 * @see com.servinte.axioma.orm.IncluDctoOdontologico
 * @author Hibernate Tools
 */
public class IncluDctoOdontologicoHome {

	private static final Log log = LogFactory
			.getLog(IncluDctoOdontologicoHome.class);

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

	public void persist(IncluDctoOdontologico transientInstance) {
		log.debug("persisting IncluDctoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(IncluDctoOdontologico instance) {
		log.debug("attaching dirty IncluDctoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(IncluDctoOdontologico instance) {
		log.debug("attaching clean IncluDctoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(IncluDctoOdontologico persistentInstance) {
		log.debug("deleting IncluDctoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public IncluDctoOdontologico merge(IncluDctoOdontologico detachedInstance) {
		log.debug("merging IncluDctoOdontologico instance");
		try {
			IncluDctoOdontologico result = (IncluDctoOdontologico) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public IncluDctoOdontologico findById(long id) {
		log.debug("getting IncluDctoOdontologico instance with id: " + id);
		try {
			IncluDctoOdontologico instance = (IncluDctoOdontologico) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.IncluDctoOdontologico", id);
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

	public List findByExample(IncluDctoOdontologico instance) {
		log.debug("finding IncluDctoOdontologico instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.IncluDctoOdontologico").add(
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
