package com.servinte.axioma.orm;

// Generated Mar 1, 2011 12:13:16 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class EsqTarProcedimientoContrato.
 * @see com.servinte.axioma.orm.EsqTarProcedimientoContrato
 * @author Hibernate Tools
 */
public class EsqTarProcedimientoContratoHome {

	private static final Log log = LogFactory
			.getLog(EsqTarProcedimientoContratoHome.class);

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

	public void persist(EsqTarProcedimientoContrato transientInstance) {
		log.debug("persisting EsqTarProcedimientoContrato instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(EsqTarProcedimientoContrato instance) {
		log.debug("attaching dirty EsqTarProcedimientoContrato instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(EsqTarProcedimientoContrato instance) {
		log.debug("attaching clean EsqTarProcedimientoContrato instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(EsqTarProcedimientoContrato persistentInstance) {
		log.debug("deleting EsqTarProcedimientoContrato instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public EsqTarProcedimientoContrato merge(
			EsqTarProcedimientoContrato detachedInstance) {
		log.debug("merging EsqTarProcedimientoContrato instance");
		try {
			EsqTarProcedimientoContrato result = (EsqTarProcedimientoContrato) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public EsqTarProcedimientoContrato findById(long id) {
		log
				.debug("getting EsqTarProcedimientoContrato instance with id: "
						+ id);
		try {
			EsqTarProcedimientoContrato instance = (EsqTarProcedimientoContrato) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.EsqTarProcedimientoContrato",
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

	public List findByExample(EsqTarProcedimientoContrato instance) {
		log.debug("finding EsqTarProcedimientoContrato instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.EsqTarProcedimientoContrato").add(
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
