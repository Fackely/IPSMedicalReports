package com.servinte.axioma.orm;

// Generated Jan 17, 2011 3:52:32 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AutorizacionPresuDctoOdon.
 * @see com.servinte.axioma.orm.AutorizacionPresuDctoOdon
 * @author Hibernate Tools
 */
public class AutorizacionPresuDctoOdonHome {

	private static final Log log = LogFactory
			.getLog(AutorizacionPresuDctoOdonHome.class);

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

	public void persist(AutorizacionPresuDctoOdon transientInstance) {
		log.debug("persisting AutorizacionPresuDctoOdon instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutorizacionPresuDctoOdon instance) {
		log.debug("attaching dirty AutorizacionPresuDctoOdon instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutorizacionPresuDctoOdon instance) {
		log.debug("attaching clean AutorizacionPresuDctoOdon instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutorizacionPresuDctoOdon persistentInstance) {
		log.debug("deleting AutorizacionPresuDctoOdon instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutorizacionPresuDctoOdon merge(
			AutorizacionPresuDctoOdon detachedInstance) {
		log.debug("merging AutorizacionPresuDctoOdon instance");
		try {
			AutorizacionPresuDctoOdon result = (AutorizacionPresuDctoOdon) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutorizacionPresuDctoOdon findById(long id) {
		log.debug("getting AutorizacionPresuDctoOdon instance with id: " + id);
		try {
			AutorizacionPresuDctoOdon instance = (AutorizacionPresuDctoOdon) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AutorizacionPresuDctoOdon",
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

	public List findByExample(AutorizacionPresuDctoOdon instance) {
		log.debug("finding AutorizacionPresuDctoOdon instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AutorizacionPresuDctoOdon").add(
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
