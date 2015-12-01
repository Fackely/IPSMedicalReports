package com.servinte.axioma.orm;

// Generated 20/05/2011 04:42:56 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogDetalleParamPresup.
 * @see com.servinte.axioma.orm.LogDetalleParamPresup
 * @author Hibernate Tools
 */
public class LogDetalleParamPresupHome {

	private static final Log log = LogFactory
			.getLog(LogDetalleParamPresupHome.class);

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

	public void persist(LogDetalleParamPresup transientInstance) {
		log.debug("persisting LogDetalleParamPresup instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogDetalleParamPresup instance) {
		log.debug("attaching dirty LogDetalleParamPresup instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogDetalleParamPresup instance) {
		log.debug("attaching clean LogDetalleParamPresup instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogDetalleParamPresup persistentInstance) {
		log.debug("deleting LogDetalleParamPresup instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogDetalleParamPresup merge(LogDetalleParamPresup detachedInstance) {
		log.debug("merging LogDetalleParamPresup instance");
		try {
			LogDetalleParamPresup result = (LogDetalleParamPresup) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogDetalleParamPresup findById(long id) {
		log.debug("getting LogDetalleParamPresup instance with id: " + id);
		try {
			LogDetalleParamPresup instance = (LogDetalleParamPresup) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogDetalleParamPresup", id);
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

	public List findByExample(LogDetalleParamPresup instance) {
		log.debug("finding LogDetalleParamPresup instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.LogDetalleParamPresup")
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
