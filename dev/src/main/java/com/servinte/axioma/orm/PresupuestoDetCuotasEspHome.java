package com.servinte.axioma.orm;

// Generated Jul 27, 2010 11:58:55 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PresupuestoDetCuotasEsp.
 * @see com.servinte.axioma.orm.PresupuestoDetCuotasEsp
 * @author Hibernate Tools
 */
public class PresupuestoDetCuotasEspHome {

	private static final Log log = LogFactory
			.getLog(PresupuestoDetCuotasEspHome.class);

	private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

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

	public void persist(PresupuestoDetCuotasEsp transientInstance) {
		log.debug("persisting PresupuestoDetCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresupuestoDetCuotasEsp instance) {
		log.debug("attaching dirty PresupuestoDetCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresupuestoDetCuotasEsp instance) {
		log.debug("attaching clean PresupuestoDetCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresupuestoDetCuotasEsp persistentInstance) {
		log.debug("deleting PresupuestoDetCuotasEsp instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresupuestoDetCuotasEsp merge(
			PresupuestoDetCuotasEsp detachedInstance) {
		log.debug("merging PresupuestoDetCuotasEsp instance");
		try {
			PresupuestoDetCuotasEsp result = (PresupuestoDetCuotasEsp) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresupuestoDetCuotasEsp findById(int id) {
		log.debug("getting PresupuestoDetCuotasEsp instance with id: " + id);
		try {
			PresupuestoDetCuotasEsp instance = (PresupuestoDetCuotasEsp) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresupuestoDetCuotasEsp",
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

	public List findByExample(PresupuestoDetCuotasEsp instance) {
		log.debug("finding PresupuestoDetCuotasEsp instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresupuestoDetCuotasEsp").add(
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
