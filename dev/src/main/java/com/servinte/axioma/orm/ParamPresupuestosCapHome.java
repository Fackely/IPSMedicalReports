package com.servinte.axioma.orm;

// Generated 20/05/2011 04:42:56 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ParamPresupuestosCap.
 * @see com.servinte.axioma.orm.ParamPresupuestosCap
 * @author Hibernate Tools
 */
public class ParamPresupuestosCapHome {

	private static final Log log = LogFactory
			.getLog(ParamPresupuestosCapHome.class);

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

	public void persist(ParamPresupuestosCap transientInstance) {
		log.debug("persisting ParamPresupuestosCap instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ParamPresupuestosCap instance) {
		log.debug("attaching dirty ParamPresupuestosCap instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ParamPresupuestosCap instance) {
		log.debug("attaching clean ParamPresupuestosCap instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ParamPresupuestosCap persistentInstance) {
		log.debug("deleting ParamPresupuestosCap instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ParamPresupuestosCap merge(ParamPresupuestosCap detachedInstance) {
		log.debug("merging ParamPresupuestosCap instance");
		try {
			ParamPresupuestosCap result = (ParamPresupuestosCap) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ParamPresupuestosCap findById(long id) {
		log.debug("getting ParamPresupuestosCap instance with id: " + id);
		try {
			ParamPresupuestosCap instance = (ParamPresupuestosCap) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ParamPresupuestosCap", id);
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

	public List findByExample(ParamPresupuestosCap instance) {
		log.debug("finding ParamPresupuestosCap instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.ParamPresupuestosCap")
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
