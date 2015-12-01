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
 * Home object for domain model class LogActualizAutorizaciones.
 * @see com.servinte.axioma.orm.LogActualizAutorizaciones
 * @author Hibernate Tools
 */
public class LogActualizAutorizacionesHome {

	private static final Log log = LogFactory
			.getLog(LogActualizAutorizacionesHome.class);

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

	public void persist(LogActualizAutorizaciones transientInstance) {
		log.debug("persisting LogActualizAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogActualizAutorizaciones instance) {
		log.debug("attaching dirty LogActualizAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogActualizAutorizaciones instance) {
		log.debug("attaching clean LogActualizAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogActualizAutorizaciones persistentInstance) {
		log.debug("deleting LogActualizAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogActualizAutorizaciones merge(
			LogActualizAutorizaciones detachedInstance) {
		log.debug("merging LogActualizAutorizaciones instance");
		try {
			LogActualizAutorizaciones result = (LogActualizAutorizaciones) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogActualizAutorizaciones findById(
			com.servinte.axioma.orm.LogActualizAutorizacionesId id) {
		log.debug("getting LogActualizAutorizaciones instance with id: " + id);
		try {
			LogActualizAutorizaciones instance = (LogActualizAutorizaciones) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogActualizAutorizaciones",
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

	public List findByExample(LogActualizAutorizaciones instance) {
		log.debug("finding LogActualizAutorizaciones instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogActualizAutorizaciones").add(
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
