package com.servinte.axioma.orm;

// Generated 26/01/2012 02:41:24 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DatosFinanciacion.
 * @see com.servinte.axioma.orm.DatosFinanciacion
 * @author Hibernate Tools
 */
public class DatosFinanciacionHome {

	private static final Log log = LogFactory
			.getLog(DatosFinanciacionHome.class);

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

	public void persist(DatosFinanciacion transientInstance) {
		log.debug("persisting DatosFinanciacion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DatosFinanciacion instance) {
		log.debug("attaching dirty DatosFinanciacion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DatosFinanciacion instance) {
		log.debug("attaching clean DatosFinanciacion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DatosFinanciacion persistentInstance) {
		log.debug("deleting DatosFinanciacion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DatosFinanciacion merge(DatosFinanciacion detachedInstance) {
		log.debug("merging DatosFinanciacion instance");
		try {
			DatosFinanciacion result = (DatosFinanciacion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DatosFinanciacion findById(long id) {
		log.debug("getting DatosFinanciacion instance with id: " + id);
		try {
			DatosFinanciacion instance = (DatosFinanciacion) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DatosFinanciacion", id);
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

	public List findByExample(DatosFinanciacion instance) {
		log.debug("finding DatosFinanciacion instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.DatosFinanciacion")
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
