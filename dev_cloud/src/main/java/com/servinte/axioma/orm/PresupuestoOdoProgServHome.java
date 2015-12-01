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
 * Home object for domain model class PresupuestoOdoProgServ.
 * @see com.servinte.axioma.orm.PresupuestoOdoProgServ
 * @author Hibernate Tools
 */
public class PresupuestoOdoProgServHome {

	private static final Log log = LogFactory
			.getLog(PresupuestoOdoProgServHome.class);

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

	public void persist(PresupuestoOdoProgServ transientInstance) {
		log.debug("persisting PresupuestoOdoProgServ instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresupuestoOdoProgServ instance) {
		log.debug("attaching dirty PresupuestoOdoProgServ instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresupuestoOdoProgServ instance) {
		log.debug("attaching clean PresupuestoOdoProgServ instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresupuestoOdoProgServ persistentInstance) {
		log.debug("deleting PresupuestoOdoProgServ instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresupuestoOdoProgServ merge(PresupuestoOdoProgServ detachedInstance) {
		log.debug("merging PresupuestoOdoProgServ instance");
		try {
			PresupuestoOdoProgServ result = (PresupuestoOdoProgServ) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresupuestoOdoProgServ findById(long id) {
		log.debug("getting PresupuestoOdoProgServ instance with id: " + id);
		try {
			PresupuestoOdoProgServ instance = (PresupuestoOdoProgServ) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresupuestoOdoProgServ",
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

	public List findByExample(PresupuestoOdoProgServ instance) {
		log.debug("finding PresupuestoOdoProgServ instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresupuestoOdoProgServ").add(
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
