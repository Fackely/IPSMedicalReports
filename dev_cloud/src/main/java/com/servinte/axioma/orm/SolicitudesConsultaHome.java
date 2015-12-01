package com.servinte.axioma.orm;

// Generated 23/06/2012 05:39:07 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class SolicitudesConsulta.
 * @see com.servinte.axioma.orm.SolicitudesConsulta
 * @author Hibernate Tools
 */
public class SolicitudesConsultaHome {

	private static final Log log = LogFactory
			.getLog(SolicitudesConsultaHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

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

	public void persist(SolicitudesConsulta transientInstance) {
		log.debug("persisting SolicitudesConsulta instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SolicitudesConsulta instance) {
		log.debug("attaching dirty SolicitudesConsulta instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SolicitudesConsulta instance) {
		log.debug("attaching clean SolicitudesConsulta instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SolicitudesConsulta persistentInstance) {
		log.debug("deleting SolicitudesConsulta instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SolicitudesConsulta merge(SolicitudesConsulta detachedInstance) {
		log.debug("merging SolicitudesConsulta instance");
		try {
			SolicitudesConsulta result = (SolicitudesConsulta) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SolicitudesConsulta findById(int id) {
		log.debug("getting SolicitudesConsulta instance with id: " + id);
		try {
			SolicitudesConsulta instance = (SolicitudesConsulta) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SolicitudesConsulta", id);
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

	public List findByExample(SolicitudesConsulta instance) {
		log.debug("finding SolicitudesConsulta instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.SolicitudesConsulta")
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
