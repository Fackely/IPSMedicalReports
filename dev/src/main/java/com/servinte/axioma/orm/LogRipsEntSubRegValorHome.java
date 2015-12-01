package com.servinte.axioma.orm;

// Generated Feb 23, 2011 5:31:37 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogRipsEntSubRegValor.
 * @see com.servinte.axioma.orm.LogRipsEntSubRegValor
 * @author Hibernate Tools
 */
public class LogRipsEntSubRegValorHome {

	private static final Log log = LogFactory
			.getLog(LogRipsEntSubRegValorHome.class);

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

	public void persist(LogRipsEntSubRegValor transientInstance) {
		log.debug("persisting LogRipsEntSubRegValor instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogRipsEntSubRegValor instance) {
		log.debug("attaching dirty LogRipsEntSubRegValor instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogRipsEntSubRegValor instance) {
		log.debug("attaching clean LogRipsEntSubRegValor instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogRipsEntSubRegValor persistentInstance) {
		log.debug("deleting LogRipsEntSubRegValor instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogRipsEntSubRegValor merge(LogRipsEntSubRegValor detachedInstance) {
		log.debug("merging LogRipsEntSubRegValor instance");
		try {
			LogRipsEntSubRegValor result = (LogRipsEntSubRegValor) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogRipsEntSubRegValor findById(long id) {
		log.debug("getting LogRipsEntSubRegValor instance with id: " + id);
		try {
			LogRipsEntSubRegValor instance = (LogRipsEntSubRegValor) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.LogRipsEntSubRegValor", id);
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

	public List findByExample(LogRipsEntSubRegValor instance) {
		log.debug("finding LogRipsEntSubRegValor instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogRipsEntSubRegValor").add(
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
