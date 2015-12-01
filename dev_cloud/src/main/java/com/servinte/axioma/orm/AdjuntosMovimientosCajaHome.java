package com.servinte.axioma.orm;

// Generated Jul 25, 2010 3:49:25 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

import util.UtilidadBD;

/**
 * Home object for domain model class AdjuntosMovimientosCaja.
 * @see com.servinte.axioma.orm.AdjuntosMovimientosCaja
 * @author Hibernate Tools
 */
public class AdjuntosMovimientosCajaHome {

	private static final Log log = LogFactory
			.getLog(AdjuntosMovimientosCajaHome.class);

	protected SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(AdjuntosMovimientosCaja transientInstance) {
		log.debug("persisting AdjuntosMovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AdjuntosMovimientosCaja instance) {
		log.debug("attaching dirty AdjuntosMovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AdjuntosMovimientosCaja instance) {
		log.debug("attaching clean AdjuntosMovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AdjuntosMovimientosCaja persistentInstance) {
		log.debug("deleting AdjuntosMovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AdjuntosMovimientosCaja merge(
			AdjuntosMovimientosCaja detachedInstance) {
		log.debug("merging AdjuntosMovimientosCaja instance");
		try {
			AdjuntosMovimientosCaja result = (AdjuntosMovimientosCaja) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AdjuntosMovimientosCaja findById(long id) {
		log.debug("getting AdjuntosMovimientosCaja instance with id: " + id);
		try {
			AdjuntosMovimientosCaja instance = (AdjuntosMovimientosCaja) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AdjuntosMovimientosCaja",
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

	public List findByExample(AdjuntosMovimientosCaja instance) {
		log.debug("finding AdjuntosMovimientosCaja instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AdjuntosMovimientosCaja").add(
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
