package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:55 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PresupuestoCuotasEsp.
 * @see com.servinte.axioma.orm.PresupuestoCuotasEsp
 * @author Hibernate Tools
 */
public class PresupuestoCuotasEspHome {

	private static final Log log = LogFactory
			.getLog(PresupuestoCuotasEspHome.class);

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

	public void persist(PresupuestoCuotasEsp transientInstance) {
		log.debug("persisting PresupuestoCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresupuestoCuotasEsp instance) {
		log.debug("attaching dirty PresupuestoCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresupuestoCuotasEsp instance) {
		log.debug("attaching clean PresupuestoCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresupuestoCuotasEsp persistentInstance) {
		log.debug("deleting PresupuestoCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresupuestoCuotasEsp merge(PresupuestoCuotasEsp detachedInstance) {
		log.debug("merging PresupuestoCuotasEsp instance");
		try {
			PresupuestoCuotasEsp result = (PresupuestoCuotasEsp) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresupuestoCuotasEsp findById(long id) {
		log.debug("getting PresupuestoCuotasEsp instance with id: " + id);
		try {
			PresupuestoCuotasEsp instance = (PresupuestoCuotasEsp) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresupuestoCuotasEsp", id);
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

	public List findByExample(PresupuestoCuotasEsp instance) {
		log.debug("finding PresupuestoCuotasEsp instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresupuestoCuotasEsp").add(
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
