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
 * Home object for domain model class PresupuestoOdoConvenio.
 * @see com.servinte.axioma.orm.PresupuestoOdoConvenio
 * @author Hibernate Tools
 */
public class PresupuestoOdoConvenioHome {

	private static final Log log = LogFactory
			.getLog(PresupuestoOdoConvenioHome.class);

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

	public void persist(PresupuestoOdoConvenio transientInstance) {
		log.debug("persisting PresupuestoOdoConvenio instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresupuestoOdoConvenio instance) {
		log.debug("attaching dirty PresupuestoOdoConvenio instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresupuestoOdoConvenio instance) {
		log.debug("attaching clean PresupuestoOdoConvenio instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresupuestoOdoConvenio persistentInstance) {
		log.debug("deleting PresupuestoOdoConvenio instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresupuestoOdoConvenio merge(PresupuestoOdoConvenio detachedInstance) {
		log.debug("merging PresupuestoOdoConvenio instance");
		try {
			PresupuestoOdoConvenio result = (PresupuestoOdoConvenio) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresupuestoOdoConvenio findById(long id) {
		log.debug("getting PresupuestoOdoConvenio instance with id: " + id);
		try {
			PresupuestoOdoConvenio instance = (PresupuestoOdoConvenio) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresupuestoOdoConvenio",
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

	public List findByExample(PresupuestoOdoConvenio instance) {
		log.debug("finding PresupuestoOdoConvenio instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresupuestoOdoConvenio").add(
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
