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
 * Home object for domain model class AceptacionTrasladoCaja.
 * @see com.servinte.axioma.orm.AceptacionTrasladoCaja
 * @author Hibernate Tools
 */
public class AceptacionTrasladoCajaHome {

	private static final Log log = LogFactory
			.getLog(AceptacionTrasladoCajaHome.class);

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

	public void persist(AceptacionTrasladoCaja transientInstance) {
		log.debug("persisting AceptacionTrasladoCaja instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AceptacionTrasladoCaja instance) {
		log.debug("attaching dirty AceptacionTrasladoCaja instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AceptacionTrasladoCaja instance) {
		log.debug("attaching clean AceptacionTrasladoCaja instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AceptacionTrasladoCaja persistentInstance) {
		log.debug("deleting AceptacionTrasladoCaja instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AceptacionTrasladoCaja merge(AceptacionTrasladoCaja detachedInstance) {
		log.debug("merging AceptacionTrasladoCaja instance");
		try {
			AceptacionTrasladoCaja result = (AceptacionTrasladoCaja) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AceptacionTrasladoCaja findById(long id) {
		log.debug("getting AceptacionTrasladoCaja instance with id: " + id);
		try {
			AceptacionTrasladoCaja instance = (AceptacionTrasladoCaja) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AceptacionTrasladoCaja",
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

	public List findByExample(AceptacionTrasladoCaja instance) {
		log.debug("finding AceptacionTrasladoCaja instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AceptacionTrasladoCaja").add(
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
