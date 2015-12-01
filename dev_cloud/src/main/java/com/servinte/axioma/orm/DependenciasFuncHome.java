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
 * Home object for domain model class DependenciasFunc.
 * @see com.servinte.axioma.orm.DependenciasFunc
 * @author Hibernate Tools
 */
public class DependenciasFuncHome {

	private static final Log log = LogFactory
			.getLog(DependenciasFuncHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(DependenciasFunc transientInstance) {
		log.debug("persisting DependenciasFunc instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DependenciasFunc instance) {
		log.debug("attaching dirty DependenciasFunc instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DependenciasFunc instance) {
		log.debug("attaching clean DependenciasFunc instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DependenciasFunc persistentInstance) {
		log.debug("deleting DependenciasFunc instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DependenciasFunc merge(DependenciasFunc detachedInstance) {
		log.debug("merging DependenciasFunc instance");
		try {
			DependenciasFunc result = (DependenciasFunc) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DependenciasFunc findById(
			com.servinte.axioma.orm.DependenciasFuncId id) {
		log.debug("getting DependenciasFunc instance with id: " + id);
		try {
			DependenciasFunc instance = (DependenciasFunc) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DependenciasFunc", id);
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

	public List<DependenciasFunc> findByExample(DependenciasFunc instance) {
		log.debug("finding DependenciasFunc instance by example");
		try {
			List<DependenciasFunc> results = (List<DependenciasFunc>) sessionFactory
					.getCurrentSession().createCriteria(
							"com.servinte.axioma.orm.DependenciasFunc").add(
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
