package com.servinte.axioma.orm;

// Generated Nov 23, 2010 10:55:28 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistTarjFinancieraComision.
 * @see com.servinte.axioma.orm.HistTarjFinancieraComision
 * @author Hibernate Tools
 */
public class HistTarjFinancieraComisionHome {

	private static final Log log = LogFactory
			.getLog(HistTarjFinancieraComisionHome.class);

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

	public void persist(HistTarjFinancieraComision transientInstance) {
		log.debug("persisting HistTarjFinancieraComision instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistTarjFinancieraComision instance) {
		log.debug("attaching dirty HistTarjFinancieraComision instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistTarjFinancieraComision instance) {
		log.debug("attaching clean HistTarjFinancieraComision instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistTarjFinancieraComision persistentInstance) {
		log.debug("deleting HistTarjFinancieraComision instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistTarjFinancieraComision merge(
			HistTarjFinancieraComision detachedInstance) {
		log.debug("merging HistTarjFinancieraComision instance");
		try {
			HistTarjFinancieraComision result = (HistTarjFinancieraComision) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistTarjFinancieraComision findById(long id) {
		log.debug("getting HistTarjFinancieraComision instance with id: " + id);
		try {
			HistTarjFinancieraComision instance = (HistTarjFinancieraComision) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.HistTarjFinancieraComision",
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

	public List findByExample(HistTarjFinancieraComision instance) {
		log.debug("finding HistTarjFinancieraComision instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistTarjFinancieraComision").add(
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
