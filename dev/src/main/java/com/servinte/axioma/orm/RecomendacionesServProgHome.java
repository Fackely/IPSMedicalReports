package com.servinte.axioma.orm;

// Generated Jun 8, 2010 5:26:03 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class RecomendacionesServProg.
 * @see com.servinte.axioma.orm.RecomendacionesServProg
 * @author Hibernate Tools
 */
public class RecomendacionesServProgHome {

	private static final Log log = LogFactory.getLog(RecomendacionesServProgHome.class);

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

	public void persist(RecomendacionesServProg transientInstance) {
		log.debug("persisting RecomendacionesServProg instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(RecomendacionesServProg instance) {
		log.debug("attaching dirty RecomendacionesServProg instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RecomendacionesServProg instance) {
		log.debug("attaching clean RecomendacionesServProg instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RecomendacionesServProg persistentInstance) {
		log.debug("deleting RecomendacionesServProg instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RecomendacionesServProg merge(
			RecomendacionesServProg detachedInstance) {
		log.debug("merging RecomendacionesServProg instance");
		try {
			RecomendacionesServProg result = (RecomendacionesServProg) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RecomendacionesServProg findById(int id) {
		log.debug("getting RecomendacionesServProg instance with id: " + id);
		try {
			RecomendacionesServProg instance = (RecomendacionesServProg) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.RecomendacionesServProg",
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

	public List findByExample(RecomendacionesServProg instance) {
		log.debug("finding RecomendacionesServProg instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.RecomendacionesServProg").add(
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
