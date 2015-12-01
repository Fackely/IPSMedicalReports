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
 * Home object for domain model class PaquetesOdontologicos.
 * @see com.servinte.axioma.orm.PaquetesOdontologicos
 * @author Hibernate Tools
 */
public class PaquetesOdontologicosHome {

	private static final Log log = LogFactory
			.getLog(PaquetesOdontologicosHome.class);

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

	public void persist(PaquetesOdontologicos transientInstance) {
		log.debug("persisting PaquetesOdontologicos instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PaquetesOdontologicos instance) {
		log.debug("attaching dirty PaquetesOdontologicos instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PaquetesOdontologicos instance) {
		log.debug("attaching clean PaquetesOdontologicos instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PaquetesOdontologicos persistentInstance) {
		log.debug("deleting PaquetesOdontologicos instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PaquetesOdontologicos merge(PaquetesOdontologicos detachedInstance) {
		log.debug("merging PaquetesOdontologicos instance");
		try {
			PaquetesOdontologicos result = (PaquetesOdontologicos) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PaquetesOdontologicos findById(int id) {
		log.debug("getting PaquetesOdontologicos instance with id: " + id);
		try {
			PaquetesOdontologicos instance = (PaquetesOdontologicos) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.PaquetesOdontologicos", id);
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

	public List findByExample(PaquetesOdontologicos instance) {
		log.debug("finding PaquetesOdontologicos instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PaquetesOdontologicos").add(
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
