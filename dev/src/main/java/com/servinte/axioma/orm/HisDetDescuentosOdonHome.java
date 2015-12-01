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
 * Home object for domain model class HisDetDescuentosOdon.
 * @see com.servinte.axioma.orm.HisDetDescuentosOdon
 * @author Hibernate Tools
 */
public class HisDetDescuentosOdonHome {

	private static final Log log = LogFactory
			.getLog(HisDetDescuentosOdonHome.class);

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

	public void persist(HisDetDescuentosOdon transientInstance) {
		log.debug("persisting HisDetDescuentosOdon instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(HisDetDescuentosOdon instance) {
		log.debug("attaching dirty HisDetDescuentosOdon instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(HisDetDescuentosOdon instance) {
		log.debug("attaching clean HisDetDescuentosOdon instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(HisDetDescuentosOdon persistentInstance) {
		log.debug("deleting HisDetDescuentosOdon instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public HisDetDescuentosOdon merge(HisDetDescuentosOdon detachedInstance) {
		log.debug("merging HisDetDescuentosOdon instance");
		try {
			HisDetDescuentosOdon result = (HisDetDescuentosOdon) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public HisDetDescuentosOdon findById(long id) {
		log.debug("getting HisDetDescuentosOdon instance with id: " + id);
		try {
			HisDetDescuentosOdon instance = (HisDetDescuentosOdon) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.HisDetDescuentosOdon", id);
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

	public List findByExample(HisDetDescuentosOdon instance) {
		log.debug("finding HisDetDescuentosOdon instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.HisDetDescuentosOdon").add(
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
