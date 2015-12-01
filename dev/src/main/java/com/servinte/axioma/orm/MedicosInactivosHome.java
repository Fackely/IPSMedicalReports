package com.servinte.axioma.orm;


import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import com.servinte.axioma.hibernate.HibernateUtil;

import com.servinte.axioma.hibernate.HibernateUtil;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class MedicosInactivos.
 * @see com.servinte.axioma.orm.MedicosInactivos
 * @author Hibernate Tools
 */
public class MedicosInactivosHome {

	private static final Log log = LogFactory
			.getLog(MedicosInactivosHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(MedicosInactivos transientInstance) {
		log.debug("persisting MedicosInactivos instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(MedicosInactivos instance) {
		log.debug("attaching dirty MedicosInactivos instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MedicosInactivos instance) {
		log.debug("attaching clean MedicosInactivos instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(MedicosInactivos persistentInstance) {
		log.debug("deleting MedicosInactivos instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MedicosInactivos merge(MedicosInactivos detachedInstance) {
		log.debug("merging MedicosInactivos instance");
		try {
			MedicosInactivos result = (MedicosInactivos) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MedicosInactivos findById(
			com.servinte.axioma.orm.MedicosInactivosId id) {
		log.debug("getting MedicosInactivos instance with id: " + id);
		try {
			MedicosInactivos instance = (MedicosInactivos) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.MedicosInactivos", id);
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

	public List<MedicosInactivos> findByExample(MedicosInactivos instance) {
		log.debug("finding MedicosInactivos instance by example");
		try {
			List<MedicosInactivos> results = (List<MedicosInactivos>) sessionFactory
					.getCurrentSession().createCriteria(
							"com.servinte.axioma.orm.MedicosInactivos").add(
							create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
