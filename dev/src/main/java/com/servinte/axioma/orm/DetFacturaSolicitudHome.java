package com.servinte.axioma.orm;

// Generated 1/06/2011 12:41:14 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetFacturaSolicitud.
 * @see com.servinte.axioma.orm.DetFacturaSolicitud
 * @author Hibernate Tools
 */
public class DetFacturaSolicitudHome {

	private static final Log log = LogFactory
			.getLog(DetFacturaSolicitudHome.class);

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

	public void persist(DetFacturaSolicitud transientInstance) {
		log.debug("persisting DetFacturaSolicitud instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetFacturaSolicitud instance) {
		log.debug("attaching dirty DetFacturaSolicitud instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetFacturaSolicitud instance) {
		log.debug("attaching clean DetFacturaSolicitud instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetFacturaSolicitud persistentInstance) {
		log.debug("deleting DetFacturaSolicitud instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetFacturaSolicitud merge(DetFacturaSolicitud detachedInstance) {
		log.debug("merging DetFacturaSolicitud instance");
		try {
			DetFacturaSolicitud result = (DetFacturaSolicitud) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetFacturaSolicitud findById(int id) {
		log.debug("getting DetFacturaSolicitud instance with id: " + id);
		try {
			DetFacturaSolicitud instance = (DetFacturaSolicitud) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetFacturaSolicitud", id);
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

	public List findByExample(DetFacturaSolicitud instance) {
		log.debug("finding DetFacturaSolicitud instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.DetFacturaSolicitud")
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
