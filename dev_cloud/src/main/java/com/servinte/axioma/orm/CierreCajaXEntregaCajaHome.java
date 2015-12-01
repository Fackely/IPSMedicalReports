package com.servinte.axioma.orm;

// Generated Aug 11, 2010 9:02:12 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CierreCajaXEntregaCaja.
 * @see com.servinte.axioma.orm.CierreCajaXEntregaCaja
 * @author Hibernate Tools
 */
public class CierreCajaXEntregaCajaHome {

	private static final Log log = LogFactory
			.getLog(CierreCajaXEntregaCajaHome.class);

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

	public void persist(CierreCajaXEntregaCaja transientInstance) {
		log.debug("persisting CierreCajaXEntregaCaja instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CierreCajaXEntregaCaja instance) {
		log.debug("attaching dirty CierreCajaXEntregaCaja instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CierreCajaXEntregaCaja instance) {
		log.debug("attaching clean CierreCajaXEntregaCaja instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CierreCajaXEntregaCaja persistentInstance) {
		log.debug("deleting CierreCajaXEntregaCaja instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CierreCajaXEntregaCaja merge(CierreCajaXEntregaCaja detachedInstance) {
		log.debug("merging CierreCajaXEntregaCaja instance");
		try {
			CierreCajaXEntregaCaja result = (CierreCajaXEntregaCaja) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CierreCajaXEntregaCaja findById(long id) {
		log.debug("getting CierreCajaXEntregaCaja instance with id: " + id);
		try {
			CierreCajaXEntregaCaja instance = (CierreCajaXEntregaCaja) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.CierreCajaXEntregaCaja",
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

	public List findByExample(CierreCajaXEntregaCaja instance) {
		log.debug("finding CierreCajaXEntregaCaja instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CierreCajaXEntregaCaja").add(
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
