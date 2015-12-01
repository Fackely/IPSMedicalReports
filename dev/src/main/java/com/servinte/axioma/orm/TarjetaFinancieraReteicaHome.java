package com.servinte.axioma.orm;

// Generated Jun 9, 2010 3:43:42 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TarjetaFinancieraReteica.
 * @see com.servinte.axioma.orm.TarjetaFinancieraReteica
 * @author Hibernate Tools
 */
public class TarjetaFinancieraReteicaHome {

	private static final Log log = LogFactory
			.getLog(TarjetaFinancieraReteicaHome.class);

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

	public void persist(TarjetaFinancieraReteica transientInstance) {
		log.debug("persisting TarjetaFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TarjetaFinancieraReteica instance) {
		log.debug("attaching dirty TarjetaFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TarjetaFinancieraReteica instance) {
		log.debug("attaching clean TarjetaFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TarjetaFinancieraReteica persistentInstance) {
		log.debug("deleting TarjetaFinancieraReteica instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TarjetaFinancieraReteica merge(
			TarjetaFinancieraReteica detachedInstance) {
		log.debug("merging TarjetaFinancieraReteica instance");
		try {
			TarjetaFinancieraReteica result = (TarjetaFinancieraReteica) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TarjetaFinancieraReteica findById(long id) {
		log.debug("getting TarjetaFinancieraReteica instance with id: " + id);
		try {
			TarjetaFinancieraReteica instance = (TarjetaFinancieraReteica) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TarjetaFinancieraReteica",
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

	public List findByExample(TarjetaFinancieraReteica instance) {
		log.debug("finding TarjetaFinancieraReteica instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TarjetaFinancieraReteica").add(
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
