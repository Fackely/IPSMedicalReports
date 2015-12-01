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
 * Home object for domain model class TiposUsuarioCama.
 * @see com.servinte.axioma.orm.TiposUsuarioCama
 * @author Hibernate Tools
 */
public class TiposUsuarioCamaHome {

	private static final Log log = LogFactory
			.getLog(TiposUsuarioCamaHome.class);

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

	public void persist(TiposUsuarioCama transientInstance) {
		log.debug("persisting TiposUsuarioCama instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposUsuarioCama instance) {
		log.debug("attaching dirty TiposUsuarioCama instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposUsuarioCama instance) {
		log.debug("attaching clean TiposUsuarioCama instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposUsuarioCama persistentInstance) {
		log.debug("deleting TiposUsuarioCama instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposUsuarioCama merge(TiposUsuarioCama detachedInstance) {
		log.debug("merging TiposUsuarioCama instance");
		try {
			TiposUsuarioCama result = (TiposUsuarioCama) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposUsuarioCama findById(
			com.servinte.axioma.orm.TiposUsuarioCamaId id) {
		log.debug("getting TiposUsuarioCama instance with id: " + id);
		try {
			TiposUsuarioCama instance = (TiposUsuarioCama) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposUsuarioCama", id);
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

	public List findByExample(TiposUsuarioCama instance) {
		log.debug("finding TiposUsuarioCama instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TiposUsuarioCama").add(
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
