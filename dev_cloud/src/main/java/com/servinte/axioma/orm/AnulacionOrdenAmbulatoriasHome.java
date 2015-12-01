package com.servinte.axioma.orm;

// Generated 1/06/2011 10:54:46 AM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AnulacionOrdenAmbulatorias.
 * @see com.servinte.axioma.orm.AnulacionOrdenAmbulatorias
 * @author Hibernate Tools
 */
public class AnulacionOrdenAmbulatoriasHome {

	private static final Log log = LogFactory
			.getLog(AnulacionOrdenAmbulatoriasHome.class);

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

	public void persist(AnulacionOrdenAmbulatorias transientInstance) {
		log.debug("persisting AnulacionOrdenAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AnulacionOrdenAmbulatorias instance) {
		log.debug("attaching dirty AnulacionOrdenAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AnulacionOrdenAmbulatorias instance) {
		log.debug("attaching clean AnulacionOrdenAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AnulacionOrdenAmbulatorias persistentInstance) {
		log.debug("deleting AnulacionOrdenAmbulatorias instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AnulacionOrdenAmbulatorias merge(
			AnulacionOrdenAmbulatorias detachedInstance) {
		log.debug("merging AnulacionOrdenAmbulatorias instance");
		try {
			AnulacionOrdenAmbulatorias result = (AnulacionOrdenAmbulatorias) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AnulacionOrdenAmbulatorias findById(long id) {
		log.debug("getting AnulacionOrdenAmbulatorias instance with id: " + id);
		try {
			AnulacionOrdenAmbulatorias instance = (AnulacionOrdenAmbulatorias) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AnulacionOrdenAmbulatorias",
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

	public List findByExample(AnulacionOrdenAmbulatorias instance) {
		log.debug("finding AnulacionOrdenAmbulatorias instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.AnulacionOrdenAmbulatorias")
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
