package com.servinte.axioma.orm;

// Generated Dec 14, 2010 2:25:25 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class SolicitudesMedicamentos.
 * @see com.servinte.axioma.orm.SolicitudesMedicamentos
 * @author Hibernate Tools
 */
public class SolicitudesMedicamentosHome {

	private static final Log log = LogFactory
			.getLog(SolicitudesMedicamentosHome.class);

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

	public void persist(SolicitudesMedicamentos transientInstance) {
		log.debug("persisting SolicitudesMedicamentos instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(SolicitudesMedicamentos instance) {
		log.debug("attaching dirty SolicitudesMedicamentos instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SolicitudesMedicamentos instance) {
		log.debug("attaching clean SolicitudesMedicamentos instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(SolicitudesMedicamentos persistentInstance) {
		log.debug("deleting SolicitudesMedicamentos instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public SolicitudesMedicamentos merge(
			SolicitudesMedicamentos detachedInstance) {
		log.debug("merging SolicitudesMedicamentos instance");
		try {
			SolicitudesMedicamentos result = (SolicitudesMedicamentos) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public SolicitudesMedicamentos findById(int id) {
		log.debug("getting SolicitudesMedicamentos instance with id: " + id);
		try {
			SolicitudesMedicamentos instance = (SolicitudesMedicamentos) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.SolicitudesMedicamentos",
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

	public List findByExample(SolicitudesMedicamentos instance) {
		log.debug("finding SolicitudesMedicamentos instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.SolicitudesMedicamentos").add(
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
