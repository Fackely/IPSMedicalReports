package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class UnidadFunCuentaIngCc.
 * @see com.servinte.axioma.orm.UnidadFunCuentaIngCc
 * @author Hibernate Tools
 */
public class UnidadFunCuentaIngCcHome {

	private static final Log log = LogFactory
			.getLog(UnidadFunCuentaIngCcHome.class);

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

	public void persist(UnidadFunCuentaIngCc transientInstance) {
		log.debug("persisting UnidadFunCuentaIngCc instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(UnidadFunCuentaIngCc instance) {
		log.debug("attaching dirty UnidadFunCuentaIngCc instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(UnidadFunCuentaIngCc instance) {
		log.debug("attaching clean UnidadFunCuentaIngCc instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(UnidadFunCuentaIngCc persistentInstance) {
		log.debug("deleting UnidadFunCuentaIngCc instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public UnidadFunCuentaIngCc merge(UnidadFunCuentaIngCc detachedInstance) {
		log.debug("merging UnidadFunCuentaIngCc instance");
		try {
			UnidadFunCuentaIngCc result = (UnidadFunCuentaIngCc) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public UnidadFunCuentaIngCc findById(
			com.servinte.axioma.orm.UnidadFunCuentaIngCcId id) {
		log.debug("getting UnidadFunCuentaIngCc instance with id: " + id);
		try {
			UnidadFunCuentaIngCc instance = (UnidadFunCuentaIngCc) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.UnidadFunCuentaIngCc", id);
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

	public List findByExample(UnidadFunCuentaIngCc instance) {
		log.debug("finding UnidadFunCuentaIngCc instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.UnidadFunCuentaIngCc").add(
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
