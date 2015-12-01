package com.servinte.axioma.orm;

// Generated 17/02/2012 02:32:07 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class FormaFarmaceutica.
 * @see com.servinte.axioma.orm.FormaFarmaceutica
 * @author Hibernate Tools
 */
public class FormaFarmaceuticaHome {

	private static final Log log = LogFactory
			.getLog(FormaFarmaceuticaHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

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

	public void persist(FormaFarmaceutica transientInstance) {
		log.debug("persisting FormaFarmaceutica instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(FormaFarmaceutica instance) {
		log.debug("attaching dirty FormaFarmaceutica instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FormaFarmaceutica instance) {
		log.debug("attaching clean FormaFarmaceutica instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(FormaFarmaceutica persistentInstance) {
		log.debug("deleting FormaFarmaceutica instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FormaFarmaceutica merge(FormaFarmaceutica detachedInstance) {
		log.debug("merging FormaFarmaceutica instance");
		try {
			FormaFarmaceutica result = (FormaFarmaceutica) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public FormaFarmaceutica findById(
			com.servinte.axioma.orm.FormaFarmaceuticaId id) {
		log.debug("getting FormaFarmaceutica instance with id: " + id);
		try {
			FormaFarmaceutica instance = (FormaFarmaceutica) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.FormaFarmaceutica", id);
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

	public List findByExample(FormaFarmaceutica instance) {
		log.debug("finding FormaFarmaceutica instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.FormaFarmaceutica")
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
