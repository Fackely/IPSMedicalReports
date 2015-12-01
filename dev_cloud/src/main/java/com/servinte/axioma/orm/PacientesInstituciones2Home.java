package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:55 PM by Hibernate Tools 3.2.5.Beta

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class PacientesInstituciones2.
 * @see com.servinte.axioma.orm.PacientesInstituciones2
 * @author Hibernate Tools
 */
public class PacientesInstituciones2Home {

	private static final Log log = LogFactory
			.getLog(PacientesInstituciones2Home.class);

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

	public void persist(PacientesInstituciones2 transientInstance) {
		log.debug("persisting PacientesInstituciones2 instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(PacientesInstituciones2 instance) {
		log.debug("attaching dirty PacientesInstituciones2 instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PacientesInstituciones2 instance) {
		log.debug("attaching clean PacientesInstituciones2 instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(PacientesInstituciones2 persistentInstance) {
		log.debug("deleting PacientesInstituciones2 instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PacientesInstituciones2 merge(
			PacientesInstituciones2 detachedInstance) {
		log.debug("merging PacientesInstituciones2 instance");
		try {
			PacientesInstituciones2 result = (PacientesInstituciones2) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public PacientesInstituciones2 findById(
			com.servinte.axioma.orm.PacientesInstituciones2Id id) {
		log.debug("getting PacientesInstituciones2 instance with id: " + id);
		try {
			PacientesInstituciones2 instance = (PacientesInstituciones2) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.PacientesInstituciones2",
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

	public List findByExample(PacientesInstituciones2 instance) {
		log.debug("finding PacientesInstituciones2 instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.PacientesInstituciones2").add(
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
