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
 * Home object for domain model class LogParamPresupuestoCap.
 * @see com.servinte.axioma.orm.LogParamPresupuestoCap
 * @author Hibernate Tools
 */
public class LogParamPresupuestoCapHome {

	private static final Log log = LogFactory
			.getLog(LogParamPresupuestoCapHome.class);

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

	public void persist(LogParamPresupuestoCap transientInstance) {
		log.debug("persisting LogParamPresupuestoCap instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogParamPresupuestoCap instance) {
		log.debug("attaching dirty LogParamPresupuestoCap instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogParamPresupuestoCap instance) {
		log.debug("attaching clean LogParamPresupuestoCap instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogParamPresupuestoCap persistentInstance) {
		log.debug("deleting LogParamPresupuestoCap instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogParamPresupuestoCap merge(LogParamPresupuestoCap detachedInstance) {
		log.debug("merging LogParamPresupuestoCap instance");
		try {
			LogParamPresupuestoCap result = (LogParamPresupuestoCap) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogParamPresupuestoCap findById(long id) {
		log.debug("getting LogParamPresupuestoCap instance with id: " + id);
		try {
			LogParamPresupuestoCap instance = (LogParamPresupuestoCap) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogParamPresupuestoCap",
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

	public List findByExample(LogParamPresupuestoCap instance) {
		log.debug("finding LogParamPresupuestoCap instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.LogParamPresupuestoCap")
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
