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
 * Home object for domain model class HistTarjFinancieraReteica.
 * @see com.servinte.axioma.orm.HistTarjFinancieraReteica
 * @author Hibernate Tools
 */
public class HistTarjFinancieraReteicaHome {

	private static final Log log = LogFactory
			.getLog(HistTarjFinancieraReteicaHome.class);

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

	public void persist(HistTarjFinancieraReteica transientInstance) {
		log.debug("persisting HistTarjFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistTarjFinancieraReteica instance) {
		log.debug("attaching dirty HistTarjFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistTarjFinancieraReteica instance) {
		log.debug("attaching clean HistTarjFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistTarjFinancieraReteica persistentInstance) {
		log.debug("deleting HistTarjFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistTarjFinancieraReteica merge(
			HistTarjFinancieraReteica detachedInstance) {
		log.debug("merging HistTarjFinancieraReteica instance");
		try {
			HistTarjFinancieraReteica result = (HistTarjFinancieraReteica) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistTarjFinancieraReteica findById(long id) {
		log.debug("getting HistTarjFinancieraReteica instance with id: " + id);
		try {
			HistTarjFinancieraReteica instance = (HistTarjFinancieraReteica) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.HistTarjFinancieraReteica",
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

	public List findByExample(HistTarjFinancieraReteica instance) {
		log.debug("finding HistTarjFinancieraReteica instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistTarjFinancieraReteica").add(
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
