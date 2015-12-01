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
 * Home object for domain model class LogRipsEntSubInconsisArch.
 * @see com.servinte.axioma.orm.LogRipsEntSubInconsisArch
 * @author Hibernate Tools
 */
public class LogRipsEntSubInconsisArchHome {

	private static final Log log = LogFactory
			.getLog(LogRipsEntSubInconsisArchHome.class);

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

	public void persist(LogRipsEntSubInconsisArch transientInstance) {
		log.debug("persisting LogRipsEntSubInconsisArch instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogRipsEntSubInconsisArch instance) {
		log.debug("attaching dirty LogRipsEntSubInconsisArch instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogRipsEntSubInconsisArch instance) {
		log.debug("attaching clean LogRipsEntSubInconsisArch instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogRipsEntSubInconsisArch persistentInstance) {
		log.debug("deleting LogRipsEntSubInconsisArch instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogRipsEntSubInconsisArch merge(
			LogRipsEntSubInconsisArch detachedInstance) {
		log.debug("merging LogRipsEntSubInconsisArch instance");
		try {
			LogRipsEntSubInconsisArch result = (LogRipsEntSubInconsisArch) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogRipsEntSubInconsisArch findById(long id) {
		log.debug("getting LogRipsEntSubInconsisArch instance with id: " + id);
		try {
			LogRipsEntSubInconsisArch instance = (LogRipsEntSubInconsisArch) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogRipsEntSubInconsisArch",
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

	public List findByExample(LogRipsEntSubInconsisArch instance) {
		log.debug("finding LogRipsEntSubInconsisArch instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogRipsEntSubInconsisArch").add(
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
