package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistoricoDistribucion.
 * @see com.servinte.axioma.orm.HistoricoDistribucion
 * @author Hibernate Tools
 */
public class HistoricoDistribucionHome {

	private static final Log log = LogFactory
			.getLog(HistoricoDistribucionHome.class);

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

	public void persist(HistoricoDistribucion transientInstance) {
		log.debug("persisting HistoricoDistribucion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistoricoDistribucion instance) {
		log.debug("attaching dirty HistoricoDistribucion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistoricoDistribucion instance) {
		log.debug("attaching clean HistoricoDistribucion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistoricoDistribucion persistentInstance) {
		log.debug("deleting HistoricoDistribucion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistoricoDistribucion merge(HistoricoDistribucion detachedInstance) {
		log.debug("merging HistoricoDistribucion instance");
		try {
			HistoricoDistribucion result = (HistoricoDistribucion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistoricoDistribucion findById(
			com.servinte.axioma.orm.HistoricoDistribucionId id) {
		log.debug("getting HistoricoDistribucion instance with id: " + id);
		try {
			HistoricoDistribucion instance = (HistoricoDistribucion) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.HistoricoDistribucion", id);
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

	public List findByExample(HistoricoDistribucion instance) {
		log.debug("finding HistoricoDistribucion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistoricoDistribucion").add(
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
