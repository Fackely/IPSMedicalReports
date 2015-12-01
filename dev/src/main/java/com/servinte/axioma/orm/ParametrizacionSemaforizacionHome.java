package com.servinte.axioma.orm;

// Generated 25/04/2011 02:55:41 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ParametrizacionSemaforizacion.
 * @see com.servinte.axioma.orm.ParametrizacionSemaforizacion
 * @author Hibernate Tools
 */
public class ParametrizacionSemaforizacionHome {

	private static final Log log = LogFactory
			.getLog(ParametrizacionSemaforizacionHome.class);

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

	public void persist(ParametrizacionSemaforizacion transientInstance) {
		log.debug("persisting ParametrizacionSemaforizacion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ParametrizacionSemaforizacion instance) {
		log.debug("attaching dirty ParametrizacionSemaforizacion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ParametrizacionSemaforizacion instance) {
		log.debug("attaching clean ParametrizacionSemaforizacion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ParametrizacionSemaforizacion persistentInstance) {
		log.debug("deleting ParametrizacionSemaforizacion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ParametrizacionSemaforizacion merge(
			ParametrizacionSemaforizacion detachedInstance) {
		log.debug("merging ParametrizacionSemaforizacion instance");
		try {
			ParametrizacionSemaforizacion result = (ParametrizacionSemaforizacion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ParametrizacionSemaforizacion findById(long id) {
		log.debug("getting ParametrizacionSemaforizacion instance with id: "
				+ id);
		try {
			ParametrizacionSemaforizacion instance = (ParametrizacionSemaforizacion) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ParametrizacionSemaforizacion",
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

	public List findByExample(ParametrizacionSemaforizacion instance) {
		log.debug("finding ParametrizacionSemaforizacion instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.ParametrizacionSemaforizacion")
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
