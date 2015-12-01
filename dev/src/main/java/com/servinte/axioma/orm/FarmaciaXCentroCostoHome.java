package com.servinte.axioma.orm;

// Generated 16/01/2012 07:10:27 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class FarmaciaXCentroCosto.
 * @see com.servinte.axioma.orm.FarmaciaXCentroCosto
 * @author Hibernate Tools
 */
public class FarmaciaXCentroCostoHome {

	private static final Log log = LogFactory
			.getLog(FarmaciaXCentroCostoHome.class);

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

	public void persist(FarmaciaXCentroCosto transientInstance) {
		log.debug("persisting FarmaciaXCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(FarmaciaXCentroCosto instance) {
		log.debug("attaching dirty FarmaciaXCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(FarmaciaXCentroCosto instance) {
		log.debug("attaching clean FarmaciaXCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(FarmaciaXCentroCosto persistentInstance) {
		log.debug("deleting FarmaciaXCentroCosto instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public FarmaciaXCentroCosto merge(FarmaciaXCentroCosto detachedInstance) {
		log.debug("merging FarmaciaXCentroCosto instance");
		try {
			FarmaciaXCentroCosto result = (FarmaciaXCentroCosto) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public FarmaciaXCentroCosto findById(int id) {
		log.debug("getting FarmaciaXCentroCosto instance with id: " + id);
		try {
			FarmaciaXCentroCosto instance = (FarmaciaXCentroCosto) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.FarmaciaXCentroCosto", id);
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

	public List findByExample(FarmaciaXCentroCosto instance) {
		log.debug("finding FarmaciaXCentroCosto instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.FarmaciaXCentroCosto")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
