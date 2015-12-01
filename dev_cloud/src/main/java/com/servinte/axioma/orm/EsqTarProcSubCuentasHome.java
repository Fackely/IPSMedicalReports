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
 * Home object for domain model class EsqTarProcSubCuentas.
 * @see com.servinte.axioma.orm.EsqTarProcSubCuentas
 * @author Hibernate Tools
 */
public class EsqTarProcSubCuentasHome {

	private static final Log log = LogFactory
			.getLog(EsqTarProcSubCuentasHome.class);

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

	public void persist(EsqTarProcSubCuentas transientInstance) {
		log.debug("persisting EsqTarProcSubCuentas instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(EsqTarProcSubCuentas instance) {
		log.debug("attaching dirty EsqTarProcSubCuentas instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EsqTarProcSubCuentas instance) {
		log.debug("attaching clean EsqTarProcSubCuentas instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(EsqTarProcSubCuentas persistentInstance) {
		log.debug("deleting EsqTarProcSubCuentas instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EsqTarProcSubCuentas merge(EsqTarProcSubCuentas detachedInstance) {
		log.debug("merging EsqTarProcSubCuentas instance");
		try {
			EsqTarProcSubCuentas result = (EsqTarProcSubCuentas) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public EsqTarProcSubCuentas findById(long id) {
		log.debug("getting EsqTarProcSubCuentas instance with id: " + id);
		try {
			EsqTarProcSubCuentas instance = (EsqTarProcSubCuentas) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.EsqTarProcSubCuentas", id);
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

	public List findByExample(EsqTarProcSubCuentas instance) {
		log.debug("finding EsqTarProcSubCuentas instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.EsqTarProcSubCuentas").add(
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
