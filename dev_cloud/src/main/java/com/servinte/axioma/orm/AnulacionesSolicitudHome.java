package com.servinte.axioma.orm;

// Generated 1/06/2011 10:54:46 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AnulacionesSolicitud.
 * @see com.servinte.axioma.orm.AnulacionesSolicitud
 * @author Hibernate Tools
 */
public class AnulacionesSolicitudHome {

	private static final Log log = LogFactory
			.getLog(AnulacionesSolicitudHome.class);

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

	public void persist(AnulacionesSolicitud transientInstance) {
		log.debug("persisting AnulacionesSolicitud instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AnulacionesSolicitud instance) {
		log.debug("attaching dirty AnulacionesSolicitud instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AnulacionesSolicitud instance) {
		log.debug("attaching clean AnulacionesSolicitud instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AnulacionesSolicitud persistentInstance) {
		log.debug("deleting AnulacionesSolicitud instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AnulacionesSolicitud merge(AnulacionesSolicitud detachedInstance) {
		log.debug("merging AnulacionesSolicitud instance");
		try {
			AnulacionesSolicitud result = (AnulacionesSolicitud) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AnulacionesSolicitud findById(int id) {
		log.debug("getting AnulacionesSolicitud instance with id: " + id);
		try {
			AnulacionesSolicitud instance = (AnulacionesSolicitud) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AnulacionesSolicitud", id);
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

	public List findByExample(AnulacionesSolicitud instance) {
		log.debug("finding AnulacionesSolicitud instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.AnulacionesSolicitud")
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
