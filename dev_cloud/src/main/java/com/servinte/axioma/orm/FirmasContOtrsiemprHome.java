package com.servinte.axioma.orm;

// Generated Jul 6, 2010 9:27:06 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class FirmasContOtrsiempr.
 * @see com.servinte.axioma.orm.FirmasContOtrsiempr
 * @author Hibernate Tools
 */
public class FirmasContOtrsiemprHome {

	private static final Log log = LogFactory
			.getLog(FirmasContOtrsiemprHome.class);

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

	public void persist(FirmasContOtrsiempr transientInstance) {
		log.debug("persisting FirmasContOtrsiempr instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(FirmasContOtrsiempr instance) {
		log.debug("attaching dirty FirmasContOtrsiempr instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FirmasContOtrsiempr instance) {
		log.debug("attaching clean FirmasContOtrsiempr instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(FirmasContOtrsiempr persistentInstance) {
		log.debug("deleting FirmasContOtrsiempr instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FirmasContOtrsiempr merge(FirmasContOtrsiempr detachedInstance) {
		log.debug("merging FirmasContOtrsiempr instance");
		try {
			FirmasContOtrsiempr result = (FirmasContOtrsiempr) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public FirmasContOtrsiempr findById(long id) {
		log.debug("getting FirmasContOtrsiempr instance with id: " + id);
		try {
			FirmasContOtrsiempr instance = (FirmasContOtrsiempr) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.FirmasContOtrsiempr", id);
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

	public List findByExample(FirmasContOtrsiempr instance) {
		log.debug("finding FirmasContOtrsiempr instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.FirmasContOtrsiempr").add(
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
