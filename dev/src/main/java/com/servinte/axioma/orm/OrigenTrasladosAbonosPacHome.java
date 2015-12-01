package com.servinte.axioma.orm;

// Generated Jul 22, 2010 12:52:38 PM by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class OrigenTrasladosAbonosPac.
 * @see com.servinte.axioma.orm.OrigenTrasladosAbonosPac
 * @author Hibernate Tools
 */
public class OrigenTrasladosAbonosPacHome {

	private static final Log log = LogFactory
			.getLog(OrigenTrasladosAbonosPacHome.class);

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

	public void persist(OrigenTrasladosAbonosPac transientInstance) {
		log.debug("persisting OrigenTrasladosAbonosPac instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(OrigenTrasladosAbonosPac instance) {
		log.debug("attaching dirty OrigenTrasladosAbonosPac instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrigenTrasladosAbonosPac instance) {
		log.debug("attaching clean OrigenTrasladosAbonosPac instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(OrigenTrasladosAbonosPac persistentInstance) {
		log.debug("deleting OrigenTrasladosAbonosPac instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OrigenTrasladosAbonosPac merge(
			OrigenTrasladosAbonosPac detachedInstance) {
		log.debug("merging OrigenTrasladosAbonosPac instance");
		try {
			OrigenTrasladosAbonosPac result = (OrigenTrasladosAbonosPac) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public OrigenTrasladosAbonosPac findById(long id) {
		log.debug("getting OrigenTrasladosAbonosPac instance with id: " + id);
		try {
			OrigenTrasladosAbonosPac instance = (OrigenTrasladosAbonosPac) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.OrigenTrasladosAbonosPac",
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

	public List findByExample(OrigenTrasladosAbonosPac instance) {
		log.debug("finding OrigenTrasladosAbonosPac instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.OrigenTrasladosAbonosPac").add(
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
