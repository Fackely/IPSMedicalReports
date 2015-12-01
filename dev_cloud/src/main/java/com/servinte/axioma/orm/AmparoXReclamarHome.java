package com.servinte.axioma.orm;

// Generated Feb 15, 2011 5:27:26 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AmparoXReclamar.
 * @see com.servinte.axioma.orm.AmparoXReclamar
 * @author Hibernate Tools
 */
public class AmparoXReclamarHome {

	private static final Log log = LogFactory.getLog(AmparoXReclamarHome.class);

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

	public void persist(AmparoXReclamar transientInstance) {
		log.debug("persisting AmparoXReclamar instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AmparoXReclamar instance) {
		log.debug("attaching dirty AmparoXReclamar instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AmparoXReclamar instance) {
		log.debug("attaching clean AmparoXReclamar instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AmparoXReclamar persistentInstance) {
		log.debug("deleting AmparoXReclamar instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AmparoXReclamar merge(AmparoXReclamar detachedInstance) {
		log.debug("merging AmparoXReclamar instance");
		try {
			AmparoXReclamar result = (AmparoXReclamar) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AmparoXReclamar findById(long id) {
		log.debug("getting AmparoXReclamar instance with id: " + id);
		try {
			AmparoXReclamar instance = (AmparoXReclamar) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.AmparoXReclamar", id);
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

	public List findByExample(AmparoXReclamar instance) {
		log.debug("finding AmparoXReclamar instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.AmparoXReclamar").add(
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
