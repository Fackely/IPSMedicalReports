package com.servinte.axioma.orm;

// Generated Aug 25, 2010 4:21:14 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetalleMontoGeneral.
 * @see com.servinte.axioma.orm.DetalleMontoGeneral
 * @author Hibernate Tools
 */
public class DetalleMontoGeneralHome {

	private static final Log log = LogFactory
			.getLog(DetalleMontoGeneralHome.class);

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

	public void persist(DetalleMontoGeneral transientInstance) {
		log.debug("persisting DetalleMontoGeneral instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetalleMontoGeneral instance) {
		log.debug("attaching dirty DetalleMontoGeneral instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetalleMontoGeneral instance) {
		log.debug("attaching clean DetalleMontoGeneral instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetalleMontoGeneral persistentInstance) {
		log.debug("deleting DetalleMontoGeneral instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetalleMontoGeneral merge(DetalleMontoGeneral detachedInstance) {
		log.debug("merging DetalleMontoGeneral instance");
		try {
			DetalleMontoGeneral result = (DetalleMontoGeneral) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetalleMontoGeneral findById(int id) {
		log.debug("getting DetalleMontoGeneral instance with id: " + id);
		try {
			DetalleMontoGeneral instance = (DetalleMontoGeneral) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DetalleMontoGeneral", id);
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

	public List findByExample(DetalleMontoGeneral instance) {
		log.debug("finding DetalleMontoGeneral instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetalleMontoGeneral").add(
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
