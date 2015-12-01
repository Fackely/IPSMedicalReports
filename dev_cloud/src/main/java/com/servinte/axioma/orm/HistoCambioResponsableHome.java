package com.servinte.axioma.orm;

import com.servinte.axioma.hibernate.HibernateUtil;

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class HistoCambioResponsable.
 * @see com.servinte.axioma.orm.HistoCambioResponsable
 * @author Hibernate Tools
 */
public class HistoCambioResponsableHome {

	private static final Log log = LogFactory
			.getLog(HistoCambioResponsableHome.class);

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

	public void persist(HistoCambioResponsable transientInstance) {
		log.debug("persisting HistoCambioResponsable instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistoCambioResponsable instance) {
		log.debug("attaching dirty HistoCambioResponsable instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistoCambioResponsable instance) {
		log.debug("attaching clean HistoCambioResponsable instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistoCambioResponsable persistentInstance) {
		log.debug("deleting HistoCambioResponsable instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistoCambioResponsable merge(HistoCambioResponsable detachedInstance) {
		log.debug("merging HistoCambioResponsable instance");
		try {
			HistoCambioResponsable result = (HistoCambioResponsable) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistoCambioResponsable findById(long id) {
		log.debug("getting HistoCambioResponsable instance with id: " + id);
		try {
			HistoCambioResponsable instance = (HistoCambioResponsable) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HistoCambioResponsable",
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

	public List findByExample(HistoCambioResponsable instance) {
		log.debug("finding HistoCambioResponsable instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistoCambioResponsable").add(
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
