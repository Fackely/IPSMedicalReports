package com.servinte.axioma.orm;

// Generated Feb 18, 2011 11:01:03 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogRipsEntidadesSubArchivo.
 * @see com.servinte.axioma.orm.LogRipsEntidadesSubArchivo
 * @author Hibernate Tools
 */
public class LogRipsEntidadesSubArchivoHome {

	private static final Log log = LogFactory
			.getLog(LogRipsEntidadesSubArchivoHome.class);

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

	public void persist(LogRipsEntidadesSubArchivo transientInstance) {
		log.debug("persisting LogRipsEntidadesSubArchivo instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogRipsEntidadesSubArchivo instance) {
		log.debug("attaching dirty LogRipsEntidadesSubArchivo instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogRipsEntidadesSubArchivo instance) {
		log.debug("attaching clean LogRipsEntidadesSubArchivo instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogRipsEntidadesSubArchivo persistentInstance) {
		log.debug("deleting LogRipsEntidadesSubArchivo instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogRipsEntidadesSubArchivo merge(
			LogRipsEntidadesSubArchivo detachedInstance) {
		log.debug("merging LogRipsEntidadesSubArchivo instance");
		try {
			LogRipsEntidadesSubArchivo result = (LogRipsEntidadesSubArchivo) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogRipsEntidadesSubArchivo findById(long id) {
		log.debug("getting LogRipsEntidadesSubArchivo instance with id: " + id);
		try {
			LogRipsEntidadesSubArchivo instance = (LogRipsEntidadesSubArchivo) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogRipsEntidadesSubArchivo",
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

	public List findByExample(LogRipsEntidadesSubArchivo instance) {
		log.debug("finding LogRipsEntidadesSubArchivo instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogRipsEntidadesSubArchivo").add(
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
