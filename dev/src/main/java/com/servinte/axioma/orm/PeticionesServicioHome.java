package com.servinte.axioma.orm;

// Generated 1/06/2011 12:09:46 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PeticionesServicio.
 * @see com.servinte.axioma.orm.PeticionesServicio
 * @author Hibernate Tools
 */
public class PeticionesServicioHome {

	private static final Log log = LogFactory
			.getLog(PeticionesServicioHome.class);

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

	public void persist(PeticionesServicio transientInstance) {
		log.debug("persisting PeticionesServicio instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PeticionesServicio instance) {
		log.debug("attaching dirty PeticionesServicio instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PeticionesServicio instance) {
		log.debug("attaching clean PeticionesServicio instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PeticionesServicio persistentInstance) {
		log.debug("deleting PeticionesServicio instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PeticionesServicio merge(PeticionesServicio detachedInstance) {
		log.debug("merging PeticionesServicio instance");
		try {
			PeticionesServicio result = (PeticionesServicio) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PeticionesServicio findById(
			com.servinte.axioma.orm.PeticionesServicioId id) {
		log.debug("getting PeticionesServicio instance with id: " + id);
		try {
			PeticionesServicio instance = (PeticionesServicio) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PeticionesServicio", id);
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

	public List findByExample(PeticionesServicio instance) {
		log.debug("finding PeticionesServicio instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.PeticionesServicio")
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
