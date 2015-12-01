package com.servinte.axioma.orm;

// Generated Sep 8, 2010 1:46:04 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class EncaEmiTarjetaCliente.
 * @see com.servinte.axioma.orm.EncaEmiTarjetaCliente
 * @author Hibernate Tools
 */
public class EncaEmiTarjetaClienteHome {

	private static final Log log = LogFactory
			.getLog(EncaEmiTarjetaClienteHome.class);

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

	public void persist(EncaEmiTarjetaCliente transientInstance) {
		log.debug("persisting EncaEmiTarjetaCliente instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(EncaEmiTarjetaCliente instance) {
		log.debug("attaching dirty EncaEmiTarjetaCliente instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EncaEmiTarjetaCliente instance) {
		log.debug("attaching clean EncaEmiTarjetaCliente instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(EncaEmiTarjetaCliente persistentInstance) {
		log.debug("deleting EncaEmiTarjetaCliente instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EncaEmiTarjetaCliente merge(EncaEmiTarjetaCliente detachedInstance) {
		log.debug("merging EncaEmiTarjetaCliente instance");
		try {
			EncaEmiTarjetaCliente result = (EncaEmiTarjetaCliente) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public EncaEmiTarjetaCliente findById(long id) {
		log.debug("getting EncaEmiTarjetaCliente instance with id: " + id);
		try {
			EncaEmiTarjetaCliente instance = (EncaEmiTarjetaCliente) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.EncaEmiTarjetaCliente", id);
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

	public List findByExample(EncaEmiTarjetaCliente instance) {
		log.debug("finding EncaEmiTarjetaCliente instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.EncaEmiTarjetaCliente").add(
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
