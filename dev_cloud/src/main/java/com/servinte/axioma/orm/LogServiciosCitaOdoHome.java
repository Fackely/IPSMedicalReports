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
 * Home object for domain model class LogServiciosCitaOdo.
 * @see com.servinte.axioma.orm.LogServiciosCitaOdo
 * @author Hibernate Tools
 */
public class LogServiciosCitaOdoHome {

	private static final Log log = LogFactory
			.getLog(LogServiciosCitaOdoHome.class);

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

	public void persist(LogServiciosCitaOdo transientInstance) {
		log.debug("persisting LogServiciosCitaOdo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogServiciosCitaOdo instance) {
		log.debug("attaching dirty LogServiciosCitaOdo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogServiciosCitaOdo instance) {
		log.debug("attaching clean LogServiciosCitaOdo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogServiciosCitaOdo persistentInstance) {
		log.debug("deleting LogServiciosCitaOdo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogServiciosCitaOdo merge(LogServiciosCitaOdo detachedInstance) {
		log.debug("merging LogServiciosCitaOdo instance");
		try {
			LogServiciosCitaOdo result = (LogServiciosCitaOdo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogServiciosCitaOdo findById(long id) {
		log.debug("getting LogServiciosCitaOdo instance with id: " + id);
		try {
			LogServiciosCitaOdo instance = (LogServiciosCitaOdo) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogServiciosCitaOdo", id);
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

	public List findByExample(LogServiciosCitaOdo instance) {
		log.debug("finding LogServiciosCitaOdo instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogServiciosCitaOdo").add(
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
