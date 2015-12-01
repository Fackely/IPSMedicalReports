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
 * Home object for domain model class AutorizacionesCapitacionSub.
 * @see com.servinte.axioma.orm.AutorizacionesCapitacionSub
 * @author Hibernate Tools
 */
public class AutorizacionesCapitacionSubHome {

	private static final Log log = LogFactory
			.getLog(AutorizacionesCapitacionSubHome.class);

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

	public void persist(AutorizacionesCapitacionSub transientInstance) {
		log.debug("persisting AutorizacionesCapitacionSub instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutorizacionesCapitacionSub instance) {
		log.debug("attaching dirty AutorizacionesCapitacionSub instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutorizacionesCapitacionSub instance) {
		log.debug("attaching clean AutorizacionesCapitacionSub instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutorizacionesCapitacionSub persistentInstance) {
		log.debug("deleting AutorizacionesCapitacionSub instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutorizacionesCapitacionSub merge(
			AutorizacionesCapitacionSub detachedInstance) {
		log.debug("merging AutorizacionesCapitacionSub instance");
		try {
			AutorizacionesCapitacionSub result = (AutorizacionesCapitacionSub) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutorizacionesCapitacionSub findById(long id) {
		log
				.debug("getting AutorizacionesCapitacionSub instance with id: "
						+ id);
		try {
			AutorizacionesCapitacionSub instance = (AutorizacionesCapitacionSub) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AutorizacionesCapitacionSub",
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

	public List findByExample(AutorizacionesCapitacionSub instance) {
		log.debug("finding AutorizacionesCapitacionSub instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AutorizacionesCapitacionSub").add(
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
