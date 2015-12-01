package com.servinte.axioma.orm;

// Generated Mar 29, 2011 6:00:01 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class TiposOrdenesAmbulatorias.
 * @see com.servinte.axioma.orm.TiposOrdenesAmbulatorias
 * @author Hibernate Tools
 */
public class TiposOrdenesAmbulatoriasHome {

	private static final Log log = LogFactory
			.getLog(TiposOrdenesAmbulatoriasHome.class);

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

	public void persist(TiposOrdenesAmbulatorias transientInstance) {
		log.debug("persisting TiposOrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(TiposOrdenesAmbulatorias instance) {
		log.debug("attaching dirty TiposOrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TiposOrdenesAmbulatorias instance) {
		log.debug("attaching clean TiposOrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(TiposOrdenesAmbulatorias persistentInstance) {
		log.debug("deleting TiposOrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public TiposOrdenesAmbulatorias merge(
			TiposOrdenesAmbulatorias detachedInstance) {
		log.debug("merging TiposOrdenesAmbulatorias instance");
		try {
			TiposOrdenesAmbulatorias result = (TiposOrdenesAmbulatorias) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public TiposOrdenesAmbulatorias findById(byte id) {
		log.debug("getting TiposOrdenesAmbulatorias instance with id: " + id);
		try {
			TiposOrdenesAmbulatorias instance = (TiposOrdenesAmbulatorias) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.TiposOrdenesAmbulatorias",
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

	public List findByExample(TiposOrdenesAmbulatorias instance) {
		log.debug("finding TiposOrdenesAmbulatorias instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.TiposOrdenesAmbulatorias").add(
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
