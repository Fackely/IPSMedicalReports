package com.servinte.axioma.orm;

// Generated Sep 24, 2010 5:55:47 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PrioridadUsuEsp.
 * @see com.servinte.axioma.orm.PrioridadUsuEsp
 * @author Hibernate Tools
 */
public class PrioridadUsuEspHome {

	private static final Log log = LogFactory.getLog(PrioridadUsuEspHome.class);

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

	public void persist(PrioridadUsuEsp transientInstance) {
		log.debug("persisting PrioridadUsuEsp instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PrioridadUsuEsp instance) {
		log.debug("attaching dirty PrioridadUsuEsp instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PrioridadUsuEsp instance) {
		log.debug("attaching clean PrioridadUsuEsp instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PrioridadUsuEsp persistentInstance) {
		log.debug("deleting PrioridadUsuEsp instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PrioridadUsuEsp merge(PrioridadUsuEsp detachedInstance) {
		log.debug("merging PrioridadUsuEsp instance");
		try {
			PrioridadUsuEsp result = (PrioridadUsuEsp) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PrioridadUsuEsp findById(int id) {
		log.debug("getting PrioridadUsuEsp instance with id: " + id);
		try {
			PrioridadUsuEsp instance = (PrioridadUsuEsp) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PrioridadUsuEsp", id);
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

	public List findByExample(PrioridadUsuEsp instance) {
		log.debug("finding PrioridadUsuEsp instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PrioridadUsuEsp").add(
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
