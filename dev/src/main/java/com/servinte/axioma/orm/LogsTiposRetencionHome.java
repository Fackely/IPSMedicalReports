package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogsTiposRetencion.
 * @see com.servinte.axioma.orm.LogsTiposRetencion
 * @author Hibernate Tools
 */
public class LogsTiposRetencionHome {

	private static final Log log = LogFactory
			.getLog(LogsTiposRetencionHome.class);

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

	public void persist(LogsTiposRetencion transientInstance) {
		log.debug("persisting LogsTiposRetencion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogsTiposRetencion instance) {
		log.debug("attaching dirty LogsTiposRetencion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogsTiposRetencion instance) {
		log.debug("attaching clean LogsTiposRetencion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogsTiposRetencion persistentInstance) {
		log.debug("deleting LogsTiposRetencion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogsTiposRetencion merge(LogsTiposRetencion detachedInstance) {
		log.debug("merging LogsTiposRetencion instance");
		try {
			LogsTiposRetencion result = (LogsTiposRetencion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogsTiposRetencion findById(int id) {
		log.debug("getting LogsTiposRetencion instance with id: " + id);
		try {
			LogsTiposRetencion instance = (LogsTiposRetencion) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogsTiposRetencion", id);
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

	public List findByExample(LogsTiposRetencion instance) {
		log.debug("finding LogsTiposRetencion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogsTiposRetencion").add(
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
