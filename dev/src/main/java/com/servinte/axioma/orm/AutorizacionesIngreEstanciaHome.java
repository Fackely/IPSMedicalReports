package com.servinte.axioma.orm;

// Generated Nov 17, 2010 5:12:44 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AutorizacionesIngreEstancia.
 * @see com.servinte.axioma.orm.AutorizacionesIngreEstancia
 * @author Hibernate Tools
 */
public class AutorizacionesIngreEstanciaHome {

	private static final Log log = LogFactory
			.getLog(AutorizacionesIngreEstanciaHome.class);

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

	public void persist(AutorizacionesIngreEstancia transientInstance) {
		log.debug("persisting AutorizacionesIngreEstancia instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AutorizacionesIngreEstancia instance) {
		log.debug("attaching dirty AutorizacionesIngreEstancia instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AutorizacionesIngreEstancia instance) {
		log.debug("attaching clean AutorizacionesIngreEstancia instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AutorizacionesIngreEstancia persistentInstance) {
		log.debug("deleting AutorizacionesIngreEstancia instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AutorizacionesIngreEstancia merge(
			AutorizacionesIngreEstancia detachedInstance) {
		log.debug("merging AutorizacionesIngreEstancia instance");
		try {
			AutorizacionesIngreEstancia result = (AutorizacionesIngreEstancia) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AutorizacionesIngreEstancia findById(long id) {
		log
				.debug("getting AutorizacionesIngreEstancia instance with id: "
						+ id);
		try {
			AutorizacionesIngreEstancia instance = (AutorizacionesIngreEstancia) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AutorizacionesIngreEstancia",
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

	public List findByExample(AutorizacionesIngreEstancia instance) {
		log.debug("finding AutorizacionesIngreEstancia instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AutorizacionesIngreEstancia").add(
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
