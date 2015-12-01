package com.servinte.axioma.orm;

// Generated Sep 24, 2010 5:55:47 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PrioridadOcupMedica.
 * @see com.servinte.axioma.orm.PrioridadOcupMedica
 * @author Hibernate Tools
 */
public class PrioridadOcupMedicaHome {

	private static final Log log = LogFactory
			.getLog(PrioridadOcupMedicaHome.class);

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

	public void persist(PrioridadOcupMedica transientInstance) {
		log.debug("persisting PrioridadOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PrioridadOcupMedica instance) {
		log.debug("attaching dirty PrioridadOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PrioridadOcupMedica instance) {
		log.debug("attaching clean PrioridadOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PrioridadOcupMedica persistentInstance) {
		log.debug("deleting PrioridadOcupMedica instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PrioridadOcupMedica merge(PrioridadOcupMedica detachedInstance) {
		log.debug("merging PrioridadOcupMedica instance");
		try {
			PrioridadOcupMedica result = (PrioridadOcupMedica) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PrioridadOcupMedica findById(int id) {
		log.debug("getting PrioridadOcupMedica instance with id: " + id);
		try {
			PrioridadOcupMedica instance = (PrioridadOcupMedica) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PrioridadOcupMedica", id);
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

	public List findByExample(PrioridadOcupMedica instance) {
		log.debug("finding PrioridadOcupMedica instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PrioridadOcupMedica").add(
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
