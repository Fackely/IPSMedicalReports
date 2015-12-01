package com.servinte.axioma.orm;

// Generated May 7, 2010 9:54:03 AM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogEnvioEmailAutomatico.
 * @see com.servinte.axioma.orm.LogEnvioEmailAutomatico
 * @author Hibernate Tools
 */
public class LogEnvioEmailAutomaticoHome {

	private static final Log log = LogFactory
			.getLog(LogEnvioEmailAutomaticoHome.class);

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

	public void persist(LogEnvioEmailAutomatico transientInstance) {
		log.debug("persisting LogEnvioEmailAutomatico instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogEnvioEmailAutomatico instance) {
		log.debug("attaching dirty LogEnvioEmailAutomatico instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogEnvioEmailAutomatico instance) {
		log.debug("attaching clean LogEnvioEmailAutomatico instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogEnvioEmailAutomatico persistentInstance) {
		log.debug("deleting LogEnvioEmailAutomatico instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogEnvioEmailAutomatico merge(
			LogEnvioEmailAutomatico detachedInstance) {
		log.debug("merging LogEnvioEmailAutomatico instance");
		try {
			LogEnvioEmailAutomatico result = (LogEnvioEmailAutomatico) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogEnvioEmailAutomatico findById(long id) {
		log.debug("getting LogEnvioEmailAutomatico instance with id: " + id);
		try {
			LogEnvioEmailAutomatico instance = (LogEnvioEmailAutomatico) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogEnvioEmailAutomatico",
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

	public List findByExample(LogEnvioEmailAutomatico instance) {
		log.debug("finding LogEnvioEmailAutomatico instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogEnvioEmailAutomatico").add(
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
