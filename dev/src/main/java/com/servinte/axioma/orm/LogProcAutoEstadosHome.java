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
 * Home object for domain model class LogProcAutoEstados.
 * @see com.servinte.axioma.orm.LogProcAutoEstados
 * @author Hibernate Tools
 */
public class LogProcAutoEstadosHome {

	private static final Log log = LogFactory
			.getLog(LogProcAutoEstadosHome.class);

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

	public void persist(LogProcAutoEstados transientInstance) {
		log.debug("persisting LogProcAutoEstados instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogProcAutoEstados instance) {
		log.debug("attaching dirty LogProcAutoEstados instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogProcAutoEstados instance) {
		log.debug("attaching clean LogProcAutoEstados instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogProcAutoEstados persistentInstance) {
		log.debug("deleting LogProcAutoEstados instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogProcAutoEstados merge(LogProcAutoEstados detachedInstance) {
		log.debug("merging LogProcAutoEstados instance");
		try {
			LogProcAutoEstados result = (LogProcAutoEstados) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogProcAutoEstados findById(
			com.servinte.axioma.orm.LogProcAutoEstadosId id) {
		log.debug("getting LogProcAutoEstados instance with id: " + id);
		try {
			LogProcAutoEstados instance = (LogProcAutoEstados) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogProcAutoEstados", id);
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

	public List findByExample(LogProcAutoEstados instance) {
		log.debug("finding LogProcAutoEstados instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogProcAutoEstados").add(
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
