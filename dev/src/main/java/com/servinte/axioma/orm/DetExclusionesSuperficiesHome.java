package com.servinte.axioma.orm;

// Generated Jun 21, 2010 11:07:52 AM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class DetExclusionesSuperficies.
 * @see com.servinte.axioma.orm.DetExclusionesSuperficies
 * @author Hibernate Tools
 */
public class DetExclusionesSuperficiesHome {

	private static final Log log = LogFactory
			.getLog(DetExclusionesSuperficiesHome.class);

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

	public void persist(DetExclusionesSuperficies transientInstance) {
		log.debug("persisting DetExclusionesSuperficies instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(DetExclusionesSuperficies instance) {
		log.debug("attaching dirty DetExclusionesSuperficies instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(DetExclusionesSuperficies instance) {
		log.debug("attaching clean DetExclusionesSuperficies instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(DetExclusionesSuperficies persistentInstance) {
		log.debug("deleting DetExclusionesSuperficies instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public DetExclusionesSuperficies merge(
			DetExclusionesSuperficies detachedInstance) {
		log.debug("merging DetExclusionesSuperficies instance");
		try {
			DetExclusionesSuperficies result = (DetExclusionesSuperficies) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public DetExclusionesSuperficies findById(long id) {
		log.debug("getting DetExclusionesSuperficies instance with id: " + id);
		try {
			DetExclusionesSuperficies instance = (DetExclusionesSuperficies) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.DetExclusionesSuperficies",
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

	public List findByExample(DetExclusionesSuperficies instance) {
		log.debug("finding DetExclusionesSuperficies instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.DetExclusionesSuperficies").add(
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
