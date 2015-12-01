package com.servinte.axioma.orm;

// Generated Sep 3, 2010 2:49:57 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistoDetalleMonto.
 * @see com.servinte.axioma.orm.HistoDetalleMonto
 * @author Hibernate Tools
 */
public class HistoDetalleMontoHome {

	private static final Log log = LogFactory
			.getLog(HistoDetalleMontoHome.class);

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

	public void persist(HistoDetalleMonto transientInstance) {
		log.debug("persisting HistoDetalleMonto instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistoDetalleMonto instance) {
		log.debug("attaching dirty HistoDetalleMonto instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistoDetalleMonto instance) {
		log.debug("attaching clean HistoDetalleMonto instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistoDetalleMonto persistentInstance) {
		log.debug("deleting HistoDetalleMonto instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistoDetalleMonto merge(HistoDetalleMonto detachedInstance) {
		log.debug("merging HistoDetalleMonto instance");
		try {
			HistoDetalleMonto result = (HistoDetalleMonto) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistoDetalleMonto findById(int id) {
		log.debug("getting HistoDetalleMonto instance with id: " + id);
		try {
			HistoDetalleMonto instance = (HistoDetalleMonto) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistoDetalleMonto", id);
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

	public List findByExample(HistoDetalleMonto instance) {
		log.debug("finding HistoDetalleMonto instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistoDetalleMonto").add(
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
