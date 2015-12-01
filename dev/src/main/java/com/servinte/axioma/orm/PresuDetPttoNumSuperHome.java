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
 * Home object for domain model class PresuDetPttoNumSuper.
 * @see com.servinte.axioma.orm.PresuDetPttoNumSuper
 * @author Hibernate Tools
 */
public class PresuDetPttoNumSuperHome {

	private static final Log log = LogFactory
			.getLog(PresuDetPttoNumSuperHome.class);

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

	public void persist(PresuDetPttoNumSuper transientInstance) {
		log.debug("persisting PresuDetPttoNumSuper instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PresuDetPttoNumSuper instance) {
		log.debug("attaching dirty PresuDetPttoNumSuper instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PresuDetPttoNumSuper instance) {
		log.debug("attaching clean PresuDetPttoNumSuper instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PresuDetPttoNumSuper persistentInstance) {
		log.debug("deleting PresuDetPttoNumSuper instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PresuDetPttoNumSuper merge(PresuDetPttoNumSuper detachedInstance) {
		log.debug("merging PresuDetPttoNumSuper instance");
		try {
			PresuDetPttoNumSuper result = (PresuDetPttoNumSuper) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PresuDetPttoNumSuper findById(long id) {
		log.debug("getting PresuDetPttoNumSuper instance with id: " + id);
		try {
			PresuDetPttoNumSuper instance = (PresuDetPttoNumSuper) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PresuDetPttoNumSuper", id);
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

	public List findByExample(PresuDetPttoNumSuper instance) {
		log.debug("finding PresuDetPttoNumSuper instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PresuDetPttoNumSuper").add(
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
