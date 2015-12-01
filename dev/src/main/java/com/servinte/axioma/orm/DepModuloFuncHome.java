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
 * Home object for domain model class DepModuloFunc.
 * @see com.servinte.axioma.orm.DepModuloFunc
 * @author Hibernate Tools
 */
public class DepModuloFuncHome {

	private static final Log log = LogFactory.getLog(DepModuloFuncHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(DepModuloFunc transientInstance) {
		log.debug("persisting DepModuloFunc instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DepModuloFunc instance) {
		log.debug("attaching dirty DepModuloFunc instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DepModuloFunc instance) {
		log.debug("attaching clean DepModuloFunc instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DepModuloFunc persistentInstance) {
		log.debug("deleting DepModuloFunc instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DepModuloFunc merge(DepModuloFunc detachedInstance) {
		log.debug("merging DepModuloFunc instance");
		try {
			DepModuloFunc result = (DepModuloFunc) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DepModuloFunc findById(com.servinte.axioma.orm.DepModuloFuncId id) {
		log.debug("getting DepModuloFunc instance with id: " + id);
		try {
			DepModuloFunc instance = (DepModuloFunc) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.DepModuloFunc", id);
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

	public List<DepModuloFunc> findByExample(DepModuloFunc instance) {
		log.debug("finding DepModuloFunc instance by example");
		try {
			List<DepModuloFunc> results = (List<DepModuloFunc>) sessionFactory
					.getCurrentSession().createCriteria(
							"com.servinte.axioma.orm.DepModuloFunc").add(
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
