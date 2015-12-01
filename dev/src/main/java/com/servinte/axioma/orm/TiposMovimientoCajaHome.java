package com.servinte.axioma.orm;

import com.servinte.axioma.hibernate.HibernateUtil;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class TiposMovimientoCaja.
 * @see com.servinte.axioma.orm.TiposMovimientoCaja
 * @author Hibernate Tools
 */
public class TiposMovimientoCajaHome {

	private static final Log log = LogFactory
			.getLog(TiposMovimientoCajaHome.class);

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

	public void persist(TiposMovimientoCaja transientInstance) {
		log.debug("persisting TiposMovimientoCaja instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposMovimientoCaja instance) {
		log.debug("attaching dirty TiposMovimientoCaja instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposMovimientoCaja instance) {
		log.debug("attaching clean TiposMovimientoCaja instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposMovimientoCaja persistentInstance) {
		log.debug("deleting TiposMovimientoCaja instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposMovimientoCaja merge(TiposMovimientoCaja detachedInstance) {
		log.debug("merging TiposMovimientoCaja instance");
		try {
			TiposMovimientoCaja result = (TiposMovimientoCaja) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposMovimientoCaja findById(int id) {
		log.debug("getting TiposMovimientoCaja instance with id: " + id);
		try {
			TiposMovimientoCaja instance = (TiposMovimientoCaja) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposMovimientoCaja", id);
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

	public List findByExample(TiposMovimientoCaja instance) {
		log.debug("finding TiposMovimientoCaja instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TiposMovimientoCaja").add(
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
