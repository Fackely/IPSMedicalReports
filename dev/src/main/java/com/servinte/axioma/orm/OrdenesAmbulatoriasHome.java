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
 * Home object for domain model class OrdenesAmbulatorias.
 * @see com.servinte.axioma.orm.OrdenesAmbulatorias
 * @author Hibernate Tools
 */
public class OrdenesAmbulatoriasHome {

	private static final Log log = LogFactory
			.getLog(OrdenesAmbulatoriasHome.class);

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

	public void persist(OrdenesAmbulatorias transientInstance) {
		log.debug("persisting OrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(OrdenesAmbulatorias instance) {
		log.debug("attaching dirty OrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OrdenesAmbulatorias instance) {
		log.debug("attaching clean OrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(OrdenesAmbulatorias persistentInstance) {
		log.debug("deleting OrdenesAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public OrdenesAmbulatorias merge(OrdenesAmbulatorias detachedInstance) {
		log.debug("merging OrdenesAmbulatorias instance");
		try {
			OrdenesAmbulatorias result = (OrdenesAmbulatorias) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public OrdenesAmbulatorias findById(long id) {
		log.debug("getting OrdenesAmbulatorias instance with id: " + id);
		try {
			OrdenesAmbulatorias instance = (OrdenesAmbulatorias) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.OrdenesAmbulatorias", id);
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

	public List findByExample(OrdenesAmbulatorias instance) {
		log.debug("finding OrdenesAmbulatorias instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.OrdenesAmbulatorias").add(
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
