package com.servinte.axioma.orm;

// Generated 30/05/2011 09:38:01 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PeticionesPosponer.
 * @see com.servinte.axioma.orm.PeticionesPosponer
 * @author Hibernate Tools
 */
public class PeticionesPosponerHome {

	private static final Log log = LogFactory
			.getLog(PeticionesPosponerHome.class);

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

	public void persist(PeticionesPosponer transientInstance) {
		log.debug("persisting PeticionesPosponer instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PeticionesPosponer instance) {
		log.debug("attaching dirty PeticionesPosponer instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PeticionesPosponer instance) {
		log.debug("attaching clean PeticionesPosponer instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PeticionesPosponer persistentInstance) {
		log.debug("deleting PeticionesPosponer instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PeticionesPosponer merge(PeticionesPosponer detachedInstance) {
		log.debug("merging PeticionesPosponer instance");
		try {
			PeticionesPosponer result = (PeticionesPosponer) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PeticionesPosponer findById(long id) {
		log.debug("getting PeticionesPosponer instance with id: " + id);
		try {
			PeticionesPosponer instance = (PeticionesPosponer) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PeticionesPosponer", id);
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

	public List findByExample(PeticionesPosponer instance) {
		log.debug("finding PeticionesPosponer instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.PeticionesPosponer")
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
