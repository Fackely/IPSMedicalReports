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
 * Home object for domain model class ExCoberArtEntSub.
 * @see com.servinte.axioma.orm.ExCoberArtEntSub
 * @author Hibernate Tools
 */
public class ExCoberArtEntSubHome {

	private static final Log log = LogFactory
			.getLog(ExCoberArtEntSubHome.class);

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

	public void persist(ExCoberArtEntSub transientInstance) {
		log.debug("persisting ExCoberArtEntSub instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(ExCoberArtEntSub instance) {
		log.debug("attaching dirty ExCoberArtEntSub instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ExCoberArtEntSub instance) {
		log.debug("attaching clean ExCoberArtEntSub instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(ExCoberArtEntSub persistentInstance) {
		log.debug("deleting ExCoberArtEntSub instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ExCoberArtEntSub merge(ExCoberArtEntSub detachedInstance) {
		log.debug("merging ExCoberArtEntSub instance");
		try {
			ExCoberArtEntSub result = (ExCoberArtEntSub) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public ExCoberArtEntSub findById(long id) {
		log.debug("getting ExCoberArtEntSub instance with id: " + id);
		try {
			ExCoberArtEntSub instance = (ExCoberArtEntSub) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.ExCoberArtEntSub", id);
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

	public List findByExample(ExCoberArtEntSub instance) {
		log.debug("finding ExCoberArtEntSub instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.ExCoberArtEntSub").add(
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
