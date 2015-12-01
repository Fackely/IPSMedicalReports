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
 * Home object for domain model class LogRipsEntidadesSub.
 * @see com.servinte.axioma.orm.LogRipsEntidadesSub
 * @author Hibernate Tools
 */
public class LogRipsEntidadesSubHome {

	private static final Log log = LogFactory
			.getLog(LogRipsEntidadesSubHome.class);

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

	public void persist(LogRipsEntidadesSub transientInstance) {
		log.debug("persisting LogRipsEntidadesSub instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogRipsEntidadesSub instance) {
		log.debug("attaching dirty LogRipsEntidadesSub instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogRipsEntidadesSub instance) {
		log.debug("attaching clean LogRipsEntidadesSub instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogRipsEntidadesSub persistentInstance) {
		log.debug("deleting LogRipsEntidadesSub instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogRipsEntidadesSub merge(LogRipsEntidadesSub detachedInstance) {
		log.debug("merging LogRipsEntidadesSub instance");
		try {
			LogRipsEntidadesSub result = (LogRipsEntidadesSub) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogRipsEntidadesSub findById(long id) {
		log.debug("getting LogRipsEntidadesSub instance with id: " + id);
		try {
			LogRipsEntidadesSub instance = (LogRipsEntidadesSub) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogRipsEntidadesSub", id);
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

	public List findByExample(LogRipsEntidadesSub instance) {
		log.debug("finding LogRipsEntidadesSub instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogRipsEntidadesSub").add(
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
