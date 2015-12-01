package com.servinte.axioma.orm;

// Generated Jun 21, 2010 11:07:52 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class SolicitudCambioServicio.
 * @see com.servinte.axioma.orm.SolicitudCambioServicio
 * @author Hibernate Tools
 */
public class SolicitudCambioServicioHome {

	private static final Log log = LogFactory
			.getLog(SolicitudCambioServicioHome.class);

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

	public void persist(SolicitudCambioServicio transientInstance) {
		log.debug("persisting SolicitudCambioServicio instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SolicitudCambioServicio instance) {
		log.debug("attaching dirty SolicitudCambioServicio instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SolicitudCambioServicio instance) {
		log.debug("attaching clean SolicitudCambioServicio instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SolicitudCambioServicio persistentInstance) {
		log.debug("deleting SolicitudCambioServicio instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SolicitudCambioServicio merge(
			SolicitudCambioServicio detachedInstance) {
		log.debug("merging SolicitudCambioServicio instance");
		try {
			SolicitudCambioServicio result = (SolicitudCambioServicio) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SolicitudCambioServicio findById(int id) {
		log.debug("getting SolicitudCambioServicio instance with id: " + id);
		try {
			SolicitudCambioServicio instance = (SolicitudCambioServicio) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SolicitudCambioServicio",
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

	public List findByExample(SolicitudCambioServicio instance) {
		log.debug("finding SolicitudCambioServicio instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.SolicitudCambioServicio").add(
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
