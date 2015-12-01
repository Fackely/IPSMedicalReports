package com.servinte.axioma.orm;

// Generated Nov 24, 2010 2:03:24 PM by Hibernate Tools 3.2.4.GA

import java.util.List;

import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AutorizacionesEntSubArticu.
 * @see com.servinte.axioma.orm.AutorizacionesEntSubArticu
 * @author Hibernate Tools
 */
public class AutorizacionesEntSubArticuHome {

	private static final Log log = LogFactory
			.getLog(AutorizacionesEntSubArticuHome.class);

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

	public void persist(AutorizacionesEntSubArticu transientInstance) {
		log.debug("persisting AutorizacionesEntSubArticu instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutorizacionesEntSubArticu instance) {
		log.debug("attaching dirty AutorizacionesEntSubArticu instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutorizacionesEntSubArticu instance) {
		log.debug("attaching clean AutorizacionesEntSubArticu instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutorizacionesEntSubArticu persistentInstance) {
		log.debug("deleting AutorizacionesEntSubArticu instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutorizacionesEntSubArticu merge(
			AutorizacionesEntSubArticu detachedInstance) {
		log.debug("merging AutorizacionesEntSubArticu instance");
		try {
			AutorizacionesEntSubArticu result = (AutorizacionesEntSubArticu) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutorizacionesEntSubArticu findById(long id) {
		log.debug("getting AutorizacionesEntSubArticu instance with id: " + id);
		try {
			AutorizacionesEntSubArticu instance = (AutorizacionesEntSubArticu) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AutorizacionesEntSubArticu",
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

	public List findByExample(AutorizacionesEntSubArticu instance) {
		log.debug("finding AutorizacionesEntSubArticu instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AutorizacionesEntSubArticu").add(
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
