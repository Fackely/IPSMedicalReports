package com.servinte.axioma.orm;

// Generated Nov 18, 2010 5:00:12 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ComisionXCentroAtencion.
 * @see com.servinte.axioma.orm.ComisionXCentroAtencion
 * @author Hibernate Tools
 */
public class ComisionXCentroAtencionHome {

	private static final Log log = LogFactory
			.getLog(ComisionXCentroAtencionHome.class);

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

	public void persist(ComisionXCentroAtencion transientInstance) {
		log.debug("persisting ComisionXCentroAtencion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ComisionXCentroAtencion instance) {
		log.debug("attaching dirty ComisionXCentroAtencion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ComisionXCentroAtencion instance) {
		log.debug("attaching clean ComisionXCentroAtencion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ComisionXCentroAtencion persistentInstance) {
		log.debug("deleting ComisionXCentroAtencion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ComisionXCentroAtencion merge(
			ComisionXCentroAtencion detachedInstance) {
		log.debug("merging ComisionXCentroAtencion instance");
		try {
			ComisionXCentroAtencion result = (ComisionXCentroAtencion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ComisionXCentroAtencion findById(long id) {
		log.debug("getting ComisionXCentroAtencion instance with id: " + id);
		try {
			ComisionXCentroAtencion instance = (ComisionXCentroAtencion) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ComisionXCentroAtencion",
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

	public List findByExample(ComisionXCentroAtencion instance) {
		log.debug("finding ComisionXCentroAtencion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ComisionXCentroAtencion").add(
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
