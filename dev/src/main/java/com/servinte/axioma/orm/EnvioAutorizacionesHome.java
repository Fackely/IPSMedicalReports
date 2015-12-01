package com.servinte.axioma.orm;

// Generated May 3, 2010 4:30:56 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class EnvioAutorizaciones.
 * @see com.servinte.axioma.orm.EnvioAutorizaciones
 * @author Hibernate Tools
 */
public class EnvioAutorizacionesHome {

	private static final Log log = LogFactory
			.getLog(EnvioAutorizacionesHome.class);

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

	public void persist(EnvioAutorizaciones transientInstance) {
		log.debug("persisting EnvioAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(EnvioAutorizaciones instance) {
		log.debug("attaching dirty EnvioAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EnvioAutorizaciones instance) {
		log.debug("attaching clean EnvioAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(EnvioAutorizaciones persistentInstance) {
		log.debug("deleting EnvioAutorizaciones instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EnvioAutorizaciones merge(EnvioAutorizaciones detachedInstance) {
		log.debug("merging EnvioAutorizaciones instance");
		try {
			EnvioAutorizaciones result = (EnvioAutorizaciones) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public EnvioAutorizaciones findById(long id) {
		log.debug("getting EnvioAutorizaciones instance with id: " + id);
		try {
			EnvioAutorizaciones instance = (EnvioAutorizaciones) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.EnvioAutorizaciones", id);
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

	public List findByExample(EnvioAutorizaciones instance) {
		log.debug("finding EnvioAutorizaciones instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.EnvioAutorizaciones").add(
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
