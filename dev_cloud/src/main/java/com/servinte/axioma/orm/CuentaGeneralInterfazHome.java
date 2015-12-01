package com.servinte.axioma.orm;

// Generated Apr 26, 2010 1:53:58 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CuentaGeneralInterfaz.
 * @see com.servinte.axioma.orm.CuentaGeneralInterfaz
 * @author Hibernate Tools
 */
public class CuentaGeneralInterfazHome {

	private static final Log log = LogFactory
			.getLog(CuentaGeneralInterfazHome.class);

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

	public void persist(CuentaGeneralInterfaz transientInstance) {
		log.debug("persisting CuentaGeneralInterfaz instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CuentaGeneralInterfaz instance) {
		log.debug("attaching dirty CuentaGeneralInterfaz instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CuentaGeneralInterfaz instance) {
		log.debug("attaching clean CuentaGeneralInterfaz instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CuentaGeneralInterfaz persistentInstance) {
		log.debug("deleting CuentaGeneralInterfaz instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CuentaGeneralInterfaz merge(CuentaGeneralInterfaz detachedInstance) {
		log.debug("merging CuentaGeneralInterfaz instance");
		try {
			CuentaGeneralInterfaz result = (CuentaGeneralInterfaz) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CuentaGeneralInterfaz findById(long id) {
		log.debug("getting CuentaGeneralInterfaz instance with id: " + id);
		try {
			CuentaGeneralInterfaz instance = (CuentaGeneralInterfaz) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.CuentaGeneralInterfaz", id);
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

	public List findByExample(CuentaGeneralInterfaz instance) {
		log.debug("finding CuentaGeneralInterfaz instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CuentaGeneralInterfaz").add(
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
