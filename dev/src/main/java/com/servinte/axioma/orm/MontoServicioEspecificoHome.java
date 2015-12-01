package com.servinte.axioma.orm;

// Generated Aug 25, 2010 4:29:57 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class MontoServicioEspecifico.
 * @see com.servinte.axioma.orm.MontoServicioEspecifico
 * @author Hibernate Tools
 */
public class MontoServicioEspecificoHome {

	private static final Log log = LogFactory
			.getLog(MontoServicioEspecificoHome.class);

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

	public void persist(MontoServicioEspecifico transientInstance) {
		log.debug("persisting MontoServicioEspecifico instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(MontoServicioEspecifico instance) {
		log.debug("attaching dirty MontoServicioEspecifico instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MontoServicioEspecifico instance) {
		log.debug("attaching clean MontoServicioEspecifico instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(MontoServicioEspecifico persistentInstance) {
		log.debug("deleting MontoServicioEspecifico instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MontoServicioEspecifico merge(
			MontoServicioEspecifico detachedInstance) {
		log.debug("merging MontoServicioEspecifico instance");
		try {
			MontoServicioEspecifico result = (MontoServicioEspecifico) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MontoServicioEspecifico findById(int id) {
		log.debug("getting MontoServicioEspecifico instance with id: " + id);
		try {
			MontoServicioEspecifico instance = (MontoServicioEspecifico) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.MontoServicioEspecifico",
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

	public List findByExample(MontoServicioEspecifico instance) {
		log.debug("finding MontoServicioEspecifico instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.MontoServicioEspecifico").add(
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
