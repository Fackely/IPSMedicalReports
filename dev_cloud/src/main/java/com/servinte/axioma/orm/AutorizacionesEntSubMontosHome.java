package com.servinte.axioma.orm;

// Generated 30/06/2011 05:03:07 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AutorizacionesEntSubMontos.
 * @see com.servinte.axioma.orm.AutorizacionesEntSubMontos
 * @author Hibernate Tools
 */
public class AutorizacionesEntSubMontosHome {

	private static final Log log = LogFactory
			.getLog(AutorizacionesEntSubMontosHome.class);

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

	public void persist(AutorizacionesEntSubMontos transientInstance) {
		log.debug("persisting AutorizacionesEntSubMontos instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutorizacionesEntSubMontos instance) {
		log.debug("attaching dirty AutorizacionesEntSubMontos instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutorizacionesEntSubMontos instance) {
		log.debug("attaching clean AutorizacionesEntSubMontos instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutorizacionesEntSubMontos persistentInstance) {
		log.debug("deleting AutorizacionesEntSubMontos instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutorizacionesEntSubMontos merge(
			AutorizacionesEntSubMontos detachedInstance) {
		log.debug("merging AutorizacionesEntSubMontos instance");
		try {
			AutorizacionesEntSubMontos result = (AutorizacionesEntSubMontos) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutorizacionesEntSubMontos findById(long id) {
		log.debug("getting AutorizacionesEntSubMontos instance with id: " + id);
		try {
			AutorizacionesEntSubMontos instance = (AutorizacionesEntSubMontos) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AutorizacionesEntSubMontos",
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

	public List findByExample(AutorizacionesEntSubMontos instance) {
		log.debug("finding AutorizacionesEntSubMontos instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.AutorizacionesEntSubMontos")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
