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
 * Home object for domain model class ErroresCargosEntSub.
 * @see com.servinte.axioma.orm.ErroresCargosEntSub
 * @author Hibernate Tools
 */
public class ErroresCargosEntSubHome {

	private static final Log log = LogFactory
			.getLog(ErroresCargosEntSubHome.class);

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

	public void persist(ErroresCargosEntSub transientInstance) {
		log.debug("persisting ErroresCargosEntSub instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ErroresCargosEntSub instance) {
		log.debug("attaching dirty ErroresCargosEntSub instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ErroresCargosEntSub instance) {
		log.debug("attaching clean ErroresCargosEntSub instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ErroresCargosEntSub persistentInstance) {
		log.debug("deleting ErroresCargosEntSub instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ErroresCargosEntSub merge(ErroresCargosEntSub detachedInstance) {
		log.debug("merging ErroresCargosEntSub instance");
		try {
			ErroresCargosEntSub result = (ErroresCargosEntSub) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ErroresCargosEntSub findById(
			com.servinte.axioma.orm.ErroresCargosEntSubId id) {
		log.debug("getting ErroresCargosEntSub instance with id: " + id);
		try {
			ErroresCargosEntSub instance = (ErroresCargosEntSub) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ErroresCargosEntSub", id);
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

	public List findByExample(ErroresCargosEntSub instance) {
		log.debug("finding ErroresCargosEntSub instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ErroresCargosEntSub").add(
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
