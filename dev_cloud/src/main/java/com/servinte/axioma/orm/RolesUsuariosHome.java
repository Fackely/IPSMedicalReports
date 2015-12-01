package com.servinte.axioma.orm;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import com.servinte.axioma.hibernate.HibernateUtil;

import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class RolesUsuarios.
 * @see com.servinte.axioma.orm.RolesUsuarios
 * @author Hibernate Tools
 */
public class RolesUsuariosHome {

	private static final Log log = LogFactory.getLog(RolesUsuariosHome.class);

	protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	public void persist(RolesUsuarios transientInstance) {
		log.debug("persisting RolesUsuarios instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}
	
	

	public void attachDirty(RolesUsuarios instance) {
		log.debug("attaching dirty RolesUsuarios instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(RolesUsuarios instance) {
		log.debug("attaching clean RolesUsuarios instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(RolesUsuarios persistentInstance) {
		log.debug("deleting RolesUsuarios instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public RolesUsuarios merge(RolesUsuarios detachedInstance) {
		log.debug("merging RolesUsuarios instance");
		try {
			RolesUsuarios result = (RolesUsuarios) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public RolesUsuarios findById(com.servinte.axioma.orm.RolesUsuariosId id) {
		log.debug("getting RolesUsuarios instance with id: " + id);
		try {
			RolesUsuarios instance = (RolesUsuarios) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.RolesUsuarios", id);
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

	public List<RolesUsuarios> findByExample(RolesUsuarios instance) {
		log.debug("finding RolesUsuarios instance by example");
		try {
			List<RolesUsuarios> results = (List<RolesUsuarios>) sessionFactory
					.getCurrentSession().createCriteria(
							"com.servinte.axioma.orm.RolesUsuarios").add(
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
