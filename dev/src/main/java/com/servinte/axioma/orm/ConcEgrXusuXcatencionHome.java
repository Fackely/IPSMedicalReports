package com.servinte.axioma.orm;

// Generated Apr 9, 2010 2:09:33 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class ConcEgrXusuXcatencion.
 * @see com.servinte.axioma.orm.ConcEgrXusuXcatencion
 * @author Hibernate Tools
 */
public class ConcEgrXusuXcatencionHome {

	private static final Log log = LogFactory
			.getLog(ConcEgrXusuXcatencionHome.class);

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

	public void persist(ConcEgrXusuXcatencion transientInstance) {
		log.debug("persisting ConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ConcEgrXusuXcatencion instance) {
		log.debug("attaching dirty ConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ConcEgrXusuXcatencion instance) {
		log.debug("attaching clean ConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ConcEgrXusuXcatencion persistentInstance) {
		log.debug("deleting ConcEgrXusuXcatencion instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ConcEgrXusuXcatencion merge(ConcEgrXusuXcatencion detachedInstance) {
		log.debug("merging ConcEgrXusuXcatencion instance");
		try {
			ConcEgrXusuXcatencion result = (ConcEgrXusuXcatencion) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ConcEgrXusuXcatencion findById(long id) {
		log.debug("getting ConcEgrXusuXcatencion instance with id: " + id);
		try {
			ConcEgrXusuXcatencion instance = (ConcEgrXusuXcatencion) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.ConcEgrXusuXcatencion", id);
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

	public List findByExample(ConcEgrXusuXcatencion instance) {
		log.debug("finding ConcEgrXusuXcatencion instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ConcEgrXusuXcatencion").add(
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
