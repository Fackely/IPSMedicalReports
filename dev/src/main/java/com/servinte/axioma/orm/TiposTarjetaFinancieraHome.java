package com.servinte.axioma.orm;

// Generated 15/04/2010 03:20:53 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TiposTarjetaFinanciera.
 * @see com.servinte.axioma.orm.TiposTarjetaFinanciera
 * @author Hibernate Tools
 */
public class TiposTarjetaFinancieraHome {

	private static final Log log = LogFactory
			.getLog(TiposTarjetaFinancieraHome.class);

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

	public void persist(TiposTarjetaFinanciera transientInstance) {
		log.debug("persisting TiposTarjetaFinanciera instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposTarjetaFinanciera instance) {
		log.debug("attaching dirty TiposTarjetaFinanciera instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposTarjetaFinanciera instance) {
		log.debug("attaching clean TiposTarjetaFinanciera instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposTarjetaFinanciera persistentInstance) {
		log.debug("deleting TiposTarjetaFinanciera instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposTarjetaFinanciera merge(TiposTarjetaFinanciera detachedInstance) {
		log.debug("merging TiposTarjetaFinanciera instance");
		try {
			TiposTarjetaFinanciera result = (TiposTarjetaFinanciera) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposTarjetaFinanciera findById(char id) {
		log.debug("getting TiposTarjetaFinanciera instance with id: " + id);
		try {
			TiposTarjetaFinanciera instance = (TiposTarjetaFinanciera) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposTarjetaFinanciera",
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

	public List findByExample(TiposTarjetaFinanciera instance) {
		log.debug("finding TiposTarjetaFinanciera instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TiposTarjetaFinanciera").add(
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
