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
 * Home object for domain model class PresupuestoLogImpresion.
 * @see com.servinte.axioma.orm.PresupuestoLogImpresion
 * @author Hibernate Tools
 */
public class PresupuestoLogImpresionHome {

	private static final Log log = LogFactory
			.getLog(PresupuestoLogImpresionHome.class);

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

	public void persist(PresupuestoLogImpresion transientInstance) {
		log.debug("persisting PresupuestoLogImpresion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresupuestoLogImpresion instance) {
		log.debug("attaching dirty PresupuestoLogImpresion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresupuestoLogImpresion instance) {
		log.debug("attaching clean PresupuestoLogImpresion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresupuestoLogImpresion persistentInstance) {
		log.debug("deleting PresupuestoLogImpresion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresupuestoLogImpresion merge(
			PresupuestoLogImpresion detachedInstance) {
		log.debug("merging PresupuestoLogImpresion instance");
		try {
			PresupuestoLogImpresion result = (PresupuestoLogImpresion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresupuestoLogImpresion findById(long id) {
		log.debug("getting PresupuestoLogImpresion instance with id: " + id);
		try {
			PresupuestoLogImpresion instance = (PresupuestoLogImpresion) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresupuestoLogImpresion",
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

	public List findByExample(PresupuestoLogImpresion instance) {
		log.debug("finding PresupuestoLogImpresion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresupuestoLogImpresion").add(
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
