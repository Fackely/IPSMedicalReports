package com.servinte.axioma.orm;

// Generated Sep 23, 2010 2:39:35 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class NivelAutorUsuEspec.
 * @see com.servinte.axioma.orm.NivelAutorUsuEspec
 * @author Hibernate Tools
 */
public class NivelAutorUsuEspecHome {

	private static final Log log = LogFactory
			.getLog(NivelAutorUsuEspecHome.class);

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

	public void persist(NivelAutorUsuEspec transientInstance) {
		log.debug("persisting NivelAutorUsuEspec instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(NivelAutorUsuEspec instance) {
		log.debug("attaching dirty NivelAutorUsuEspec instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(NivelAutorUsuEspec instance) {
		log.debug("attaching clean NivelAutorUsuEspec instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(NivelAutorUsuEspec persistentInstance) {
		log.debug("deleting NivelAutorUsuEspec instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public NivelAutorUsuEspec merge(NivelAutorUsuEspec detachedInstance) {
		log.debug("merging NivelAutorUsuEspec instance");
		try {
			NivelAutorUsuEspec result = (NivelAutorUsuEspec) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public NivelAutorUsuEspec findById(int id) {
		log.debug("getting NivelAutorUsuEspec instance with id: " + id);
		try {
			NivelAutorUsuEspec instance = (NivelAutorUsuEspec) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.NivelAutorUsuEspec", id);
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

	public List findByExample(NivelAutorUsuEspec instance) {
		log.debug("finding NivelAutorUsuEspec instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.NivelAutorUsuEspec").add(
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
