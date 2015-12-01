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
 * Home object for domain model class MovimientosCaja.
 * @see com.servinte.axioma.orm.MovimientosCaja
 * @author Hibernate Tools
 */
public class MovimientosCajaHome {

	private static final Log log = LogFactory.getLog(MovimientosCajaHome.class);

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

	public void persist(MovimientosCaja transientInstance) {
		log.debug("persisting MovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(MovimientosCaja instance) {
		log.debug("attaching dirty MovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MovimientosCaja instance) {
		log.debug("attaching clean MovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(MovimientosCaja persistentInstance) {
		log.debug("deleting MovimientosCaja instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MovimientosCaja merge(MovimientosCaja detachedInstance) {
		log.debug("merging MovimientosCaja instance");
		try {
			MovimientosCaja result = (MovimientosCaja) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MovimientosCaja findById(long id) {
		log.debug("getting MovimientosCaja instance with id: " + id);
		try {
			MovimientosCaja instance = (MovimientosCaja) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.MovimientosCaja", id);
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

	public List findByExample(MovimientosCaja instance) {
		log.debug("finding MovimientosCaja instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.MovimientosCaja").add(
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
