package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:55 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TipoCuentaBancaria.
 * @see com.servinte.axioma.orm.TipoCuentaBancaria
 * @author Hibernate Tools
 */
public class TipoCuentaBancariaHome {

	private static final Log log = LogFactory
			.getLog(TipoCuentaBancariaHome.class);

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

	public void persist(TipoCuentaBancaria transientInstance) {
		log.debug("persisting TipoCuentaBancaria instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TipoCuentaBancaria instance) {
		log.debug("attaching dirty TipoCuentaBancaria instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TipoCuentaBancaria instance) {
		log.debug("attaching clean TipoCuentaBancaria instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TipoCuentaBancaria persistentInstance) {
		log.debug("deleting TipoCuentaBancaria instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TipoCuentaBancaria merge(TipoCuentaBancaria detachedInstance) {
		log.debug("merging TipoCuentaBancaria instance");
		try {
			TipoCuentaBancaria result = (TipoCuentaBancaria) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TipoCuentaBancaria findById(char id) {
		log.debug("getting TipoCuentaBancaria instance with id: " + id);
		try {
			TipoCuentaBancaria instance = (TipoCuentaBancaria) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TipoCuentaBancaria", id);
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

	public List findByExample(TipoCuentaBancaria instance) {
		log.debug("finding TipoCuentaBancaria instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TipoCuentaBancaria").add(
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
