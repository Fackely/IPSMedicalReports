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
 * Home object for domain model class LogRipsEntSubInconsisCamp.
 * @see com.servinte.axioma.orm.LogRipsEntSubInconsisCamp
 * @author Hibernate Tools
 */
public class LogRipsEntSubInconsisCampHome {

	private static final Log log = LogFactory
			.getLog(LogRipsEntSubInconsisCampHome.class);

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

	public void persist(LogRipsEntSubInconsisCamp transientInstance) {
		log.debug("persisting LogRipsEntSubInconsisCamp instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogRipsEntSubInconsisCamp instance) {
		log.debug("attaching dirty LogRipsEntSubInconsisCamp instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogRipsEntSubInconsisCamp instance) {
		log.debug("attaching clean LogRipsEntSubInconsisCamp instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogRipsEntSubInconsisCamp persistentInstance) {
		log.debug("deleting LogRipsEntSubInconsisCamp instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogRipsEntSubInconsisCamp merge(
			LogRipsEntSubInconsisCamp detachedInstance) {
		log.debug("merging LogRipsEntSubInconsisCamp instance");
		try {
			LogRipsEntSubInconsisCamp result = (LogRipsEntSubInconsisCamp) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogRipsEntSubInconsisCamp findById(long id) {
		log.debug("getting LogRipsEntSubInconsisCamp instance with id: " + id);
		try {
			LogRipsEntSubInconsisCamp instance = (LogRipsEntSubInconsisCamp) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogRipsEntSubInconsisCamp",
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

	public List findByExample(LogRipsEntSubInconsisCamp instance) {
		log.debug("finding LogRipsEntSubInconsisCamp instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogRipsEntSubInconsisCamp").add(
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
