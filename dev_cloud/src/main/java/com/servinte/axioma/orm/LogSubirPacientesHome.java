package com.servinte.axioma.orm;

// Generated Feb 23, 2011 6:13:17 PM by Hibernate Tools 3.2.4.GA

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class LogSubirPacientes.
 * @see com.servinte.axioma.orm.LogSubirPacientes
 * @author Hibernate Tools
 */
public class LogSubirPacientesHome {

	private static final Log log = LogFactory
			.getLog(LogSubirPacientesHome.class);

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

	public void persist(LogSubirPacientes transientInstance) {
		log.debug("persisting LogSubirPacientes instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(LogSubirPacientes instance) {
		log.debug("attaching dirty LogSubirPacientes instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(LogSubirPacientes instance) {
		log.debug("attaching clean LogSubirPacientes instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(LogSubirPacientes persistentInstance) {
		log.debug("deleting LogSubirPacientes instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public LogSubirPacientes merge(LogSubirPacientes detachedInstance) {
		log.debug("merging LogSubirPacientes instance");
		try {
			LogSubirPacientes result = (LogSubirPacientes) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public LogSubirPacientes findById(long id) {
		log.debug("getting LogSubirPacientes instance with id: " + id);
		try {
			LogSubirPacientes instance = (LogSubirPacientes) sessionFactory
					.getCurrentSession().get(
							"com.servinte.axioma.orm.LogSubirPacientes", id);
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

	public List findByExample(LogSubirPacientes instance) {
		log.debug("finding LogSubirPacientes instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria(
					"com.servinte.axioma.orm.LogSubirPacientes").add(
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
