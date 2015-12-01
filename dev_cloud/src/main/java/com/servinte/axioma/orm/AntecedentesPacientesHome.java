package com.servinte.axioma.orm;

// Generated 4/05/2011 05:28:56 PM by Hibernate Tools 3.4.0.CR1

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Home object for domain model class AntecedentesPacientes.
 * @see com.servinte.axioma.orm.AntecedentesPacientes
 * @author Hibernate Tools
 */
public class AntecedentesPacientesHome {

	private static final Log log = LogFactory
			.getLog(AntecedentesPacientesHome.class);

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

	public void persist(AntecedentesPacientes transientInstance) {
		log.debug("persisting AntecedentesPacientes instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(AntecedentesPacientes instance) {
		log.debug("attaching dirty AntecedentesPacientes instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(AntecedentesPacientes instance) {
		log.debug("attaching clean AntecedentesPacientes instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(AntecedentesPacientes persistentInstance) {
		log.debug("deleting AntecedentesPacientes instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public AntecedentesPacientes merge(AntecedentesPacientes detachedInstance) {
		log.debug("merging AntecedentesPacientes instance");
		try {
			AntecedentesPacientes result = (AntecedentesPacientes) sessionFactory
					.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public AntecedentesPacientes findById(int id) {
		log.debug("getting AntecedentesPacientes instance with id: " + id);
		try {
			AntecedentesPacientes instance = (AntecedentesPacientes) sessionFactory
					.getCurrentSession()
					.get("com.servinte.axioma.orm.AntecedentesPacientes", id);
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

	public List findByExample(AntecedentesPacientes instance) {
		log.debug("finding AntecedentesPacientes instance by example");
		try {
			List results = sessionFactory
					.getCurrentSession()
					.createCriteria(
							"com.servinte.axioma.orm.AntecedentesPacientes")
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
