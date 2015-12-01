package com.servinte.axioma.orm;

import com.servinte.axioma.hibernate.HibernateUtil;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import com.servinte.axioma.hibernate.HibernateUtil;
import static org.hibernate.criterion.Example.create;

/**
 * Home object for domain model class PresupuestoIntervencion.
 * @see com.servinte.axioma.orm.PresupuestoIntervencion
 * @author Hibernate Tools
 */
public class PresupuestoIntervencionHome {

	private static final Log log = LogFactory
			.getLog(PresupuestoIntervencionHome.class);

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

	public void persist(PresupuestoIntervencion transientInstance) {
		log.debug("persisting PresupuestoIntervencion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresupuestoIntervencion instance) {
		log.debug("attaching dirty PresupuestoIntervencion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresupuestoIntervencion instance) {
		log.debug("attaching clean PresupuestoIntervencion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresupuestoIntervencion persistentInstance) {
		log.debug("deleting PresupuestoIntervencion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresupuestoIntervencion merge(
			PresupuestoIntervencion detachedInstance) {
		log.debug("merging PresupuestoIntervencion instance");
		try {
			PresupuestoIntervencion result = (PresupuestoIntervencion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresupuestoIntervencion findById(
			com.servinte.axioma.orm.PresupuestoIntervencionId id) {
		log.debug("getting PresupuestoIntervencion instance with id: " + id);
		try {
			PresupuestoIntervencion instance = (PresupuestoIntervencion) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresupuestoIntervencion",
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

	public List<PresupuestoIntervencion> findByExample(
			PresupuestoIntervencion instance) {
		log.debug("finding PresupuestoIntervencion instance by example");
		try {
			List<PresupuestoIntervencion> results = (List<PresupuestoIntervencion>) sessionFactory
					.getCurrentSession().createCriteria(
							"com.servinte.axioma.orm.PresupuestoIntervencion")
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
