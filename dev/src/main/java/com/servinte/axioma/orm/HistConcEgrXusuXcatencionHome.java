package com.servinte.axioma.orm;

// Generated Apr 14, 2010 9:09:52 AM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class HistConcEgrXusuXcatencion.
 * @see com.servinte.axioma.orm.HistConcEgrXusuXcatencion
 * @author Hibernate Tools
 */
public class HistConcEgrXusuXcatencionHome {

	private static final Log log = LogFactory
			.getLog(HistConcEgrXusuXcatencionHome.class);

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

	public void persist(HistConcEgrXusuXcatencion transientInstance) {
		log.debug("persisting HistConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HistConcEgrXusuXcatencion instance) {
		log.debug("attaching dirty HistConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HistConcEgrXusuXcatencion instance) {
		log.debug("attaching clean HistConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HistConcEgrXusuXcatencion persistentInstance) {
		log.debug("deleting HistConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HistConcEgrXusuXcatencion merge(
			HistConcEgrXusuXcatencion detachedInstance) {
		log.debug("merging HistConcEgrXusuXcatencion instance");
		try {
			HistConcEgrXusuXcatencion result = (HistConcEgrXusuXcatencion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HistConcEgrXusuXcatencion findById(long id) {
		log.debug("getting HistConcEgrXusuXcatencion instance with id: " + id);
		try {
			HistConcEgrXusuXcatencion instance = (HistConcEgrXusuXcatencion) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.HistConcEgrXusuXcatencion",
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

	public List findByExample(HistConcEgrXusuXcatencion instance) {
		log.debug("finding HistConcEgrXusuXcatencion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HistConcEgrXusuXcatencion").add(
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