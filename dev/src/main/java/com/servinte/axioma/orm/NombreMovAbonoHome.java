package com.servinte.axioma.orm;

// Generated 18/07/2011 06:11:30 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NombreMovAbono.
 * @see com.servinte.axioma.orm.NombreMovAbono
 * @author Hibernate Tools
 */
public class NombreMovAbonoHome {

	private static final Log log = LogFactory.getLog(NombreMovAbonoHome.class);

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

	public void persist(NombreMovAbono transientInstance) {
		log.debug("persisting NombreMovAbono instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NombreMovAbono instance) {
		log.debug("attaching dirty NombreMovAbono instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NombreMovAbono instance) {
		log.debug("attaching clean NombreMovAbono instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NombreMovAbono persistentInstance) {
		log.debug("deleting NombreMovAbono instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NombreMovAbono merge(NombreMovAbono detachedInstance) {
		log.debug("merging NombreMovAbono instance");
		try {
			NombreMovAbono result = (NombreMovAbono) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NombreMovAbono findById(long id) {
		log.debug("getting NombreMovAbono instance with id: " + id);
		try {
			NombreMovAbono instance = (NombreMovAbono) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NombreMovAbono", id);
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

	public List findByExample(NombreMovAbono instance) {
		log.debug("finding NombreMovAbono instance by example");
		try {
			List results = sessionFactory.getCurrentSession()
					.createCriteria("com.servinte.axioma.orm.NombreMovAbono")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
