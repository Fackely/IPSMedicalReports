package com.servinte.axioma.orm;

// Generated Nov 12, 2010 4:08:18 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class CuentaInterfazEmpresInsti.
 * @see com.servinte.axioma.orm.CuentaInterfazEmpresInsti
 * @author Hibernate Tools
 */
public class CuentaInterfazEmpresInstiHome {

	private static final Log log = LogFactory
			.getLog(CuentaInterfazEmpresInstiHome.class);

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

	public void persist(CuentaInterfazEmpresInsti transientInstance) {
		log.debug("persisting CuentaInterfazEmpresInsti instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(CuentaInterfazEmpresInsti instance) {
		log.debug("attaching dirty CuentaInterfazEmpresInsti instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(CuentaInterfazEmpresInsti instance) {
		log.debug("attaching clean CuentaInterfazEmpresInsti instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(CuentaInterfazEmpresInsti persistentInstance) {
		log.debug("deleting CuentaInterfazEmpresInsti instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public CuentaInterfazEmpresInsti merge(
			CuentaInterfazEmpresInsti detachedInstance) {
		log.debug("merging CuentaInterfazEmpresInsti instance");
		try {
			CuentaInterfazEmpresInsti result = (CuentaInterfazEmpresInsti) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public CuentaInterfazEmpresInsti findById(long id) {
		log.debug("getting CuentaInterfazEmpresInsti instance with id: " + id);
		try {
			CuentaInterfazEmpresInsti instance = (CuentaInterfazEmpresInsti) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.CuentaInterfazEmpresInsti",
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

	public List findByExample(CuentaInterfazEmpresInsti instance) {
		log.debug("finding CuentaInterfazEmpresInsti instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.CuentaInterfazEmpresInsti").add(
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
