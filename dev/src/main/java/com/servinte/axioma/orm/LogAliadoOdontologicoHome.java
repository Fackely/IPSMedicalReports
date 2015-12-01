package com.servinte.axioma.orm;

// Generated May 3, 2010 4:30:56 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogAliadoOdontologico.
 * @see com.servinte.axioma.orm.LogAliadoOdontologico
 * @author Hibernate Tools
 */
public class LogAliadoOdontologicoHome {

	private static final Log log = LogFactory
			.getLog(LogAliadoOdontologicoHome.class);

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

	public void persist(LogAliadoOdontologico transientInstance) {
		log.debug("persisting LogAliadoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogAliadoOdontologico instance) {
		log.debug("attaching dirty LogAliadoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogAliadoOdontologico instance) {
		log.debug("attaching clean LogAliadoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogAliadoOdontologico persistentInstance) {
		log.debug("deleting LogAliadoOdontologico instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogAliadoOdontologico merge(LogAliadoOdontologico detachedInstance) {
		log.debug("merging LogAliadoOdontologico instance");
		try {
			LogAliadoOdontologico result = (LogAliadoOdontologico) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogAliadoOdontologico findById(long id) {
		log.debug("getting LogAliadoOdontologico instance with id: " + id);
		try {
			LogAliadoOdontologico instance = (LogAliadoOdontologico) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogAliadoOdontologico", id);
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

	public List findByExample(LogAliadoOdontologico instance) {
		log.debug("finding LogAliadoOdontologico instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogAliadoOdontologico").add(
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
