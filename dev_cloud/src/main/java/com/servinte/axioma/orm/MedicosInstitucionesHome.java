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
 * Home object for domain model class MedicosInstituciones.
 * @see com.servinte.axioma.orm.MedicosInstituciones
 * @author Hibernate Tools
 */
public class MedicosInstitucionesHome {

	private static final Log log = LogFactory
			.getLog(MedicosInstitucionesHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(MedicosInstituciones transientInstance) {
		log.debug("persisting MedicosInstituciones instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(MedicosInstituciones instance) {
		log.debug("attaching dirty MedicosInstituciones instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(MedicosInstituciones instance) {
		log.debug("attaching clean MedicosInstituciones instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(MedicosInstituciones persistentInstance) {
		log.debug("deleting MedicosInstituciones instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public MedicosInstituciones merge(MedicosInstituciones detachedInstance) {
		log.debug("merging MedicosInstituciones instance");
		try {
			MedicosInstituciones result = (MedicosInstituciones) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public MedicosInstituciones findById(
			com.servinte.axioma.orm.MedicosInstitucionesId id) {
		log.debug("getting MedicosInstituciones instance with id: " + id);
		try {
			MedicosInstituciones instance = (MedicosInstituciones) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.MedicosInstituciones", id);
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

	public List<MedicosInstituciones> findByExample(
			MedicosInstituciones instance) {
		log.debug("finding MedicosInstituciones instance by example");
		try {
			List<MedicosInstituciones> results = (List<MedicosInstituciones>) sessionFactory
					.getCurrentSession().createCriteria(
							"com.servinte.axioma.orm.MedicosInstituciones")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
