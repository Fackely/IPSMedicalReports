package com.servinte.axioma.orm;

// Generated 14/03/2012 10:54:35 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TiposSolicitud.
 * @see com.servinte.axioma.orm.TiposSolicitud
 * @author Hibernate Tools
 */
public class TiposSolicitudHome {

	private static final Log log = LogFactory.getLog(TiposSolicitudHome.class);

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

	public void persist(TiposSolicitud transientInstance) {
		log.debug("persisting TiposSolicitud instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposSolicitud instance) {
		log.debug("attaching dirty TiposSolicitud instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposSolicitud instance) {
		log.debug("attaching clean TiposSolicitud instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposSolicitud persistentInstance) {
		log.debug("deleting TiposSolicitud instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposSolicitud merge(TiposSolicitud detachedInstance) {
		log.debug("merging TiposSolicitud instance");
		try {
			TiposSolicitud result = (TiposSolicitud) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposSolicitud findById(int id) {
		log.debug("getting TiposSolicitud instance with id: " + id);
		try {
			TiposSolicitud instance = (TiposSolicitud) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposSolicitud", id);
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

	public List findByExample(TiposSolicitud instance) {
		log.debug("finding TiposSolicitud instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.TiposSolicitud")
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
